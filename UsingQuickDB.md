# 1: #
# Usando QuickDB #

## 1.1 ##
## What is QuickDB? ##
QuickDB is a library that allows a developer to focus on the definition of the entities that represent the tables of the database and perform operations that allow the interaction between these entities and the database without having to perform tedious configurations, leaving to the library the task to infer the object structure and make the mapping of the object to the Database.
The Data Model to be persist doesn't need to implement any interface or extend from another Class. QuickDB uses certain Annotations or it is also possible to use the Intuitive Mode (following some Conventions) to work with the Data Model, where QuickDB determines based on the Data Type of each Object and some other features how to perform the proper mappings (It is also possible to combine attributes with and without Annotations in a single Class).


## 1.2 ##
## Why QuickDB? ##
Functionality is not synonymous with Complexity.
Being able to do a lot of things don't have to involve waste a lot of time in configuration tasks.


## 1.3 ##
## Capacidades de QuickDB ##
![http://quickdb.googlecode.com/files/capabilities1-2.png](http://quickdb.googlecode.com/files/capabilities1-2.png)
This capabilities can be combined with Inheritance, Compound Objects, Collections (Many to Many Relation, One to Many Relation) and Automatic Table Creation.


## 1.4 ##
## Adding QuickDB to a Java Project ##
To use QuickDB it is necessary to add the library into your Java Project, QuickDB use the different JDBC drivers depending on which Database is going to work with, in this way, it is not necessary for QuickDB to implement specific functionalities for each DBMS, but that should only be able to establish the connection with the Database through one of this drivers in runtime. This present the advantage to avoid the need of having in the project functionalities from another DBMS that is not going to be used.

To add QuickDB into a Project there are several ways, the most common are:
  * Add the library into the Project using the IDE (Netbeans example):
![http://quickdb.googlecode.com/files/addLibrary.jpg](http://quickdb.googlecode.com/files/addLibrary.jpg)

  * Add the library into the Project as a Maven Dependency:
```
<dependency>
  <groupId>cat.quickdb</groupId>
  <artifactId>QuickDB</artifactId>
  <version>1.2</version>
</dependency>
```

To be able to add QuickDB dependency into a Maven Project, it is necessary that the library can be reachable from the local repository or from an external repository,
Para poder agregar la dependencia con QuickDB desde Maven es necesario que la librería este accesible en nuestro repositorio local o en un repositorio externo, what can be done in two different ways:

  1. Download the library and then install it in the Local Repository:
```
mvn install:install-file  -Dfile=path-to-QuickDB-file-jar \
                          -DgroupId=cat.quickdb \
                          -DartifactId=QuickDB \
                          -Dversion=1.2 \
                          -Dpackaging=jar \
                          -DcreateChecksum=true
```

  1. Add QuickDB Repository into your Maven Project:
```
<repositories>
  <repository>
    <id>QuickDB</id>
    <url>http://quickdb.googlecode.com/hg/mavenRepo</url>
  </repository>
</repositories>
```


## 1.5 ##
## Add Database Driver ##
Once you have a Project that include QuickDB, it is necessary to add the driver that will establish the connection between QuickDB and the Database.

QuickDB currently works with the following Databases:
  * **MySQL:** [http://dev.mysql.com/downloads/connector/j/](http://dev.mysql.com/downloads/connector/j/)
  * **PostgreSQL:** [http://jdbc.postgresql.org/download.html](http://jdbc.postgresql.org/download.html)
  * **SQLite:** [http://www.zentus.com/sqlitejdbc/](http://www.zentus.com/sqlitejdbc/)
  * **Firebird:** [http://www.firebirdsql.org/index.php?op=files&id=jaybird](http://www.firebirdsql.org/index.php?op=files&id=jaybird)

The process to add any of this drivers is the same as described to include QuickDB.


## 1.6 ##
## Requirements ##

  * **QuickDB works with Java version 1.5 or higher.**

The versions of the drivers with those who have carried out the tests are:

  * MySQL: Versión 5.1.5 or Higher
  * PostgreSQL: JDBC4 Versión 8.4-701 (or Compatible)
  * SQLite: Versión 056 (or Compatible)
  * Firebird: Versión 2.1.6 (or Compatible)