# 9: #
# Queries #
QuickDB implements two systems of Queries, which makes much easier to obtain an specific object, since he promotes the query creation and the specification of the conditions from a totally objects orientated perspective.

## 9.1 ##
## StringQuery ##
StringQuery is one of QuickDB's query systems and it's with which it interacts when the method "obtain", of AdminBase, is invoked passing as parameter the object on which the query applies, and a String containing the query.
The use of the String is to make the query based only on the object's properties and make the desired comparisons referring only to the attributes of the object (as if they were public).

Example:

Based on the following classes:

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

To get the Employee Object where the inherited attribute Address has as street  "unnamed street" should only be done:

```
Employee e = new Employee();

admin.obtain(e, "address.street = 'unnamed street'");
```

Which would be equivalent to the SQL query:

```
SELECT Employee.id, Employee.code, Employee.rolDescription, Employee.parent_id
FROM Employee
JOIN Person ON Employee.parent_id = Person.id
JOIN Address ON Person.address = Address.id
WHERE Address.street = 'unnamed street' 
```

This way is specified in the string the attributes of the object of which you want to make the query, taking into account that starts from the level of the class of object that is passed by parameter, and consulting the inherited attributes (of any level, ie the direct Parent of the Class, or the Parent of Parent of the Class) as their own.
As can be seen in the example above, "address" is a reference to another object inherited from the parent class, which contains the attribute "street". Therefore, is placed "address.street", where "address" is the name of the attribute within the class "Person", which the queries system interprets as inherited by the class "Employee", and then asks for the attribute "street" Class "Address" by separating the instance and the attribute by a dot (.) as referring to public attributes.

StringQuery supports the following operators:

  * AND, &&, &
  * OR, ||, |
  * =
  * !
  * <
  * >
  * LIKE, like

StringQuery greatly helps to create very complex queries in a easy way, but also presents some limitations as date operations, few operators, and having to specify the whole sentence in a String, thats why was created the system queries that is explained below.

## 9.2 ##
## QuickDB Query ##
This queries system carries out the creation of the query by invoking methods of classes "Query", "Where" and "DateQuery" (in case of operations with Dates), which has the advantage of promoting well-formed queries creations and minimize the work, as it is responsible for establishing the JOINs between tables wh.en necessary, etc.
This queries system is used when invoke the "Obtain" method, passing as the only parameter the object on which we want to perform the operation.

### 9.2.1 ###
### Class Query ###
Where class provides us the following methods to making the query:

  * **where(String field, {Class baseClass}):** Which initiates the query. "Field" is the name of the field on whom you want to perform a certain verification, and the attribute "baseClass" is optional, if not specified, it is assumed that the attribute belongs to the class of object passed to "Obtain" as a parameter or to any of its parent class, otherwise, must be specified the Class to which the attribute belongs.

All conditions stated in braces {} are optional.

  * **and(FIELD, {CLASS}):** Concatenates the previous verification with the verification who starts with this new attribute (FIELD) through "AND".
  * **or(FIELD, CLASS...):** Concatenates the previous verification with the verification who starts with this new attribute (FIELD) through “OR”.
  * **group(FIELDS, {CLASS}):** Specifies by which fields (FIELDS) will be group the query, by placing them within a string (String) separated each by a comma (,). For each field within the string must specify the class to which belong them, unless they all belong to the base class directly or by inheritance.
  * **whereGroup(FIELD, {CLASS}):** Corresponds to the SQL's "HAVING", and specifies a condition to those fields by which the query was grouped (requires to have applied a previously group with "group(...)").
  * **sort(BOOLEAN, FIELDS, CLASS...):** Sort the results of the query based on the fields specified in the string (Following the same methodology previously expressed of to separate each one by a comma (,)). For each field within the string must specify the class to which belong them, unless they all belong to the base class directly or by inheritance. The expressed boolean value determines the sort type, ascending if TRUE, and descending if FALSE.

