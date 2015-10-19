# 5: #
# Creación de Tablas #
QuickDB infiere a través del tipo de dato como crear la respectiva Tabla en la Base de Datos.
Para la Creación de las tablas se tiene en cuenta los conceptos explicados en la sección de “Convenciones” y “Anotaciones”.
En caso de no existir Anotaciones, se crea la Tabla tomando el nombre de la Clase como nombre de la Tabla, se utiliza el atributo (obligatorio) entero “id” como clave primaria de la Tabla auto incremental y se crean las demás columnas de acuerdo a los Tipos de Datos especificados en la sección “Convenciones”, todas con propiedad NOT NULL.
En el caso de los Strings se crea el campo con un largo igual a la cadena ingresada o igual a 150 si la primer cadena ingresada era menor a este tamaño. En cuanto a la Herencia, se agrega un campo en la Tabla con el nombre “parent\_id” el cual contendrá el valor de la clave primaria de la Tabla Padre. Para las colecciones se ignora el campo al mapear la Clase a una Tabla y se crea una Tabla Relacional entre la Clase y los elementos contenidos en la colección.
Para el caso de trabajar con Anotaciones, todas estas propiedades mencionadas y varias mas, pueden ser completamente configuradas utilizando la anotación @ColumnDefinition explicada previamente.

A continuación un ejemplo completo utilizando todas las anotaciones descriptas en la sección anterior y las tablas resultantes al realizar la primera inserción:

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

**Tabla: “ModelParentTest”** (Clase: “ModelParent”)
<br />
![http://quickdb.googlecode.com/files/tableParent.png](http://quickdb.googlecode.com/files/tableParent.png)

**Tabla: “CollecAnnotation”** (Clase: “CollectionAnnotation”)
<br />
![http://quickdb.googlecode.com/files/tableCollec.png](http://quickdb.googlecode.com/files/tableCollec.png)

**Tabla: “AnnotationModelCollecAnnotation”** (relación entre la Clase que contiene la colección y los items dentro de la colección)
<br />
![http://quickdb.googlecode.com/files/AnnotationCollec.png](http://quickdb.googlecode.com/files/AnnotationCollec.png)

**Tabla: “AnnotationModel”** (Clase: “ModelAnnotation”)
<br />
![http://quickdb.googlecode.com/files/Tableannotationmodel.png](http://quickdb.googlecode.com/files/Tableannotationmodel.png)

Cabe destacar que muchas de las anotaciones utilizadas podrían haberse obviado y seguir las convenciones, y se obtendrian los mismos resultados en el caso de la definición de los tipos de datos.