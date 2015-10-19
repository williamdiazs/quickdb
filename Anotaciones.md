# 4: #
# Anotaciones #

## 4.1 ##
## Tabla ##
Table puede estar acompañada de un parámetro o no, el parámetro de Table indica a que tabla de la Base de Datos se mapeara esta entidad. Al no incluir el parámetro simplemente se asume que la Tabla lleva el mismo nombre que la Clase.

  * Especificando directamente a cual Tabla hace referencia una Clase:

```
import cat.quickdb.annotation.Table;

@Table("TableExample")
public class Example{
   ...
}
```

  * Tomando como nombre de la Tabla el nombre de la Clase:

```
import cat.quickdb.annotation.Table;

@Table
public class Example{
   ...
}
```


## 4.2 ##
## Padre ##
Parent indica que esta Clase hereda de otra y que por lo tanto los atributos de la Clase Padre se guardaran en otra tabla (esta anotación no lleva ninguna atributo).

```
import cat.quickdb.annotation.Parent;
import cat.quickdb.annotation.Table;

@Parent
@Table
public class ExampleSon{
   ...
}
```

Utilizando esta anotación se puede utilizar Clases Padre que no estén dentro del mismo paquete, lo cual era una restricción al trabajar simplemente con las Convenciones.

## 4.3 ##
## Columna ##
En cuanto a las anotaciones Column, estas se utilizan para definir de forma completa un atributo de la entidad con su correspondiente mapeo dentro de una Tabla en la Base de Datos.
Los distintos parámetros de la Anotación Column, poseen todos valores por defecto, por lo que solo es necesario completar aquellos que sean necesarios para determinado caso particular.

  * **name:** Nombre del Campo de dicha Tabla en la Base de Datos, si no se especifica toma el mismo nombre del atributo.
  * **type:** Indica el tipo de dato del atributo, el cual puede ser PRIMARYKEY, FOREIGNKEY,COLLECTION, PRIMITIVE (si no se especifica se asume que el tipo es PRIMITIVE, el cual es una de las primitivas del lenguaje contemplando además String y java.sql.Date).
  * **getter:** Este parámetro presenta la posibilidad de poder expresar explícitamente cual sera el método del cual obtendremos el valor de dicho atributo (si no se especifica se asume que si el atributo lleva el nombre "atributo" el método getter sera "getAtributo").
  * **setter:** El mismo caso anterior pero para los métodos de seteo.
  * **collectionClass:** Cuando el atributo marcado con esta anotación es una colección se puede decir explícitamente que tipo de objetos contendrá esta colección.
  * **ignore:** Colocándole como valor True le decimos a QuickDB que no tenga en cuenta este atributo al momento de persistir el objeto.

Todos los parámetros no especificados siguen por defecto el comportamiento establecido en las Convenciones.

Si una clase tuviera un atributo "name" y ese atributo representara un campo con el mismo nombre en su respectiva Tabla en la Base de Datos, fuera de tipo String (por ejemplo), y sus respectivos métodos de get y set fueran "getName" y "setName", entonces para la Anotación de Column bastaría con que se colocara:

```
@Column
private String name;
```

Lo cual seria el equivalente a dejar dicho atributo sin anotación ya que sigue las convenciones.
Si por el contrario, el atributo antes mencionado no siguiera ninguna de las condiciones establecidas, podríamos indicar cual es el nombre del Campo en la Base de Datos al que hace referencia, su tipo, y sus métodos de get y set:

```
import cat.quickdb.annotation.Column;
import cat.quickdb.annotation.Properties.TYPES;
import cat.quickdb.annotation.Table;

@Table("TableExample")
public class Example{
   
   @Column(name="userName", type=TYPES.PRIMITIVE, getter="readName", setter="writeName")
   private String name;
}
```

Para el caso del parámetro “collectionClass” de Column, hay 2 formas de especificarlo:
  * Colocando solo el nombre de la Clase cuyos objetos formaran parte de la colección, si esta misma se encuentra dentro del mismo paquete de la Clase que contiene el atributo de Colección:

