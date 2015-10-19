# 6: #
# AdminBase #
AdminBase is the main class of the Library and with which all the operations related to the database are perform. An instance of AdminBase should be created to make use of the interaction between the Database and the Data Model.

## 6.1 ##
By creating the instance, AdminBase must received certain parameters with which will attempt to create the connection with the Database:

AdminBase Parameters are:
  * **Host**
  * **Port**
  * **Database Name**
  * **User**
  * **Password**
  * **Schema** (Solo para Postgre)

**Examples:**

  * For MySQL:
```
AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL,
        "localhost", "3306", "testQuickDB", "root", "pass");
```

  * For Postgre:
```
AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.POSTGRES,
        "localhost", "5432", "testQuickDB", "postgres", "postgres", "testing");
```

  * For Firebird:
```
AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.FIREBIRD,
        "localhost", "employees.gdb", "SYSDBA", "firebird");
```

  * For SQLite:
```
AdminBase admin = AdminBase.initialize(
        AdminBase.DATABASE.SQLite, "test");
```

## 6.2 ##
## Operations ##
The operations represents the actions that can be performed between the Object Data Model and the Database. To explain each of the operations is taken as an example the following class:

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
To save an object in the database, it is necessary to create an instance of it and then invoke the method "save" which is the responsable to execute that task.

Example:
```
Person person = new Person("leeloo", 22);
admin.save(person);
```

### 6.2.2 ###
### saveAll ###
To save a collection of objects in the database, it is necessary to create the instantiate the collection including the objects from the Data Model and then invoke the method "saveAll" which is the responsable to execute that task.

Example:
```
ArrayList array = new ArrayList();
array.add(add new Person("name1", 20));
array.add(add new Person("name2", 20));
array.add(add new Person("name3", 32));

admin.saveAll(array);
```

### 6.2.3 ###
### saveGetIndex ###
To save an object in the database and retrieve the Key generated for that Object, it is necessary to create an instance of it and then invoke the method "saveGetIndex" which is the responsable to execute that task.

Example:
```
Person person = new Person("leeloo", 22);
admin.saveGetIndex(person);
```

### 6.2.4 ###
### modify ###
To modify an object in the database, it is necessary to retrieve the Object first from the Database, apply the proper modifications and then invoke the method "modify" which is the responsable to execute that task.

Example:
```
Person p = new Person();
admin.obtain(p, "age=22");

p.setName("Leonardo");
admin.modify(p);
```

### 6.2.5 ###
### modifyAll ###
It is the same case as "modify", you get first the collection desired, modify the values of that collection and execute "modifyAll."

Example:
```
Person p = new Person();
arrayList array = admin.obtainAll(p, "age=22");

for(Object o : array){
   //Modify the Object
}

admin.modifyAll(array);
```

### 6.2.6 ###
### delete ###
Retrieve the Object to be erase and then execute "delete".

Example:
```
Person p = new Person();
admin.obtain(p, "age=22");

admin.delete(p);
```

### 6.2.7 ###
### obtain ###
The "obtain" method is overloaded, because is possible to work with any of the two QuickDB query systems (which are explained in another section.) Therefore "Obtain" can receive as parameter the object from which the data is desired, this being the QuickDB system called "Query":

```
Person p = new Person();
admin.obtain(p).where("street", Address.class).equal("unnamed street").find();
```

Or "obtain" can receive two parameters, the first one is the object from which the data is desired, and the second a String specifying the search conditions based on the characteristics of the second QuickDB Query System called "StringQuery":

```
Person p = new Person();
admin.obtain(p, "address.street = 'unnamed street'");
```

### 6.2.8 ###
### obtainAll ###
To obtain from the Database a collection of Objects, is only necessary to create an empty instance of the particular object and then execute the method "obtainAll" from AdminBase to retrieve all the objects that satisfied some condition.

Example:
```
Person person = new Person();
ArrayList array = admin.obtainAll(person, "age=22");
```
Or:
```
ArrayList array = admin.obtainAll(Person.class, "age=22");
```

It is also possible to perform the search using the QuickDB Query System as will be mention in the Query Section.

### 6.2.9 ###
### obtainWhere ###
This method allows the developer to specify only the WHERE section in SQL query and will return the resulting object.
Is faster than QuickDB Query System because not need to infer the structure of the object to perform the JOINs relevant to carry out the query when other objects are involve, but this only allow simple queries on the same object.

Example:
```
Person p = new Person();
admin.obtainWhere(p, "age=22");
```

### 6.2.10 ###
### obtainSelect ###
This method returns the resulting object of execute the SQL query specified explicitly.
The specified query must refer to the object that is passed by parameter and must be fully defined.

Example:
```
Person p = new Person();
admin.obtainSelect(p, "SELECT * FROM Person WHERE age=22");
```

### 6.2.11 ###
### obtainJoin ###
Through this method it is possible to obtain an Array (Object), where each element of this array will turn to be a string, with the number of elements equal to the columns specified in the search (ie, this method returns a representation of the resulting table as an object array). It is useful to complete the data of a graphic component as a Table, etc.

You must specify the complete SQL query:
```
Object[] objects = admin.obtainJoin("SELECT Person.name, Person.age FROM Person", 2);
```

### 6.2.12 ###
### lazyLoad ###
By this method it is possible to load an object gradually as needed.

For example, for the following classes:
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

Only the attributes of Person are loaded (1), then the values of Phone's attributes are added (2) and finally those of Company (3):

```
Person p = new Person();

//Person Attributes(1)
admin.lazyLoad(p, "age=22");
//Phone is NULL at this moment

//Attributes from Phone are added
admin.lazyLoad(p);
//Phone is already complete, except for Company which is NULL

//Attributes from Company are added
admin.lazyLoad(p);
```

### 6.2.13 ###
### executeQuery ###
"executeQuery" is perhaps the simplest method, since all it does is execute a statement in the database and the only thing returned is True if ends successfully or False otherwise.

Example:
```
admin.executeQuery("DELETE FROM Person");
```

### 6.2.14 ###
### checkTableExist ###
Determines if a given table exist (returns True), or otherwise if that Table does not exist (returns False).

Example:
```
if(admin.checkTableExist("Person")){
    System.out.println("The Table Exists");
}else{
    System.out.println("The Table Does Not Exists");
}
```

### 6.2.15 ###
### Transacciones ###
By default QuickDB manages transactions at the level of the object which begins the operation, it means, no matter if it is a simple object, composed with another objects, with collections, or with inheritance, must complete the operation (along with all the involving objects) successfully, otherwise it will discard all changes that were made during the operation. But it is also possible to establish that all operations are implemented independently, no matter what happens to the related objects, as follows:

```
admin.setAutoCommit(true);
```

It is also possible to manage the transactions explicitly, for the cases where it is necessary to save a group of object or neither.

```
Example example = new Example();
Person person = new Person("leeloo", 22);

admin.openAtomicBlock();
admin.save(example);
admin.save(person);
admin.closeAtomicBlock();
```

If we wanted to validate that all the operations were carried out with success or otherwise cancel the transaction and make a rollback, it could be done as follows:

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