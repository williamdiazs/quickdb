# 9: #
# Consultas #
QuickDB implementa 2 sistemas de Consultas, los cuales facilitan mucho la tarea de obtener determinado objeto, ya que promueve la creación de las consultas y especificación de las condiciones desde una perspectiva totalmente orientada a objetos.

## 9.1 ##
## StringQuery ##
StringQuery es uno de los sistemas de consultas de QuickDB y es con el que se interactúa cuando se invoca al metodo “obtain” de AdminBase y se pasa como parámetro el Objeto sobre el que se aplica la consulta, y un String conteniendo la consulta.
El uso del String consiste en realizar la consulta basándose únicamente en las propiedades del Objeto y realizar las comparaciones deseadas refiriéndose únicamente a los atributos del objeto (como si estos fueran públicos).

Ejemplo:

Basándose en las siguientes clases:

```
public class Person{
    private int id;
    private String name;
    private java.sql.Date birth;
    private ArrayList<Phone> phone;
    private Address address;
    //Getters - Setters
}

public class Employee extends Person{
    private int id;
    private int code;
    private String rolDescription;
    //Getters - Setters
}

public class Address{
    private int id;
    private String street;
    private int number;
    //Getters - Setters
}

public class Phone{
    private int id;
    private String areaCode;
    private String number;
    //Getters - Setters
}
```

Para obtener el Objeto Employee donde el atributo heredado Address tenga como calle "unnamed street" solo debe hacerse:

```
Employee e = new Employee();

admin.obtain(e, "address.street = 'unnamed street'");
```

Lo que equivaldría al hacerlo con SQL a:

```
SELECT Employee.id, Employee.code, Employee.rolDescription, Employee.parent_id
FROM Employee
JOIN Person ON Employee.parent_id = Person.id
JOIN Address ON Person.address = Address.id
WHERE Address.street = 'unnamed street' 
```

De esta forma se especifica en la cadena los atributos del objeto por los que se quiere realizar la consulta, teniendo en cuenta que se parte desde el nivel de la clase del objeto que se pasa por parámetro, y consultando los atributos heredados (sean del nivel que sean, es decir del Padre directo de la Clase, o del Padre del Padre de la Clase) como si fueran propios.
Como se puede ver en el Ejemplo expuesto, “address” es una referencia a otro Objeto heredada desde la Clase Padre, que contiene el atributo “street”, es por ello que se coloca “address.street”, siendo “address” el nombre del atributo dentro de la Clase “Person”, el cual para el sistema de consultas se interpreta como que es heredado por la Clase “Employee”, y luego se pregunta por el atributo “street” de la Clase “Address” separando la instancia y el atributo por un punto (.) como si se refiriera a atributos públicos.

StringQuery soporta los siguientes operadores:

  * AND, &&, &
  * OR, ||, |
  * =
  * !
  * <
  * >
  * LIKE, like

StringQuery ayuda de gran manera a crear consultas complejas de forma muy fácil, pero a su vez presenta ciertas limitaciones en cuanto al manejo de fechas, pocos operadores, y tener que especificar toda la sentencia en un String, por lo cual se creo el sistema de consultas que se explicara a continuación.

## 9.2 ##
## QuickDB Query ##
Este sistema de Consultas realiza la creación de la consulta a través de la invocación de métodos de las Clases “Query”, “Where” y “DateQuery” (para el caso de operaciones con Fechas), lo cual tiene la ventaja de promover la creación de consultas bien formadas, y minimizar el trabajo, ya que se encarga de establecer los JOINs entre las tablas que sean necesarias, etc.
Este sistema de consultas es el que se utiliza al invocar el método “obtain” pasando solo como parámetro el objeto sobre el que queremos realizar la operación.

### 9.2.1 ###
### Clase Query ###
La Clase Query nos brinda los siguientes métodos con los que ir armando la consulta:

  * **where(String field, {Class claseBase}):** El cual comienza la consulta. “Field” corresponde al nombre del campo por el que se desea realizar determinada comprobación, y el atributo “classBase” es opcional, si no se especifica, se asume que el atributo pertenece a la Clase del Objeto que se paso a “obtain” como parámetro o a alguna de sus clases Padre, de lo contrario, debe especificarse la Clase a la que pertenece dicho atributo.

Todas las condiciones expresadas entre corchetes {} son opcionales.

  * **and(FIELD, {CLASS}):** Concatena la comprobación realizada previamente junto con la comprobación que se comienza con este nuevo atributo (FIELD) mediante “AND”.
  * **or(FIELD, CLASS...):** Concatena la comprobación realizada previamente junto con la comprobación que se comienza con este nuevo atributo (FIELD) mediante “OR”.
  * **group(FIELDS, {CLASS}):** Se especifican por cuales campos (FIELDS) se desea agrupar la consulta, colocando los mismos dentro de una cadena (String) separado cada uno por una coma (,). Por cada campo dentro de la cadena se deberá especificar la Clase a los que pertenecen los mismos, a no ser que todos pertenezcan a la clase base de forma directa o por herencia.
  * **whereGroup(FIELD, {CLASS}):** Corresponde al “HAVING” de SQL, y especifica una condición para los campos por los que fue agrupada la consulta (requiere haber aplicado previamente una agrupación con “group(...)”).
  * **sort(BOOLEAN, FIELDS, CLASS...):** Ordena el resultado de la Consulta en base a los campos especificados en la cadena (siguiendo la misma metodología expresada previamente de separar cada uno por una coma(,)). Para cada uno de los campos se debera especificar la Clase a la que pertenecen, a no ser que todos pertenezcan a la clase base de forma directa o por herencia. El valor booleano expresado determinara el tipo de ordenamiento, siendo TRUE de forma ascendente, y FALSE de forma descendente.

