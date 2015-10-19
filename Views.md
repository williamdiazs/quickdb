# 10: #
# Views #

The Views on QuickDB are used to create simpler representations of complex Data Structures, such as Objects compound by other Objects, etc.
A View provides the possibility of obtaining data more quickly, as not having to process all objects hierarchically involved, but the values assigned directly to the View attributes, greatly reducing processing time.

To build up a View is necessary to create a Class extending from "View", which specifies a method called **“query()”** that must be implemented by the SubClass.
This method **“query()”** returns a data “Object” type which may be a String containing a string specifying the query, or may be a data “QuickDB Query” type allowing to form the query with the facilities of this query system.

There are 3 other methods which implementation its optional, depending on the type of View one wants to create:

  * **renameColumns():** It specifies in a string the new names that each of the fields will take from the resulting query.This names will be used to obtain the values and assign them to the specific attribute inside the View. If this method it's not implemented, the new names from the attributes of the View are taken by default in the order they appear.
  * **columns():** It specifies in a string which attributes will be taken into account on the query, if this method it's not implemented, the names from each of the View attributes are taken in the order they appear.
  * **classes():** It specifies in an Array of Class[.md](.md) type the classes each of the specified attributes in “columns()” belong to.If this method it's not implemented, is assumed that all attributes are from the SuperClass specified on the query.


## 10.1 ##
## Examples ##

Based on the following Data Model:

```
public class ObjectViewTest1{
    private int id;
    private String name;
    private String account;
    private ObjectViewTest2 obj2;
}

public class ObjectViewTest2{
    private int id;
    private String description;
    private Date date;
}
```

The following Views are created:

  1. Using the Query System “QuickDB Query”, and rewriting all configuration methods  of View
```
import cat.quickdb.db.View;
import cat.quickdb.query.Query;
import java.sql.Date;

public class ViewObject extends View{

    private String name;
    private String description;
    private String account;
    private Date dateView;

    @Override
    public Object query(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        Query query = Query.create(this.getAdminBase(), o1);
        query.where("account", ObjectViewTest1.class).equal("account test");
        return query;
    }

    @Override
    public String columns(){
        return "name, description, account, date";
    }

    @Override
    public String renameColumns(){
        return "name, description, account, dateView";
    }

    @Override
    public Class[] classes(){
        return new Class[]{ObjectViewTest1.class, ObjectViewTest2.class, ObjectViewTest1.class, ObjectViewTest2.class};
    }

}
```

  1. Using the query specification on a text string.
```
import cat.quickdb.db.View;
import java.sql.Date;

public class ViewObjectString extends View{

    private String name;
    private String description;
    private String account;
    private Date dateView;

    @Override
    public Object query(){
        return "SELECT ObjectViewTest1.name 'name', ObjectViewTest2.description 'description', " +
                "ObjectViewTest1.account 'account', ObjectViewTest2.date 'dateView' " +
                "FROM ObjectViewTest1 " +
                "JOIN ObjectViewTest2 ON ObjectViewTest1.obj2 = ObjectViewTest2.id " +
                "WHERE ObjectViewTest1.account = 'account test'";
    }

}
```

  1. Using the Query System  “QuickDB Query”, without implementing the method of renaming the resulting columns.
```
import cat.quickdb.db.View;
import cat.quickdb.query.Query;
import java.sql.Date;

public class ViewObjectWithoutRename extends View{

    private String name;
    private String description;
    private String account;
    private Date date;

    @Override
    public Object query(){
        ObjectViewTest1 o1 = new ObjectViewTest1();
        Query query = Query.create(this.getAdminBase(), o1);
        query.where("account", ObjectViewTest1.class).equal("account test");
        return query;
    }

    @Override
    public String columns(){
        return "name, description, account, date";
    }

    @Override
    public Class[] classes(){
        return new Class[]{ObjectViewTest1.class, ObjectViewTest2.class, ObjectViewTest1.class, ObjectViewTest2.class};
    }

}
```

### 10.1.1 ###
### Creating an Instance of the View ###
There are 2 ways to initialize a View, the first consist of executing the method **“initializeViews(...)”** of “AdminBase” which will initialize all instances created or to be created from the Data Model. And the other way is to give an active instance of AdminBase to the View:

```
//1:
AdminBase.initializeViews(AdminBase.DATABASE.MYSQL, "[HOST]", "[PORT]", "[DB]", "[USER]", "[PASSWORD]");

//2:
ViewObject view = new ViewObject();
view.initilizeAdminBase(admin);
```

### 10.1.2 ###
### Obtaining the Values of the View ###
Once the instance of View is created, you just need to use the methods **“obtain()”** or **“obtainAll()”** to get View results based on the objects from the Database:

```
//1:
view.obtain();

//2:
ArrayList array = view.obtainAll();
```

### 10.1.3 ###
### Dynamics Query in a View ###
A View is specified based on a consult that represents it, but if during runtime you would like to change that query in order to add an other condition for the obtained Views to be particular to a situation, you can resort to the method “dynamicQuery” of the View, which, as seen in method “query()”, can be worked through String or using the Query System of QuickDB, which concatenates on the query that represents the View a new set of conditions to be evaluated.

Example:

```
//Using String:
ViewObjectString view = new ViewObjectString();
view.initializeView(admin);
view.dynamicQuery("AND name LIKE '%Dynamic%'");
view.obtain();

//Using QuickDB Query
ViewObject view = new ViewObject();
view.initializeView(admin);
view.dynamicQuery( ((Query) view.query()).and("name").match("Dynamic") );
view.obtain();
```