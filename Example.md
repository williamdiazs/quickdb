# 11: #
# Step by Step Example #

This example will show a concrete application of QuickDB for persistence and management of a fairly complete data structure using both types with and without annotations.
_(This example may differ for QuickDB 1.3 version currently in development due to some changes being made in the code notations)_


---

## 11.1: ##
## Without Annotations ##
**Create "Person" Class:**
```
public class Person {

    private int id;
    private String name;
    private String surname;

    public Person() {
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

}
```

**Create "Address" Class:**
```
public class Address {

    public int id;
    public String street;
    public int number;

    public Address() {
    }

    public Address(String street, int number) {
        this.street = street;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getStreet() {
        return street;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
```

**Create "Buy" Class:**
In this case, because this class has an attribute which is a collection of primitive types, based on QuickDB restrictions it is necessary to annotate that attribute as follow:
```
import java.util.ArrayList;
import cat.quickdb.annotation.Column;
import cat.quickdb.annotation.Table;

@Table
public class Buy {

    private int id;
    private String article;
    private double price;
    @Column(collectionClass="java.lang.Integer")
    private ArrayList<Integer> codes;

    public Buy() {
    }

    public Buy(String article, double price, ArrayList<Integer> codes) {
        this.article = article;
        this.price = price;
        this.codes = codes;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setCodes(ArrayList<Integer> codes) {
        this.codes = codes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getArticle() {
        return article;
    }

    public ArrayList<Integer> getCodes() {
        return codes;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

}
```

**Create "Customer" Class:**
```
import java.util.ArrayList;

public class Customer extends Person{

    private int id;
    private String email;
    private Address address;
    private ArrayList buy;

    public Customer() {
    }

    public Customer(String name, String surname, String email, Address address,
            ArrayList buy) {
        super(name, surname);
        this.email = email;
        this.address = address;
        this.buy = buy;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBuy(ArrayList buy) {
        this.buy = buy;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public ArrayList getBuy() {
        return buy;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
```

### Interacting with AdminBase ###
Now for the rest of this example, having the Data Model described before, a complete instance of Customer is going to be stored in the Data Base (The Data Base at this point is empty and no table has been created).

```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        //Create AdminBase instance for MySQL
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        //Create integer collection for Buy
        ArrayList codes1 = new ArrayList();
        codes1.add(4357673);
        codes1.add(5457334);
        //Create integer collection for Buy
        ArrayList codes2 = new ArrayList();
        codes2.add(4567);
        codes2.add(3345);

        //Create Buy Objects
        Buy buy1 = new Buy("cat food", 16.5, codes1);
        Buy buy2 = new Buy("citric juice", 7, codes2);
        Buy buy3 = new Buy("space ship", 599.99, null);
        //Add Buy Objects to a collection
        ArrayList buys = new ArrayList();
        buys.add(buy1);
        buys.add(buy2);
        buys.add(buy3);

        //Create Address Object
        Address address = new Address("unnamed street", 123);

        //Create Customer Object compound with
        //the objects created before
        Customer customer = new Customer("Diego", "Sarmentero",
                "diego.sarmentero@gmail.com", address, buys);

        //Store Customer Object in the Database
        admin.save(customer);
    }
}
```

**Tables automatically created to store the information:**
  * Address (Columns: id, street, number)
  * Buy (Columns: id, article, price)
  * BuyInteger (Columns: id, base, object)
  * Customer (Columns: id, email, address, parent\_id)
  * CustomerBuy (Columns: id, base, related)
  * Person (Columns: id, name, surname)


**Recover complete "Customer" Object stored in the database:**
```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        Customer customer = new Customer();
        //In this way we will obtain the Customer Object
        //along with all the objects related to it
        //in the same state as they were stored
        admin.obtain(customer).where("name").equal("Diego").find();

        //Para modificar el Objeto solo es necesario manipularlo
        //To modify the object only is necessary to manipulate it
        //as any other object
        customer.setName("Leonardo");
        //And then execute the modification
        admin.modify(customer);
    }
}
```


---


## 11.2: ##
## With Annotations ##
**Create "Person" Class:**
```
import cat.quickdb.annotation.*;

@Table
public class Person {

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(autoIncrement=true, primary=true,
        type=Definition.DATATYPE.INTEGER, length=11)
    private int id;
    @Column
    private String name;
    @Column
    private String surname;

    public Person() {
    }

    public Person(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

}
```