### 9.2.2 ###
### Clase Where ###
La Clase Where brinda los siguientes métodos con los que ir armando la consulta:

Los siguientes métodos pueden recibir por parámetro un **Valor** concreto con el que realizar la comprobación, o una **Cadena** con el nombre de un campo y la **Clase** a la que pertenece dicho campo para armar la validación correspondiente en la consulta.

  * **equal(OBJECT, {OBJECT})**
  * **greater(OBJECT, {OBJECT})**
  * **lower(OBJECT, {OBJECT})**
  * **equalORgreater(OBJECT, {OBJECT})**
  * **equalORlower(OBJECT, {OBJECT})**
  * **notEqual(OBJECT, {OBJECT})**

Los siguientes métodos no reciben ningún tipo de parámetro, sino que especifican una condición para el atributo especificado previamente:

  * **not()**
  * **isNull()**
  * **isNotNull()**

Otros métodos:

  * **between(OBJECT1, OBJECT2):** Este método puede recibir por parámetro cualquier objeto de tipo primitivo, o algún otro objeto que implemente el método “toString()” de manera coherente para consultar si el valor del atributo especificado previo a la llamada de esta operación se encuentra dentro del intervalo que existe entre estos 2 valores (ambos valores deben ser del mismo tipo de dato).
  * **in({OBJECTS}):** Este método recibe un conjunto de objetos, con los que validara si el atributo especificado previo a la llamada de esta función, corresponde a alguno de los valores recibidos en esta función por parámetro.
  * **match(EXPRESION):** Este método recibe una Cadena por parámetro y evalúa si el atributo especificado previo a la llamada de esta función contiene a la cadena recibida. Si se recibe una cadena simple, se comprobara si el atributo corresponde a una cadena del tipo: “EXPRESION” (siendo  cualquier tipo de cadena), por el contrario si en la EXPRESION se utiliza alguno de los caracteres “%” (que representa cualquier conjunto de caracteres) o `“_”` (que representa un carácter cualquiera), se validará por la EXPRESION exacta recibida sin alterar el principio y el final.
  * **date():** Con este método, se especifica que el atributo ingresado previamente corresponde a un atributo del tipo Fecha, por lo que devuelve una referencia a “DateQuery” para poder realizar las comprobaciones de Fecha que sean necesarias.

### 9.2.3 ###
### Clase DateQuery ###
La Clase DateQuery brinda los siguientes métodos con los que ir armando la consulta:

  * **differenceWith(VALUE, {CLASS}):** Este método permite determinar la diferencia en días entre el atributo expresado previamente y el valor que se pasa por parámetro u otro campo de alguna tabla de la Base de Datos (si se especifica el Campo y la Clase).
  * **month():** Devuelve el valor del mes del atributo ingresado previamente.
  * **day():** Devuelve el valor del día del atributo ingresado previamente.
  * **year():** Devuelve el valor del año del atributo ingresado previamente.

Luego estos valores devueltos deben ser comparados utilizando alguno de los métodos de la Clase “Where”.

### 9.2.4 ###
### Realizando la Busqueda ###
Una vez concatenados los métodos para formar la consulta, solo queda la “ejecución” de la misma, y existen 2 métodos para realizarlo, agregando como ultimo método en la concatenación:

  * **find():** el cual completa con los datos resultantes el objeto pasado por parámetro a "obtain”.
  * **findAll():** el cual retorna una colección con los objetos resultantes de ejecutar dicha consulta.

Ejemplos:

En base al siguiente Modelo de Datos:

```
public class UserParent{
    private int id;
    private String description;
    private ReferenceQuery reference;
}

public class UserQuery extends UserParent{
    private int id;
    private String name;
}

public class ReferenceParent{
    private int id;
    private String valueParent;
}

public class ReferenceQuery extends ReferenceParent{
    private int id;
    private String value;
}

public class CompleteQuery{
    private int id;
    private String name;
    private double salary;
    private int age;
    private Date birth;
    private boolean cond;
}
```

**Consultas:**

  * Consulta Simple:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("name").equal("son name").find();
```

  * Consulta Simple de Atributo Heredado:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("description").equal("parent description2").find();
```

  * Consulta en base al Valor del atributo de una Referencia:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("valueParent", ReferenceQuery.class).equal("son value").find();
```

  * Consulta en base al Valor del atributo heredado de una Referencia:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("valueParent", ReferenceQuery.class).equal("value Parent").find();
```

  * Consulta utilizando “between”, “and” y “lower”
```
ArrayList array = admin.obtain(user).where("birth").between("1980-01-01", "2010-12-31").and("salary").lower(2000).findAll();
```

  * Consulta utilizando “in”, “or” y “match”
```
ArrayList array = admin.obtain(user).where("age").in(22, 23, 24, 26).or("name").match("Sarmentero").findAll();
```

  * Consulta utilizando “group”, “whereGroup” y “greater”
```
ArrayList array = admin.obtain(user).where("age").greater(10).group("salary").whereGroup("salary").greater(2000).findAll();
```

  * Consulta utilizando “differenceWidth”, “equal”
```
java.sql.Date date = new Date(104, 4, 22);
ArrayList array = admin.obtain(user).where("birth").date().differenceWith(date.toString()).equal(2).findAll();
```