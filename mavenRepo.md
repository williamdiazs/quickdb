# Using With Maven #

To add QuickDB to your project using Maven you have 2 choices:

  * Download the .JAR file and then execute:
```
mvn install:install-file  -Dfile=path-to-QuickDB-file-jar \
                          -DgroupId=cat.quickdb \
                          -DartifactId=QuickDB \
                          -Dversion=1.2 \
                          -Dpackaging=jar \
                          -DcreateChecksum=true
```


---

  * Or you can just use it adding this configuration to your POM:

Add the Repository:
```
<repositories>
  <repository>
    <id>QuickDB</id>
    <url>http://quickdb.googlecode.com/hg/mavenRepo</url>
  </repository>
</repositories>
```

Then add the Dependency:
```
<dependency>
  <groupId>cat.quickdb</groupId>
  <artifactId>QuickDB</artifactId>
  <version>1.2</version>
</dependency>
```


---

### Drivers: ###

  * For MySQL add the Following Dependency too:

```
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>5.1.8</version>
</dependency>
```