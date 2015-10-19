# 5: #
# Creating Tables #
QuickDB infers, through the data type, how to create the corresponding table in the database.
For Creating the tables are taken into account the concepts explained in the section [Conventions](http://code.google.com/p/quickdb/wiki/Conventions#3:) and [Annotations](http://code.google.com/p/quickdb/wiki/Annotations#4:).
In absence of annotations, establishing the Table taking the name of the class as the table's name , using the attribute (required) integer "id" as auto incremental primary key and creating the other columns according the data types specified in the [Conventions](http://code.google.com/p/quickdb/wiki/Conventions#3:), all with NOT NULL property.
In case of Strings, the field is created with a length equal to that of the entered String or equal to 150 if the first input is smaller than this size. In case of Inheritance, is added a field in the table with the name "parent\_id" which will contain the primary key value of the Parent table. For collections, field is ignored when mapping the class to a table, and a new relational table between the Class and the elements contained in the collection, is created.
In case of working with annotations, all these properties mentioned and several more, can be completely configured using the annotation [@!ColumnDefinition](http://code.google.com/p/quickdb/wiki/Annotations#4.4) previously explained.


Following is a complete example using all annotations described in the previous section and the resulting tables after the first insertion:

```
import cat.quickdb.annotation.Table;

@Table("ModelParentTest")
public class ModelParent{
    private int id;
    private String description
}


import cat.quickdb.annotation.Table;
import cat.quickdb.annotation.Column;
import cat.quickdb.annotation.ColumnDefinition;
import cat.quickdb.annotation.Definition.DATATYPE;
import cat.quickdb.annotation.Properties.TYPES;

@Table("CollecAnnotation")
public class CollectionAnnotation{
    
    @Column(type=TYPES.PRIMARYKEY)
    @ColumnDefinition(autoIncrement=true, length=11,
        primary=true, type=DATATYPE.INT)
    private int idCollection;
    @Column(name="itemName", setter="setItemName", getter="getItemName")
    private String item;
    @Column(ignore=true)
    private String nothing;
    
}

import cat.quickdb.annotation.Table;
import cat.quickdb.annotation.Column;
import cat.quickdb.annotation.Parent;
import cat.quickdb.annotation.ColumnDefinition;
import cat.quickdb.annotation.Definition.DATATYPE;
import cat.quickdb.annotation.Properties.TYPES;
import cat.quickdb.annotation.Validation;
import java.sql.Date;
import java.util.ArrayList;

@Table("AnnotationModel")
public class ModelAnnotation extends ModelParent{

    @Column(type=TYPES.PRIMARYKEY)
    @ColumnDefinition(autoIncrement=true, length=11,
        primary=true, type=DATATYPE.INT)
    private int idCollection;
    @Column(name="modelName")
    @ColumnDefinition(length=300, defaultValue="test")
    private String name;
    @Validation(numeric={Validation.GREATER, 18})
    @ColumnDefinition(type=DATATYPE.INTEGER)
    private int age;
    @ColumnDefinition(type=DATATYPE.DATETIME)
    private Date birth;
    @Column(getter="getterSalary")
    private double salary;
    @Column(type=TYPES.COLLECTION, collectionClass="cat.quickdb.annotations.model.CollectionAnnotation")
    private ArrayList array;
    @Column(type=TYPES.FOREIGNKEY, name="foreignCollec")
    @ColumnDefinition(type=DATATYPE.INTEGER)
    private CollectionAnnotation collec;

}
```

**Table: “ModelParentTest”** (Class: “ModelParent”)
<br />
![http://quickdb.googlecode.com/files/tableParent.png](http://quickdb.googlecode.com/files/tableParent.png)

**Table: “CollecAnnotation”** (Class: “CollectionAnnotation”)
<br />
![http://quickdb.googlecode.com/files/tableCollec.png](http://quickdb.googlecode.com/files/tableCollec.png)

**Table: “AnnotationModelCollecAnnotation”** (relationship between the class that contains the collection and the items in the collection)
<br />
![http://quickdb.googlecode.com/files/AnnotationCollec.png](http://quickdb.googlecode.com/files/AnnotationCollec.png)

**Table: “AnnotationModel”** (Class: “ModelAnnotation”)
<br />
![http://quickdb.googlecode.com/files/Tableannotationmodel.png](http://quickdb.googlecode.com/files/Tableannotationmodel.png)

It should be noted that many of the annotations used, could have been overlooked following the conventions and the same results would be obtained in the definition of data types.