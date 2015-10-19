# 3: #
# Conventions #
Conventions are used to remove the configuration tasks within QuickDB. There are many properties that can be deducted automatically without any configuration (although, if desired, anything can be configured as will be seen in the next section Annotations). Specified below are details to take into account in the Data Model, to be supported by QuickDB without the need for more than writing Entities. These conventions are necessary when not working with annotations, otherwise, the Entities can be written in any other way (provided that such characteristics are specified by the annotations).


## 3.1 ##
## For Primary Keys ##
Each class in the model, has to declare, as a first attribute, an integer variable named "id":

```
public class Person{
   private int id;
   ...
}
```


## 3.2 ##
## Inheritance ##
To QuickDB being able to recognize automatically the inheritance, both Parent and Son, must be in the same package. This makes possible to determine when to stop analyzing the inheritance recursively, and not reach the level of Object.

```
package com.example;

public class Parent{
   ...
}

package com.example;

public class Son extends Parent{
   ...
}
```


## 3.3 ##
## Collections ##
QuickDB supports only those collecctions that implements "java.util.List" or "java.util.Collection" interfaces, and when you work without annotation, the attribute name must match the name of the class of objects that make up this collection (with the first letter in lowercase if desired).

```
public class Person{
   private ArrayList phone;
}

public class Phone{
   ...
}
```

For collections that contain only primitive data types, see into "Annotation" section where it's explained how contemplate this case.


## 3.4 ##
## Getters y Setters ##
When working without annotations, QuickDB should automatically determine which methods will set and return the value of each attribute.To do so, we follow the convention by default of write "set" or "get" and then the name of the attribute with CamelCase:

```
public class Person{

   private int id;
   private String name;

   public void setId(int id){...}
   public int getId(){...}
   public void setName(String name){...}
   public String getName(){...}
}
```


## 3.5 ##
## Data Types ##
![http://quickdb.googlecode.com/files/dataType01.png](http://quickdb.googlecode.com/files/dataType01.png)