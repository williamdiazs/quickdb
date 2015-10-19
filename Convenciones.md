# 3: #
# Convenciones #
Las convenciones se utilizan para eliminar las tareas de configuración dentro de QuickDB, hay muchas propiedades que pueden ser deducidas automáticamente sin necesidad de configuración (aunque todo es posible de configurar si se desea como se vera en la sección siguiente de Anotaciones), y a continuación se muestran cuales son los detalles a tener en cuenta en el Modelo de Datos para que sea soportado por QuickDB sin necesidad de realizar mas que la escritura de las Entidades.
Estas convenciones son necesarias al no trabajar con Anotaciones, de lo contrario, pueden escribirse las Entidades de cualquier otra forma siempre que se especifiquen dichas características mediante las anotaciones.


## 3.1 ##
## Para Claves Primarias ##
Cada Clase del modelo debe declarar como primer atributo una variable entera de nombre id”:

```
public class Person{
   private int id;
   ...
}
```


## 3.2 ##
## Herencia ##
Para que QuickDB pueda reconocer la herencia automáticamente, es necesario que la Clase de la que se extienda se encuentre dentro del mismo paquete que la Clase hija, esto osibilita poder determinar cuando dejar de analizar la herencia de forma recursiva y no llegar hasta el nivel de Object.

```
package com.example;

public class Parent{
   ...
}

package com.example;

public class Son extends Parent{
   ...
}
```

## 3.3 ##
## Colecciones ##
Al trabajar con colecciones, solo son soportadas aquellas que implementan la interfaz "java.util.List" o "java.util.Collection", y cuando se trabaja sin anotaciones además el nombre del atributo debe ser igual al nombre de la clase de los objetos que compondrán dicha colección (con la primer letra en minúscula si se desea):

```
public class Person{
   private ArrayList phone;
}

public class Phone{
   ...
}
```

Para colecciones que contengan solo tipos de datos primitivos, se explicara en la sección “Anotaciones” como contemplar este caso.


## 3.4 ##
## Getters y Setters ##
Al no trabajar con anotaciones, QuickDB debe determinar automáticamente cuales serán los métodos desde los que se obtendrán los valores de los atributos, y cuales serán los métodos a través de los cuales se le podrá dar valores a los atributos. Para hacer esto se sigue la convención por defecto de escribir "get" o "set" y luego el nombre del atributo con CamelCase:

```
public class Person{

   private int id;
   private String name;

   public void setId(int id){...}
   public int getId(){...}
   public void setName(String name){...}
   public String getName(){...}
}
```


## 3.5 ##
## Tipos de Datos ##
![http://quickdb.googlecode.com/files/tablaTipoDato.png](http://quickdb.googlecode.com/files/tablaTipoDato.png)