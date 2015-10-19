# 11: #
# Ejemplo Paso a Paso #

Este ejemplo mostrara un aplicación concreta de QuickDB en la persistencia y manejo de una estructura de datos bastante completa utilizando tanto la modalidad sin y con anotaciones.
_(Este ejemplo puede diferir para la versión en desarrollo de QuickDB 1.3 actualmente en desarrollo debido a algunos cambios de notaciones que se están realizando en el código)_


---

## 11.1: ##
## Sin Anotaciones ##
**Creando la Clase "Person":**
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

**Creando la Clase "Address":**
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

**Creando la Clase "Buy":**
En este caso al trabajar con un atributo que es una colección de tipos de datos primitivos del lenguaje, por las restricciones de QuickDB estamos obligados a agregarle una anotación explicitando este caso.
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

**Creando la Clase "Customer":**
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

### Interactuando con AdminBase ###
Ahora para el resto del ejemplo, teniendo en cuenta el Modelo de Clases creado previamente, se creara una instancia COMPLETA de Customer y se procederá a **guardar** dicho objeto en la Base de Datos (vacía hasta este punto, sin ninguna Tabla creada):

```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        //Crear Instancia de AdminBase para MySQL
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        //Crear colección de enteros para Buy
        ArrayList codes1 = new ArrayList();
        codes1.add(4357673);
        codes1.add(5457334);
        //Crear colección de enteros para Buy
        ArrayList codes2 = new ArrayList();
        codes2.add(4567);
        codes2.add(3345);

        //Crear Objetos Buy
        Buy buy1 = new Buy("cat food", 16.5, codes1);
        Buy buy2 = new Buy("citric juice", 7, codes2);
        Buy buy3 = new Buy("space ship", 599.99, null);
        //Agregar Objetos Buy a una colección
        ArrayList buys = new ArrayList();
        buys.add(buy1);
        buys.add(buy2);
        buys.add(buy3);

        //Crear Objeto Address
        Address address = new Address("unnamed street", 123);

        //Crear Objeto Customer compuesto por los
        //Objetos creados previamente
        Customer customer = new Customer("Diego", "Sarmentero",
                "diego.sarmentero@gmail.com", address, buys);

        //Guardar Objeto Customer COMPLETO en la Base de Datos
        admin.save(customer);
    }
}
```

**Tablas creadas automáticamente para guardar la información:**
  * Address (Columnas: id, street, number)
  * Buy (Columnas: id, article, price)
  * BuyInteger (Columnas: id, base, object)
  * Customer (Columnas: id, email, address, parent\_id)
  * CustomerBuy (Columnas: id, base, related)
  * Person (Columnas: id, name, surname)


**Recuperar el Objeto "Customer" completo guardado en la Base de Datos:**
```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        Customer customer = new Customer();
        //De esta forma obtendriamos el Objeto Customer
        //junto con todos los Objetos asociados al mismo
        //en el mismo estado en que fue guardado.
        admin.obtain(customer).where("name").equal("Diego").find();

        //Para modificar el Objeto solo es necesario manipularlo
        //como cualquier otro objeto
        customer.setName("Leonardo");
        //Y luego ejecutar la modificación
        admin.modify(customer);
    }
}
```


---


## 11.2: ##
## Con Anotaciones ##
**Creando la Clase "Person":**
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

**Creando la Clase "Address":**
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

**Creando la Clase "Buy":**
En este caso al trabajar con un atributo que es una colección de tipos de datos primitivos del lenguaje, por las restricciones de QuickDB estamos obligados a agregarle una anotación explicitando este caso.
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

**Creando la Clase "Customer":**
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

### Interactuando con AdminBase ###
Ahora para el resto del ejemplo, teniendo en cuenta el Modelo de Clases creado previamente, se creara una instancia COMPLETA de Customer y se procederá a **guardar** dicho objeto en la Base de Datos (vacía hasta este punto, sin ninguna Tabla creada):

```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        //Crear Instancia de AdminBase para MySQL
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        //Crear colección de enteros para Buy
        ArrayList codes1 = new ArrayList();
        codes1.add(4357673);
        codes1.add(5457334);
        //Crear colección de enteros para Buy
        ArrayList codes2 = new ArrayList();
        codes2.add(4567);
        codes2.add(3345);

        //Crear Objetos Buy
        Buy buy1 = new Buy("cat food", 16.5, codes1);
        Buy buy2 = new Buy("citric juice", 7, codes2);
        Buy buy3 = new Buy("space ship", 599.99, null);
        //Agregar Objetos Buy a una colección
        ArrayList buys = new ArrayList();
        buys.add(buy1);
        buys.add(buy2);
        buys.add(buy3);

        //Crear Objeto Address
        Address address = new Address("unnamed street", 123);

        //Crear Objeto Customer compuesto por los
        //Objetos creados previamente
        Customer customer = new Customer("Diego", "Sarmentero",
                "diego.sarmentero@gmail.com", address, buys);

        //Guardar Objeto Customer COMPLETO en la Base de Datos
        admin.save(customer);
    }
}
```

**Tablas creadas automáticamente para guardar la información:**
  * Address (Columnas: id, street, number)
  * Buy (Columnas: id, item, price)
  * BuyInteger (Columnas: id, base, object)
  * Customer (Columnas: id, email, address, parent\_id)
  * CustomerBuy (Columnas: id, base, related)
  * Person (Columnas: id, name, surname)


**Recuperar el Objeto "Customer" completo guardado en la Base de Datos:**
```
import java.util.ArrayList;
import cat.quickdb.db.AdminBase;

public class App {

    public static void main(String[] args) {
        AdminBase admin = AdminBase.initialize(AdminBase.DATABASE.MYSQL, "localhost",
                "3306", "test", "root", "");

        Customer customer = new Customer();
        //De esta forma obtendriamos el Objeto Customer
        //junto con todos los Objetos asociados al mismo
        //en el mismo estado en que fue guardado.
        admin.obtain(customer).where("name").equal("Diego").find();
    }
}
```

**Código utilizado en el ejemplo (Proyecto Maven):** [SOURCE](http://quickdb.googlecode.com/files/QuickDBExample.zip)

**Más Ejemplos en:** [QuickDBTesting](http://code.google.com/p/quickdb/source/browse/testing/QuickDBTesting#QuickDBTesting/src/main/java/quickdb)
<br>
<i>Clase Principal:</i> <a href='http://code.google.com/p/quickdb/source/browse/testing/QuickDBTesting/src/main/java/quickdb/tests/quickdbtesting/App.java'>App.java</a>