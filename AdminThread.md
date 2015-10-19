# 8: #
# AdminThread #
AdminThread expone prácticamente los mismas funcionalidades que AdminBase, con la diferencia de que todos las operaciones ejecutadas son creadas en un hilo de ejecución distinto. Esta funcionalidad permite realizar operaciones que involucren a Objetos o Colecciones de Objetos muy grandes sin bloquear la aplicación.
Un detalle a tener en cuenta, es que como las operaciones se ejecutan en un hilo aparte, no se sabe en que momento se van a ejecutar, y por lo tanto, ninguno de los métodos devuelve ningún valor.

Hay 2 formas de inicializar un objeto AdminThread:

```
AdminThread adminThread1 = new AdminThread(admin);

AdminThread adminThread1 = new AdminThread(DATABASE.MYSQL,
     "localhost", "3306", "testQuickDB", "root", "pass");
```

La primera consiste en pasarle al constructor una instancia ya creada de AdminBase de la cual hará uso para las operaciones, y la segunda consiste en pasarle los mismos datos que se le pasan al método de inicialización de AdminBase para que AdminThread cree su propia instancia de AdminBase.

Las operaciones soportadas por **AdminThread** son:

  * **save**
  * **modify**
  * **delete**
  * **execute**
  * **lazyLoad**
  * **obtainWhere**
  * **obtainSelect**
  * **obtain (Object)**
  * **obtain (String)**
  * **saveAll**
  * **modifyAll**
  * **setAutoCommit**

Operaciones como “saveGetIndex” se han dejado afuera, ya que la finalidad misma de la operación es obtener el valor de retorno, y ninguna de las funciones expuestas por AdminThread tiene valor de retorno.
También es importante tener en cuenta que aunque se cuenta con las operaciones de “obtain...”, estas no aseguran que el Objeto sea cargado en ese momento, ya que no se sabe cuando se ejecutara el hilo.