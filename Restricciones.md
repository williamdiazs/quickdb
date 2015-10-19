# 2: #
# Restricciones #

Para trabajar con QuickDB hay ciertas restricciones que hay que tener en cuenta:

  * Todas las clases del modelo deben contener un atributo entero de clave primaria, y debe ser el primer atributo declarado en la Clase.
  * Todas las clases del modelo deben contener un Constructor Vacío.
  * En el caso de una relación de Clases Padre-Hijo, si los métodos Get y Set de la clave primaria llevan el mismo nombre, entonces tanto el Padre como el Hijo deben tener el mismo valor de Clave Primaria (ya que al ser públicos los métodos el valor del Hijo sobreescribiría al del Padre), impidiendo que otra Clase a ser almacenada en la Base de Datos extienda de la misma Clase Padre (Se soluciona utilizando Anotaciones).
  * Para combinar el uso en una Clase de atributos con y sin anotaciones (para configurar la creación de tablas, es decir, al utilizar la anotación @ColumnDefinition), es necesario agregar la anotación @Table a la Clase.