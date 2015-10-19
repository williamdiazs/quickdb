# 4: #
# Annotations #

## 4.1 ##
## Table ##
Table may be accompanied by a parameter or not, the Table parameter indicates to which table in the database is mapped this entity. By not including the parameter is simply assumed that the table has the same name as the class.

  * Directly specifying to which table is a Class referred:

```
import cat.quickdb.annotation.Table;

@Table("TableExample")
public class Example{
   ...
}
```

  * Taking the name of the Class as Table's name.:

```
import cat.quickdb.annotation.Table;

@Table
public class Example{
   ...
}
```



## 4.2 ##
## Parent ##
Parent indicates that this class inherits from another, and therefore the attributes of the parent class will be stored in another table (this notation does not include any attribute).

```
import cat.quickdb.annotation.Parent;
import cat.quickdb.annotation.Table;

@Parent
@Table
public class ExampleSon{
   ...
}
```

Using this notation, can be used a parent class that is not within the same package, which was a restriction on the work simply with the [Conventions](http://code.google.com/p/quickdb/wiki/Conventions#3:).



## 4.3 ##
## Column ##
Column annotations are used to define completely an attribute of the entity with its corresponding mapping in a table in the database.
The various parameters of the Column annotation, have all default values, so you only need to fill those, whom are necessary for a particular case.

  * **name:** Field Name in the Table of the database, if not specified bears the name of the attribute.
  * **type:** ndicates the data type of the attribute, which can be PRIMARYKEY, FOREINGKEY, COLLECTION, PRIMITIVE (if not specified it is assumed that the type is PRIMITIVE, which is one of the language primitives, also contemplating String and java.sql.Date).
  * **getter:** This parameter give the possibility to explicitly express, which will be the method who will return the value of that attribute (if not specified it is assumed that, if the attribute is named "attribute", getter method will be "getAttribute").
  * **setter:** The same as above but for setting methods.
  * **collectionClass:** When the attribute marked with this annotation is a collection, can be said explicitly what type of objects will contain this collection.
  * **ignore:** By setting as True, QuickDB will not consider this attribute in the persistence of the object.

All unspecified parameters, are set to default following the behavior of the [Conventions](http://code.google.com/p/quickdb/wiki/Conventions#3:).

If a class has a "name" attribute that represents a field with the same name in their respective table in the database, is type String (for example), and their respective get and set methods are "getName" and "setName"; then for Column annotation would be enough:

```
@Column
private String name;
```

Which would be the equivalent as leave the attribute without annotation, because it follows the [Conventions](http://code.google.com/p/quickdb/wiki/Conventions#3:).
If, however, the attribute mentioned above doesn't follow any of the conditions, could be indicate which field (in the database) will reference it, its type, and its get and set methods:

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

In the case of parameter "collectionClass"  of column, there are two ways to specify:
  * By placing only the name of the class whose objects form part of the collection, if it is within the same package that contains the Class who has the Collection attribute:

```
@Column(collectionClass="ExampleSon")
private ArrayList examples;
```

  * Or by specifying the full path (Package.Class) in case of it is in another package:

```
@Column(collectionClass="quickdb.example.ExampleSon")
private ArrayList examples;
```

For Collections of Primitive Data types (int, double, float, short, long, boolean, and String and Date too), annotations should be used and are specified as follows:

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

It's important to note that it is only necessary to specify those parameters that are necessary, if we wish to indicate only the type, we could write:

```
@Column(type=TYPES.PRIMARYKEY)
private int id;
```

The different **Types** that can be specified are:
  * **PRIMITIVE:** It refers to those types that are mapped directly to the data types supported by the database.
  * **PRIMARYKEY:** It refers to the primary key of the Table and it's important to specify it, as it indicates to the library that, in cases of insertions, not try to force the value of the field, and let the database handle it.
  * **FOREIGNKEY:** It refers to foreign keys, which are represented in the database with references to other tables, and in the entities with references to other objects, this way, when you have a Class with a reference to another object, wich should also be mapped in a table, it must be marked as type "FOREINGKEY".
  * **COLLECTION:** Specifies whether the attribute is library Collection, to handle it as a one-to-many or many-to-many relation. (creates the necessary Tables to carry out this function).



## 4.4 ##
## Column Definition ##
Column Definition sets the properties to the creation of the Table in the database, when try to make the first insertion if the table does not exist. The parameters passed are:

  * **type:** The data type with which will be create their respective column in the database. Default is VARCHAR.
  * **length:** Length of the Data Type. By default is no length specified to the types, except for VARCHAR who has assigned a predetermined length of 150.
  * **notNull:** Specifies whether the column can accept or not null values. By default is true.
  * **defaultValue:** The value by default of each field in the column. By default is an empty string.
  * **autoIncrement:** By default is false.
  * **unique:** Specifies whether the field's value type is unique. By default is false.
  * **primary:** Specifies whether the column is primary key type. By default is false.
  * **format:** Specifies the column format. By default is DEFAULT. (Only for MySQL)
  * **storage:** Sets the Storage type to use. By default is DEFAULT. (Only for MySQL)

Example:
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
## Validations ##
Validations allows automatic checks on the entity to determine if the object will be stored or modified in the database. If the Validation condition is not fulfilled the upgrade process would be suspended.

A validation can be carried out through the following conditions:

  * **conditionMatch:** Receives an array of strings with different regular expressions to evaluate.
  * **maxLength:** Specifies the maximum length that can have a field (including the value specified).
  * **numeric:** Makes numeric type checks on the field. Receive an array with a set of numerical values, being organized in this way: [condition, value].
  * **date:** Makes checks on an object of type date. Receives an array with a set of numerical values, being organized in this way: (part\_of\_date, condition, value).

As regular expressions for  "conditionMatch", Validation annotation has four armed expressions which you can choose, rather than create new ones, these are:

  * **conditionURL:** Evaluates the text entered in the field, as a well-formed URL expression.
  * **conditionMail:** Evaluates the text entered in the field, as a well-formed e-mail address.
  * **conditionSecurePassword:** Evaluates the text entered in the field, as secure password (taking into account capital letters, lowercase letters, numbers and special characters).
  * **conditionNoEmpty:** Evaluates that the variable does not contain an empty string.

For _"numeric"_ checks, conditions which can be chosen are:

  * EQUAL
  * LOWER
  * GREATER
  * EQUALORLOWER
  * EQUALORGREATER

For _“date”_ checks, parts of the date available are:

  * YEAR
  * MONTH
  * DAY

And the conditions are the same as those of _“numeric”_.

  1. Complete example using all available validations.
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

  1. Example of numerical validations.
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

  1. Example of date validations.
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
## Mixed Mode ##
It's possible to work with Classes that specifies some attributes with Annotations, and others without (the latter being those who follow [Conventions](http://code.google.com/p/quickdb/wiki/Conventions#3:)). You only have to consider placing the @Table annotation to the level of Class (As mentioned in section [Restrictions](http://code.google.com/p/quickdb/wiki/Restricciones#2:)).



## 4.7 ##
## Data Tipes ##
Data Tipes that can be defined with the annotation ColumnDefinition are:
![http://quickdb.googlecode.com/files/datatype2.png](http://quickdb.googlecode.com/files/datatype2.png)