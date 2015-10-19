# 10: #
# Vistas #

Las Vistas en QuickDB se utilizan para simplificar las representaciones de Estructuras de Datos complejas, por ejemplo Objetos compuestos por diversos Objetos, etc.
Una Vista brinda la posibilidad de obtener los datos con mayor rapidez, ya que no es necesario procesar jerárquicamente todos los Objetos involucrados, sino que se asignarán los valores especificados directamente a los atributos de la Vista disminuyendo de gran manera el tiempo de procesamiento.

Para armar una Vista es necesario crear una Clase que extienda de “View”, la cual especifica un método denominado **“query()”**  que debe ser implementado por la Clase Hija.
Este método **“query()”** retorna un dato de tipo “Object” el que bien puede ser un String conteniendo en una cadena la especificación de la consulta, o puede ser un Objeto del Tipo
“QuickDB Query” permitiendo armar la consulta con las facilidades de este sistema de consultas.

Existen otros 3 métodos los cuales son opcionales, y dependen del tipo de Vista que se quiera generar:

  * **renameColumns():** Se especifica en una cadena los nuevos nombres que tomara cada uno de los campos resultantes de la consulta. Estos nombres serán los que se utilizarán para obtener los valores y asignarlos a los atributos específicos dentro de la Vista. Si este método no se implementa, se toma por defecto como nuevos nombres los nombres de cada uno de los Atributos de la Vista en el orden en que figuran.
  * **columns():** Se especifica en una Cadena cuales atributos se tendrán en cuenta en la consulta, si no se implementa este método, se toma como los nombres de cada uno de los Atributos de la Vista en el orden en que figuran.
  * **classes():** Especifica en un Array del tipo Class[.md](.md) las clases a las que pertenecen cada uno de los atributos especificados en “columns()”. Si este método no se implementa, se asume que todos los atributos son de la Clase Base especificada en la consulta.

## 10.1 ##
## Ejemplos ##

En base al Modelo de Datos siguiente:

```
public class ObjectViewTest1{
    private int id;
    private String name;
    private String account;
    private ObjectViewTest2 obj2;
}

public class ObjectViewTest2{
    private int id;
    private String description;
    private Date date;
}
```

Las Vistas creadas son las siguientes:

  1. Haciendo uso del Sistema de consultas “QuickDB Query”, y reescribiendo todos los métodos de configuración de View.
```
import cat.quickdb.db.View;
import cat.quickdb.query.Query;
import java.sql.Date;

public class ViewObject extends View{

    private String name;
    private String description;
    private String account;
    private Date dateView;

    @Override
    public Object query(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        Query query = Query.create(this.getAdminBase(), o1);
        query.where("account", ObjectViewTest1.class).equal("account test");
        return query;
    }

    @Override
    public String columns(){
        return "name, description, account, date";
    }

    @Override
    public String renameColumns(){
        return "name, description, account, dateView";
    }

    @Override
    public Class[] classes(){
        return new Class[]{ObjectViewTest1.class, ObjectViewTest2.class, ObjectViewTest1.class, ObjectViewTest2.class};
    }

}
```

  1. Haciendo uso de la especificación de la consulta en una Cadena de texto.
```
import cat.quickdb.db.View;
import java.sql.Date;

public class ViewObjectString extends View{

    private String name;
    private String description;
    private String account;
    private Date dateView;

    @Override
    public Object query(){
        return "SELECT ObjectViewTest1.name 'name', ObjectViewTest2.description 'description', " +
                "ObjectViewTest1.account 'account', ObjectViewTest2.date 'dateView' " +
                "FROM ObjectViewTest1 " +
                "JOIN ObjectViewTest2 ON ObjectViewTest1.obj2 = ObjectViewTest2.id " +
                "WHERE ObjectViewTest1.account = 'account test'";
    }

}
```

  1. Haciendo uso del Sistema de consultas “QuickDB Query”, sin implementar el método de renombrado de las columnas resultantes.
```
import cat.quickdb.db.View;
import cat.quickdb.query.Query;
import java.sql.Date;

public class ViewObjectWithoutRename extends View{

    private String name;
    private String description;
    private String account;
    private Date date;

    @Override
    public Object query(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        Query query = Query.create(this.getAdminBase(), o1);
        query.where("account", ObjectViewTest1.class).equal("account test");
        return query;
    }

    @Override
    public String columns(){
        return "name, description, account, date";
    }

    @Override
    public Class[] classes(){
        return new Class[]{ObjectViewTest1.class, ObjectViewTest2.class, ObjectViewTest1.class, ObjectViewTest2.class};
    }

}
```

### 10.1.1 ###
### Cómo crear una Instancia de la Vista ###
Existen 2 formas de inicializar una Vista, la primera consiste en ejecutar el método **“initializeViews(...)”** de “AdminBase” el cual inicializará todas las instancias creadas o por crearse del Modelo de Datos. Y la otra forma es pasarle a la Vista una instancia activa de AdminBase:

```
//1:
AdminBase.initializeViews(AdminBase.DATABASE.MYSQL, "[HOST]", "[PORT]", "[DB]", "[USER]", "[PASSWORD]");

//2:
ViewObject view = new ViewObject();
view.initilizeAdminBase(admin);
```

### 10.1.2 ###
### Cómo obtener los Valores de la Vista ###
Una vez creada la instancia de la Vista, sólo es necesario utilizar los métodos **“obtain()”** u **“obtainAll()”** para obtener la/s Vista/s resultantes en base a los objetos de la Base de Datos:

```
//1:
view.obtain();

//2:
ArrayList array = view.obtainAll();
```

### 10.1.3 ###
### Consultas Dinámicas en una Vista ###
Una vista es especificada en base a una consulta que la representa, pero si se quisiera en tiempo de ejecución alterar esa consulta para agregar alguna condición más para que las Vistas obtenidas sean particulares a una situación, se puede recurrir a la utilización del método “dynamicQuery” de la Vista, el cual tanto como se vio en el método “query()” se puede trabajar mediante String o mediante la utilización del sistemas de consultas de QuickDB, y lo que hace es concatenar a la consulta que representa la Vista un nuevo conjunto de condiciones a evaluar.

Ejemplo:

```
//Utilizando String:
ViewObjectString view = new ViewObjectString();
view.initializeView(admin);
view.dynamicQuery("AND name LIKE '%Dynamic%'");
view.obtain();

//Utilizando QuickDB Query
ViewObject view = new ViewObject();
view.initializeView(admin);
view.dynamicQuery( ((Query) view.query()).and("name").match("Dynamic") );
view.obtain();
```