```
@Column(collectionClass="ExampleSon")
private ArrayList examples;
```

  * O especificando la ruta completa (Paquete.Clase) en el caso que se encontrara en otro Paquete:

```
@Column(collectionClass="quickdb.example.ExampleSon")
private ArrayList examples;
```

Para las Colecciones de tipos de Datos Primitivos (int, double, float, short, long, boolean, y tambien String y Date), es necesario utilizar anotaciones, y se especifican de la siguiente forma:

```
@Column(collectionClass="java.lang.Boolean")
private ArrayList booleans;

@Column(collectionClass="java.sql.Date")
private ArrayList dates;

@Column(collectionClass="java.lang.Double")
private ArrayList doubles;

@Column(collectionClass="java.lang.Float")
private ArrayList floats;

@Column(collectionClass="java.lang.Integer")
private ArrayList integers;

@Column(collectionClass="java.lang.String")
private ArrayList strings;
```

Es importante tener en cuenta que solo es necesario especificar aquellos parámetros que sean necesarios, si solo quisiéramos indicar el tipo podríamos poner:

```
@Column(type=TYPES.PRIMARYKEY)
private int id;
```

Los distintos **Tipos** que pueden especificarse son:
  * **PRIMITIVE:** Se refiere a aquellos tipos que son mapeados directamente a los Tipos de Datos soportados por la Base de Datos.
  * **PRIMARYKEY:** Hace referencia a la clave primaria de dicha Tabla y es de importancia especificarla, ya que indica a la librería que en casos de inserciones no se intente forzar el valor de este campo y se deje a la Base de Datos manejarlo.
  * **FOREIGNKEY:** Hace referencia a las claves foráneas, las cuales son representadas en la Base de Datos por referencias a otras Tablas, y en las Entidades por referencias a otros objetos, de esta forma cuando se tiene en una Clase una referencia a otro objeto que también debe ser mapeado a una Tabla, este debe ser marcado como tipo "FOREIGNKEY".
  * **COLLECTION:** Especifica si dicho atributo es del tipo colección para trabajarlo como una relación uno-a-muchos o muchos-a-muchos (crea las Tablas que fueran necesarias para llevar a cabo esta función).

## 4.4 ##
## Definición de Columna ##
ColumnDefinition establece las propiedades con las que se creara la Tabla en la Base de Datos al tratar de hacer la primera inserción si la Tabla no existe. Los parámetros que recibe son:

  * **type:** El tipo de dato con el que se creara dicha columna en la Base de Datos. Por defecto el tipo es VARCHAR.
  * **length:** Largo del tipo de Dato. Por defecto no especifica largo a los tipos, salvo a VARCHAR que le asigna un largo predeterminado de 150.
  * **notNull:** Especifica si la columna puede aceptar valores nulos o no. Por defecto su valor es true.
  * **defaultValue:** El valor que tiene cada campo de la columna por defecto. Por defecto su valor es una cadena vacía.
  * **autoIncrement:** Valor por defecto false.
  * **unique:** Estable si el valor del campo es de tipo único. Valor por defecto false.
  * **primary:** Estable si la columna es de tipo clave primaria. Valor por defecto false.
  * **format:** Estable el formato de la columna. Por defecto DEFAULT. (Solo para MySQL)
  * **storage:** Establece el tipo de Storage a utilizar. Por defecto DEFAUL. (Solo para MySQL)

Ejemplo:
```
import cat.quickdb.annotation.Column;
import cat.quickdb.annotation.ColumnDefinition;
import cat.quickdb.annotation.Definition;
import cat.quickdb.annotation.Properties.TYPES;
import cat.quickdb.annotation.Table;

@Table
public class Person{

   @Column(type=TYPES.PRIMARYKEY)
   @ColumnDefinition(type=Definition.DATATYPE.INT, length=11, autoIncrement=true, primary=true)
   private int id;
   
   @Column
   @ColumnDefinition
   private String name;
}
```


## 4.5 ##
## Validaciones ##
Las validaciones permiten realizar comprobaciones automáticas sobre la Entidad para determinar si dicho Objeto sera guardado o modificado en la Base de Datos. Si la condición de Validación no se cumpliera el proceso de actualización se suspendería.

