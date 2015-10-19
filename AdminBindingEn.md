# 7: #
# AdminBinding #
AdminBinding allows the developer to create the data model extending the AdminBinding Class (but this is not required, this is another available resource to simplify some processes), which serves as a "link" between the entities and AdminBase to allow the operations that interact with the database to be executed from the object itself and not use AdminBase intermediary as directly.
AdminBinding not cover all the functionality that AdminBase provides, as it only handles those operations that are specific to the object that contains them, leaving out those that refer to collections, etc.

In this way, the interaction could be even more natural, just telling the object which operation to execute. It is necessary before starting to work with the entities of the model to execute the following line of code, which initializes the connection with the database for all the instances of the model:

```
AdminBase.initializeAdminBinding(AdminBase.DATABASE.MYSQL,
        "[HOST]", "[PORT]", "[DATABASE]", "[USER]", "[PASSWORD]");
```

Where the properties are the same as for the initialization of AdminBase:
  * The DBMS to be used.
  * Host
  * Port
  * Database
  * User
  * Password
  * Schema (Only for Postgre)

The Methods inherited from AdminBinding Class are:

  * save
  * saveGetIndex
  * delete
  * modify
  * obtain
  * obtainSelect
  * obtainWhere
  * lazyLoad

Methods as "obtainAll", "saveAll", etc. Have been left out because they do not specifically refer to the object that contains them, but refer to collections or other operations involving external objects.

Examples are based on the following entity:

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
## Operations ##
The operations are specified in the same way as is done in "AdminBase" with the difference that the first attribute is omitted, which one was the object that involved the operation, and now this object is the one that execute the action by itself.

Example:

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
_(Both of QuickDB Query Systems are supported)_
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

Operation is executed as follows:

```
Person p = new Person();

//Person Attributes(1)
p.lazyLoad("age=23");
//Phone is NULL at this moment

//Attributes from Phone are added
p.lazyLoad("");
//Phone is already complete, except for Company which is NULL

//Attributes from Company are added
p.lazyLoad("");
```