### 9.2.2 ###
### Class Where ###
Where class provides us the following methods to making the query:

The following methods can receive a parameter by **Value** to perform the test, or a **String** with the name of a field and the **Class** to which it belongs to build the validation for the query.

  * **equal(OBJECT, {OBJECT})**
  * **greater(OBJECT, {OBJECT})**
  * **lower(OBJECT, {OBJECT})**
  * **equalORgreater(OBJECT, {OBJECT})**
  * **equalORlower(OBJECT, {OBJECT})**
  * **notEqual(OBJECT, {OBJECT})**

The following methods doesn't receive any parameter, but they specify a condition to the previously specified attribute:

  * **not()**
  * **isNull()**
  * **isNotNull()**

Other methods:

  * **between(OBJECT1, OBJECT2):** This method can receive any parameter of primitive object type, or some other object that implements the method "toString()" in a coherent way, to see if the specified attribute value prior to the call of this operation is within the range that exists between these two values (both values must be of the same data type).
  * **in({OBJECTS}):** This method receives a set of objects with which validate if the attribute specified prior to the call of this function, corresponds to any of the values received in this function by parameter.
  * **match(EXPRESSION):** This method receives by parameter a string and evaluates if the attribute specified prior to the call of this function contains the received string.If received a simple string, will verify if the attribute corresponds to a string like "EXPRESSION" (being  any kind of string), on the contrary, if in the EXPRESSION is used any of the "%" characters (which represents any kind of characters) or “_” (representing a character any), will validate by the exact EXPRESSION received without altering the beginning and end.
  * **date():** With this method, is specified that the attribute previously entered corresponds to a Date attribute type, so returns a reference to "DateQuery" to carry out the necessary date checks._

### 9.2.3 ###
### Class DateQuery ###
DateQuery class provides us the following methods to making the query:

  * **differenceWith(VALUE, {CLASS}):** This method determines the difference in days between the attribute previously expressed and the value passed by parameter, or another field of a table in the database (if specify the field and the Class).
  * **month():** Returns the month value of the attribute previously entered.
  * **day():** Returns the day value of the attribute previously entered.
  * **year():** Returns the year value of the attribute previously entered.

Then these returned values must be compared using one of the methods of the class "Where."

### 9.2.4 ###
### Searching ###
Once  concatenated the methods to form the query, just remains the "execution" of the same, and there are two ways to do it, by adding as the last method in the concatenation:

  * **find():** which completes with the resulting data the object passed as parameter to "Obtain".
  * **findAll():** which returns a collection with the objects resulting from executing the query.

Exapmles:

Based on the following data model:

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

**Queries:**

  * Simple Query:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("name").equal("son name").find();
```

  * Simple Query of Inherited Attributes:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("description").equal("parent description2").find();
```

  * Query based on the attribute's value of a reference:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("valueParent", ReferenceQuery.class).equal("son value").find();
```

  * Query based on the inherited attribute's value of a reference:
```
UserQuery user = new UserQuery();
admin.obtain(user).where("valueParent", ReferenceQuery.class).equal("value Parent").find();
```

  * Query using “between”, “and” and “lower”
```
ArrayList array = admin.obtain(user).where("birth").between("1980-01-01", "2010-12-31").and("salary").lower(2000).findAll();
```

  * Query using “in”, “or” and “match”
```
ArrayList array = admin.obtain(user).where("age").in(22, 23, 24, 26).or("name").match("Sarmentero").findAll();
```

  * Query using “group”, “whereGroup” and “greater”
```
ArrayList array = admin.obtain(user).where("age").greater(10).group("salary").whereGroup("salary").greater(2000).findAll();
```

  * Query using “differenceWidth”, “equal”
```
java.sql.Date date = new Date(104, 4, 22);
ArrayList array = admin.obtain(user).where("birth").date().differenceWith(date.toString()).equal(2).findAll();
```