Una Validación se puede llevar a cabo a través de las siguientes condiciones:

  * **conditionMatch:** Recibe un array de Strings con las distintas expresiones regulares a evaluar.
  * **maxLength:** Especifica el largo máximo que puede tener cierto campo (incluido el valor especificado).
  * **numeric:** Realiza comprobaciones de tipos numericas sobre el campo. Recibe un array con un conjunto de valores numericos, estando organizados de esta forma: [condición, valor].
  * **date:** Realiza comprobaciones sobre un objeto de tipo fecha. Recibe un array con un conjunto de valores numericos, estando organizados de esta forma: (parte\_de\_fecha, condición, valor).

En cuanto a las expresiones regulares para “conditionMatch”, la anotación Validation cuenta con 4 expresiones ya armadas de las que se puede elegir, en lugar de crear nuevas, estas son:

  * **conditionURL:** Evalúa que el texto ingresado en dicho campo sea una expresión URL bien formada.
  * **conditionMail:** Evalúa que el texto ingresado en dicho campo sea un e-mail bien formado.
  * **conditionSecurePassword:** Evalúa que el texto ingresado en dicha variable sea un password seguro (contemplando mayúsculas, minúsculas, números y caracteres especiales).
  * **conditionNoEmpty:** Evalúa que la variable no contenga una cadena vacía.

Para las comprobaciones de _“numeric”_ las condiciones de las que se puede elegir son:

  * EQUAL
  * LOWER
  * GREATER
  * EQUALORLOWER
  * EQUALORGREATER

Para las comprobaciones de _“date”_ las partes de la fecha disponible son:

  * YEAR
  * MONTH
  * DAY

Y las condiciones son las mismas que se utilizan en _“numeric”_.

  1. Ejemplo completo utilizando todas las validaciones disponibles.
```
import cat.quickdb.annotation.Validation;
import java.sql.Date;

public class ValidUser{

    private int id;
    @Validation(maxLength=20, conditionMatch={Validation.conditionNoEmpty})
    private String name;
    @Validation(conditionMatch={Validation.conditionSecurePassword})
    private String pass;
    @Validation(conditionMatch={Validation.conditionMail})
    private String mail;
    @Validation(conditionMatch={Validation.conditionURL})
    private String url;
    @Validation(date={Validation.YEAR, Validation.EQUALORGREATER, 1988})
    private Date birthDate;
    @Validation(numeric={Validation.GREATER, 18})
    private int age;
    
}
```

  1. Ejemplo de validaciones numéricas.
```
import cat.quickdb.annotation.Validation;

public class ValidComplexNumeric{

    private int id;
    @Validation(numeric={Validation.EQUAL, 5})
    private String number1;
    @Validation(numeric={Validation.EQUALORGREATER, 2, Validation.EQUALORLOWER, 9})
    private int number2;
    @Validation(numeric={Validation.GREATER, 1, Validation.LOWER, 3})
    private int number3;    
    
}
```

  1. Ejemplo de validaciones de fecha.
```
import cat.quickdb.annotation.Validation;
import java.sql.Date

public class ValidComplexDate{

    private int id;
    @Validation(date={Validation.DAY, Validation.GREATER, 5,
        Validation.MONTH, Validation.EQUAL, 5,
        Validation.YEAR, Validation.EQUALORLOWER, 2009})
    private Date date;

}
```

## 4.6 ##
## Modo Mixto ##
Es posible trabajar con Clases donde se especifiquen algunos atributos con Anotaciones y otros sin Anotaciones (siendo estos últimos los que siguen las convenciones), para lo cual solo hay que tener en cuenta de colocar la anotación @Table al nivel de la Clase (como se mencionó en la sección de “Restricciones”).


## 4.7 ##
## Tipos de Datos ##
Los tipos de Datos que pueden ser definidos con la anotación ColumnDefinition son:
![http://quickdb.googlecode.com/files/tipoDato2.png](http://quickdb.googlecode.com/files/tipoDato2.png)