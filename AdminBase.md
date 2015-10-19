# 6: #
# AdminBase #
AdminBase es la Clase principal de la Librería y es con la cual se realizaran todas las operaciones relacionadas con la Base de Datos. Una instancia de AdminBase debe ser creada para poder luego usar ese objeto para los fines antes mencionados.

## 6.1 ##
Al crear la instancia, deben pasarse ciertos parámetros con los que AdminBase podrá realizar la conexión a la Base de Datos:

Los Parámetros de AdminBase son:
  * **Host**
  * **Puerto**
  * **Nombre de la Base de Datos**
  * **Usuario**
  * **Password**
  * **Schema** (Solo para Postgre)

**Ejemplos:**

  * Para MySQL:
```
AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL,
        "localhost", "3306", "testQuickDB", "root", "pass");
```

  * Para Postgre:
```
AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.POSTGRES,
        "localhost", "5432", "testQuickDB", "postgres", "postgres", "testing");
```

  * Para Firebird:
```
AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.FIREBIRD,
        "localhost", "employees.gdb", "SYSDBA", "firebird");
```

  * Para SQLite:
```
AdminBase admin = AdminBase.initialize(
        AdminBase.DATABASE.SQLite, "test");
```

## 6.2 ##
## Operaciones ##
Las operaciones son todas aquellas acciones que pueden realizarse entre un Objeto del Modelo de Datos y la Base de Datos.
Para explicar cada una de las operaciones, se tomara como ejemplo la siguiente clase:

```
public class Person{

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

### 6.2.1 ###
### save ###
Para guardar un Objeto en la Base de Datos, solo debemos crear una instancia del mismo y luego invocar el método “save” para que se encargue de llevar a cabo dicha tarea.

Ejemplo:
```
Person person = new Person("leeloo", 22);
admin.save(person);
```

### 6.2.2 ###
### saveAll ###
Para guardar una colección de objetos, procedemos a crear dicha colección y luego solo ejecutamos "saveAll" de AdminBase.

Ejemplo:
```
ArrayList array = new ArrayList();
array.add(add new Person("name1", 20));
array.add(add new Person("name2", 20));
array.add(add new Person("name3", 32));

admin.saveAll(array);
```

### 6.2.3 ###
### saveGetIndex ###
Para guardar un Objeto en la Base de Datos y obtener el valor del índice generado para el mismo, solo debemos crear una instancia del mismo y luego invocar en AdminBase el método “saveGetIndex”.

Ejemplo:
```
Person person = new Person("leeloo", 22);
admin.saveGetIndex(person);
```

### 6.2.4 ###
### modify ###
Para modificar un Objeto en la Base de Datos, debemos obtener previamente el objeto almacenado en la base de datos, aplicar las modificaciones pertinentes sobre ese objeto y luego ejecutar la modificación.

Ejemplo:
```
Person p = new Person();
admin.obtain(p, "age=22");

p.setName("Leonardo");
admin.modify(p);
```

### 6.2.5 ###
### modifyAll ###
Se aplica el mismo caso que para "modify", se obtiene primero la colección que se desea, se modifican los valores de esa colección y se ejecuta "modifyAll".

Ejemplo:
```
Person p = new Person();
arrayList array = admin.obtainAll(p, "age=22");

for(Object o : array){
   //Realizar Modificaciones
}

admin.modifyAll(array);
```

### 6.2.6 ###
### delete ###
Se obtiene el Objeto que se desea eliminar y luego se ejecuta "delete".

Ejemplo:
```
Person p = new Person();
admin.obtain(p, "age=22");

