# 7: #
# AdminBinding #
AdminBinding permite al desarrollador heredar de esta clase, y poder acceder a los métodos de interacción con la Base de Datos a través del propio Objeto, sin tener que interactuar directamente con AdminBase.
Como su nombre lo indica, la función de AdminBinding es justamente la de establecer un vinculo o enlace entre los Objetos del Modelo de Datos y AdminBase. De esta forma la interacción podría realizarse de forma mas natural inclusive al decirle al propio objeto la operación que se quiere ejecutar.
Es necesario antes de comenzar a trabajar con las entidades del modelo ejecutar la siguiente linea de código, la cual inicializara la conexión con la Base de Datos para todas las instancias del Modelo (se encuentren o no creadas a este momento):

```
AdminBase.initializeAdminBinding(AdminBase.DATABASE.MYSQL,
        "[HOST]", "[PORT]", "[DATABASE]", "[USER]", "[PASSWORD]");
```

Donde las Propiedades son las mismas que para la inicialización de AdminBase:
  * El DBMS a utilizar.
  * Host
  * Puerto
  * Base de Datos
  * Usuario
  * Password
  * Schema (Solo para Postgre)

Los Métodos disponibles al heredar de esta Clase son:

  * save
  * saveGetIndex
  * delete
  * modify
  * obtain
  * obtainSelect
  * obtainWhere
  * lazyLoad

Métodos como "obtainAll", "saveAll", etc. Se han dejado afuera porque no se refieren específicamente al objeto que los contiene, sino que se refieren a colecciones u  operaciones independientes del objeto.

Ejemplos Basados en la siguiente entidad:

```
import cat.quickdb.db.AdminBinding;

public class Person extends AdminBinding{
    
    private int id;
    private String name;
    private int age;
    
    public Person(){}
    public Person(String name, int age){
        this.name = name;
        this.age = age;
    }
    
    //Getters
    //Setters
    
}
```


## 7.1 ##
## Operaciones ##
Las operaciones se especifican de la misma forma que se hace en “AdminBase” con la diferencia de que se omite de pasar el primer atributo que era el Objeto en si sobre el que se deseaba realizar la operación, ya que ahora el Objeto pasaría a ser el que ejecutaría las operaciones.

Ejemplos:

### 7.1.1 ###
### save ###
```
Person person = new Person("diego", 23);
person.save();
```

### 7.1.2 ###
### saveGetIndex ###
```
Person person = new Person("diego", 23);
int id = person.saveGetIndex();
```

### 7.1.3 ###
### modify ###
```
Person p = new Person();
p.obtain("age=23");

p.setName("Leonardo");
p.modify();
```

### 7.1.4 ###
### obtain ###
_(Los dos sistemas de Consultas de QuickDB son soportados)_
```
Person p = new Person();
p.obtain("name = 'diego'");

p.obtain().where("street", Address.class).equal("unnamed street").find();
```

### 7.1.5 ###
### obtainSelect ###
```
Person p = new Person();
p.obtainSelect("SELECT * FROM Person WHERE age=23");
```

### 7.1.6 ###
### obtainWhere ###
```
Person p = new Person();
p.obtainWhere("age=23");
```

### 7.1.7 ###
### lazyLoad ###
```
public class Person extends AdminBinding{
    private int id;
    private String name;
    private Phone phone;
    //Getters - Setters
}

public class Phone extends AdminBinding{
    private int id;
    private String number;
    private Company company;
    //Getters - Setters
}

public class Company extends AdminBinding{
    private int id;
    private String description;
    //Getters - Setters
}
```

Se ejecutaría la operación de la siguiente forma:

```
Person p = new Person();

//Atributos de Person(1)
p.lazyLoad("age=23");
//Phone es NULL en este momento

//Agregamos los atributos de Phone
p.lazyLoad("");
//Phone ya tiene sus valores, salvo por company que es NULL

//Agrega los valores de Company
p.lazyLoad("");
```