**Create "Address" Class:**
```
import cat.quickdb.annotation.*;

@Table
public class Address {

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(autoIncrement=true, primary=true,
        type=Definition.DATATYPE.INTEGER, length=11)
    public int id;
    @Column(type=Properties.TYPES.PRIMITIVE)  //Por defecto es PRIMITIVE
    public String street;
    @Column
    @ColumnDefinition(type=Definition.DATATYPE.INTEGER)
    public int number;

    public Address() {
    }

    public Address(String street, int number) {
        this.street = street;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getStreet() {
        return street;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}
```

**Create "Buy" Class:**
In this case, because this class has an attribute which is a collection of primitive types, based on QuickDB restrictions it is necessary to annotate that attribute as follow:
```
import java.util.ArrayList;
import cat.quickdb.annotation.*;

@Table
public class Buy {

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(autoIncrement=true, primary=true,
        type=Definition.DATATYPE.INTEGER, length=11)
    private int id;
    @Column(name="item")
    private String article;
    @Column
    @ColumnDefinition(type=Definition.DATATYPE.DOUBLE)
    private double price;
    @Column(type=Properties.TYPES.COLLECTION, collectionClass="java.lang.Integer")
    private ArrayList<Integer> codes;

    public Buy() {
    }

    public Buy(String article, double price, ArrayList<Integer> codes) {
        this.article = article;
        this.price = price;
        this.codes = codes;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public void setCodes(ArrayList<Integer> codes) {
        this.codes = codes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getArticle() {
        return article;
    }

    public ArrayList<Integer> getCodes() {
        return codes;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

}
```

**Create "Customer" Class:**
```
import java.util.ArrayList;
import cat.quickdb.annotation.*;

@Parent
@Table
public class Customer extends Person{

    @Column(type=Properties.TYPES.PRIMARYKEY)
    @ColumnDefinition(autoIncrement=true, primary=true,
        type=Definition.DATATYPE.INTEGER, length=11)
    private int id;
    @Column
    private String email;
    @Column(type=Properties.TYPES.FOREIGNKEY)
    private Address address;
    @Column(type=Properties.TYPES.COLLECTION)
    private ArrayList buy;

    public Customer() {
    }

    public Customer(String name, String surname, String email, Address address,
            ArrayList buy) {
        super(name, surname);
        this.email = email;
        this.address = address;
        this.buy = buy;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setBuy(ArrayList buy) {
        this.buy = buy;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public ArrayList getBuy() {
        return buy;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
```

### Interacting with AdminBase ###
Now for the rest of this example, having the Data Model described before, a complete instance of Customer is going to be stored in the Data Base (The Data Base at this point is empty and no table has been created):

```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        //Create AdminBase instance for MySQL
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        //Create integer collection for Buy
        ArrayList codes1 = new ArrayList();
        codes1.add(4357673);
        codes1.add(5457334);
        //Create integer collection for Buy
        ArrayList codes2 = new ArrayList();
        codes2.add(4567);
        codes2.add(3345);

        //Create Buy Objects
        Buy buy1 = new Buy("cat food", 16.5, codes1);
        Buy buy2 = new Buy("citric juice", 7, codes2);
        Buy buy3 = new Buy("space ship", 599.99, null);
        //Add Buy Objects to a collection
        ArrayList buys = new ArrayList();
        buys.add(buy1);
        buys.add(buy2);
        buys.add(buy3);

        //Create Address Object
        Address address = new Address("unnamed street", 123);

        //Create Customer Object compound with
        //the objects created before
        Customer customer = new Customer("Diego", "Sarmentero",
                "diego.sarmentero@gmail.com", address, buys);

        //Store Customer Object in the Database
        admin.save(customer);
    }
}
```

**Tables automatically created to store the information:**
  * Address (Columns: id, street, number)
  * Buy (Columns: id, item, price)
  * BuyInteger (Columns: id, base, object)
  * Customer (Columns: id, email, address, parent\_id)
  * CustomerBuy (Columns: id, base, related)
  * Person (Columns: id, name, surname)


**Recover complete "Customer" Object stored in the database:**
```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        Customer customer = new Customer();
        //In this way we will obtain the Customer Object
        //along with all the objects related to it
        //in the same state as they were stored
        admin.obtain(customer).where("name").equal("Diego").find();
    }
}
```

**Source Code used in this Example (Maven Project):** [SOURCE](http://quickdb.googlecode.com/files/QuickDBExample.zip)

**More Examples at:** [QuickDBTesting](http://code.google.com/p/quickdb/source/browse/testing/QuickDBTesting#QuickDBTesting/src/main/java/quickdb)
<br>
<i>Main Class:</i> <a href='http://code.google.com/p/quickdb/source/browse/testing/QuickDBTesting/src/main/java/quickdb/tests/quickdbtesting/App.java'>App.java</a>