admin.delete(p);
```

### 6.2.7 ###
### obtain ###
El método “obtain” se encuentra sobrecargado, ya que esta la opción de trabajar con Obtain a través de cualquiera de los 2 Sistemas de Consultas de QuickDB (Los cuales se explicaran en otra sección).
Por lo tanto “obtain” puede recibir como parámetro el Objeto sobre el que se desea obtener los datos, siendo este el sistema de QuickDB denominado “Query”:

```
Person p = new Person();
admin.obtain(p).where("street", Address.class).equal("unnamed street").find();
```

O bien “obtain” puede recibir 2 parámetros, siendo el primer el Objeto sobre el que se desea obtener los datos, y el segundo un String especificando las condiciones de la búsqueda basado en las características del segundo sistema de QuickDB denominado “StringQuery”:

```
Person p = new Person();
admin.obtain(p, "address.street = 'unnamed street'");
```

### 6.2.8 ###
### obtainAll ###
Para obtener de la Base de Datos una Colección de Objetos, solo debemos crear una instancia del mismo vacía y luego decirle a AdminBase que recupere los objetos que cumplan con la condición.

Ejemplo:
```
Person person = new Person();
ArrayList array = admin.obtainAll(person, "age=22");
```
O:
```
ArrayList array = admin.obtainAll(Person.class, "age=22");
```

También esta la opción de realizar la búsqueda de Colecciones de Objetos con el sistema “Query” de QuickDB como se vera en otra sección.

### 6.2.9 ###
### obtainWhere ###
Este método nos permite especificar únicamente la sección del WHERE en una consulta SQL y nos devolverá el objeto resultante.
Es mas rápido que los sistemas de Queries de QuickDB ya que no debe inferir sobre la estructura del Objeto para realizar los JOINs que sean pertinentes para llevar a cabo la consulta, pero por lo tanto solo sirve para consultas simples sobre el mismo objeto, no pudiendo involucrar las relaciones que tenga un Objeto con otro.

Ejemplo:
```
Person p = new Person();
admin.obtainWhere(p, "age=22");
```

### 6.2.10 ###
### obtainSelect ###
Este método devuelve el Objeto resultante al ejecutar la consulta SQL especificada  explícitamente.
La consulta especificada debe referirse al Objeto que se pasa por parámetro, y debe estar
completamente definida.

Ejemplo:
```
Person p = new Person();
admin.obtainSelect(p, "SELECT * FROM Person WHERE age=22");
```

### 6.2.11 ###
### obtainJoin ###
A través de este método es posible obtener un Array (Object[.md](.md)), donde cada elemento de este array sera a su vez un String con la cantidad de elementos igual a las columnas que se especifico en la búsqueda (es decir, este método devuelve una representación de la tabla resultante como un objeto matriz). Es de utilidad para completar los datos de un Componente Gráfico como una Tabla, etc.

Debe especificarse la consulta SQL completa:
```
Object[] objects = admin.obtainJoin("SELECT Person.name, Person.age FROM Person", 2);
```

### 6.2.12 ###
### lazyLoad ###
Mediante este método es posible ir cargando un Objeto de forma progresiva a medida que sea
necesario.

Por ejemplo, para las siguientes clases:
```
public class Person{
    private int id;
    private String name;
    private Phone phone;
    //Getters - Setters
}

public class Phone{
    private int id;
    private String number;
    private Company company;
    //Getters - Setters
}

public class Company{
    private int id;
    private String description;
    //Getters - Setters
}
```

Realizamos la carga solo de los atributos de Person(1), luego agregamos los valores de los atributos de Phone(2) y por ultimo los de Company(3):

```
Person p = new Person();

//Atributos de Person(1)
admin.lazyLoad(p, "age=22");
//Phone es NULL en este momento

//Agregamos los atributos de Phone
admin.lazyLoad(p);
//Phone ya tiene sus valores, salvo por company que es NULL

//Agrega los valores de Company
admin.lazyLoad(p);
```

### 6.2.13 ###
### executeQuery ###
“executeQuery” quizás sea el método mas simple, ya que lo único que hace es ejecutar una
sentencia en la Base de Datos y lo único que devuelve es Verdadero si se ejecuto con Éxito o Falso en caso contrario.

Ejemplo:
```
admin.executeQuery("DELETE FROM Person");
```

### 6.2.14 ###
### checkTableExist ###
Determina si existe una determinada Tabla (devuelve True), o si de lo contrario dicha Tabla no existe (devuelve False).

Ejemplo:
```
if(admin.checkTableExist("Person")){
    System.out.println("La Tabla Existe");
}else{
    System.out.println("La Tabla No Existe");
}
```

### 6.2.15 ###
### Transacciones ###
QuickDB por defecto maneja las transacciones a nivel del Objeto con el que se comienza la
operación, es decir, el Objeto, ya sea simple, compuesto de otros objetos, con colecciones, o con herencia, debe contemplar la operación solicitada (junto con todos los objetos que involucre) con éxito, de lo contrario se descartan todos los cambios que se realizaran durante la operación.
Pero también existe la posibilidad de establecer que todas las operaciones se ejecuten de forma independiente, sin importar lo que suceda con los Objetos relacionados, de la siguiente forma:

```
admin.setAutoCommit(true);
```

A su vez, es posible manejar las transacciones de manera explicita, para los casos donde se pretenda guardar cierta cantidad de Objetos o ninguno:

```
Example example = new Example();
Person person = new Person("leeloo", 22);

admin.openAtomicBlock();
admin.save(example);
admin.save(person);
admin.closeAtomicBlock();
```

Si se quisiera validar que todas las operaciones se realizaron con éxito o de lo contrario cancelar la transacción y volver la base al estado previo, se podría realizar de la siguiente forma:

```
Example example = new Example();
Person person = new Person("leeloo", 22);

boolean value = true;
admin.openAtomicBlock();
value &= admin.save(example);
value &= admin.save(person);
if(value){
    admin.closeAtomicBlock();
}else{
    admin.cancelAtomicBlock();
}
```