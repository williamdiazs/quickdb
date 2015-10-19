# 1: #
# Usando QuickDB #

## 1.1 ##
## Qué es QuickDB? ##
QuickDB es una librería que permite a un desarrollador centrarse en la definición de las Entidades que mapean las Tablas de la Base de Datos y realizar operaciones que permitan la interacción entre estas Entidades y la Base de Datos sin tener que realizar tediosas configuraciones, dejando simplemente que la librería a través de la estructura del objeto infiera como realizar el mapeo del objeto a la Base de Datos.
El Modelo de Datos que se vaya a persistir NO es necesario que implemente ninguna interfaz o herede de ninguna clase. Para trabajar con este Modelo de Datos QuickDB utiliza ciertas Anotaciones o también puede recurrirse al Modo Intuitivo (siguiendo ciertas Convenciones), donde QuickDB en base al tipo de dato de cada objeto y otras características determina como realizar los respectivos mapeos (también es posible la combinación de atributos con y sin anotaciones, como se vera mas adelante).


## 1.2 ##
## Por Qué QuickDB? ##
Porque la funcionalidad no es sinónimo de complejidad.
Ser capaz de realizar diversas operaciones no debe implicar perder mucho tiempo en tareas de configuración.


## 1.3 ##
## Capacidades de QuickDB ##
![http://quickdb.googlecode.com/files/capabilities1-2.png](http://quickdb.googlecode.com/files/capabilities1-2.png)
Estas capacidades pueden ser combinados con el uso de Herencia, Objetos compuestos, Colecciones (relación muchos-a-muchos, relación uno-a-muchos) y Creación automática de tablas, como ya se vera mas adelante.


## 1.4 ##
## Agregando QuickDB a un Proyecto Java ##
Para utilizar QuickDB es necesario incorporar esta librería en un proyecto Java, QuickDB utiliza los distintos drivers JDBC dependiendo de la Base de Datos con la que vaya a conectarse, de esta forma, no es necesario que QuickDB implemente funcionalidades especificas para cada motor de Base de Datos, sino que solo debe ser capaz de conectarse a uno de estos drivers, lo cual realiza en tiempo de ejecución. Teniendo como ventaja a su vez, no cargar al proyecto donde se incluya QuickDB con código que no va a utilizarse para cierto proyecto especifico.

Para incorporar QuickDB a un Proyecto hay diversas formas, las mas comunes suelen ser:
  * Agregar la librería al Proyecto desde el IDE (Ejemplo con Netbeans):
![http://quickdb.googlecode.com/files/addLibrary.jpg](http://quickdb.googlecode.com/files/addLibrary.jpg)

  * Agregar la librería como dependencia en un Proyecto Maven:
```
<dependency>
  <groupId>cat.quickdb</groupId>
  <artifactId>QuickDB</artifactId>
  <version>1.2</version>
</dependency>
```

Para poder agregar la dependencia con QuickDB desde Maven es necesario que la librería este accesible en nuestro repositorio local o en un repositorio externo, para lo cual hay 2 formas de habilitar su disponibilidad:

  1. Descargar la librería y luego instalarla en el Repositorio Local:
```
mvn install:install-file  -Dfile=path-to-QuickDB-file-jar \
                          -DgroupId=cat.quickdb \
                          -DartifactId=QuickDB \
                          -Dversion=1.2 \
                          -Dpackaging=jar \
                          -DcreateChecksum=true
```

  1. Agregar el Repositorio de QuickDB al Proyecto Maven:
```
<repositories>
  <repository>
    <id>QuickDB</id>
    <url>http://quickdb.googlecode.com/hg/mavenRepo</url>
  </repository>
</repositories>
```


## 1.5 ##
## Agregar el Driver de la Base de Datos ##
Una vez que ya se cuenta con un Proyecto armado con QuickDB incorporado, es necesario agregar el driver que conectara QuickDB con la Base de Datos.

QuickDB actualmente trabaja con las siguientes Bases de Datos:
  * **MySQL:** [http://dev.mysql.com/downloads/connector/j/](http://dev.mysql.com/downloads/connector/j/)
  * **PostgreSQL:** [http://jdbc.postgresql.org/download.html](http://jdbc.postgresql.org/download.html)
  * **SQLite:** [http://www.zentus.com/sqlitejdbc/](http://www.zentus.com/sqlitejdbc/)
  * **Firebird:** [http://www.firebirdsql.org/index.php?op=files&id=jaybird](http://www.firebirdsql.org/index.php?op=files&id=jaybird)

El proceso para agregar cualquiera de los drivers es el mismo descripto para la incorporación de QuickDB.


## 1.6 ##
## Requerimientos ##

  * **QuickDB trabaja con la Versión 1.5 de Java o Superior.**

Los Versiones de los Drivers con los que se han realizado las pruebas son las siguientes:

  * MySQL: Versión 5.1.5 o Superior
  * PostgreSQL: JDBC4 Versión 8.4-701 (o Compatible)
  * SQLite: Versión 056 (o Compatible)
  * Firebird: Versión 2.1.6 (o Compatible)