# 8: #
# AdminThread #
AdminThread exposes virtually the same functionality as AdminBase, with the difference that all the transactions executed are created in a different execution thread. This feature allows to execute operations involving objects or very large collections of objects without blocking the application. One detail to note is that the operations are executed in a separate thread, it is not known at what time is going to be executed, and therefore none of the methods return a value.

There are two ways to initialize an AdminThread object:

```
AdminThread adminThread1 = new AdminThread(admin);

AdminThread adminThread1 = new AdminThread(DATABASE.MYSQL,
     "localhost", "3306", "testQuickDB", "root", "pass");
```

The first one consist to pass to the constructor an instance of AdminBase and which will be uses for the different operations, and the second one is to pass the same data that is passed to the method of initialization from AdminBase to allow AdminThread to create his own instance of AdminBase .

**AdminThread** support the following operations:

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

Operations as "saveGetIndex" have been left out, since the purpose of the operation is to obtain the return value, and none of the tasks set by AdminThread has a return value. It is also important to note that although operations as "obtain..." are included, these do not ensure that the object is loaded at that time, and we do not know when the thread is going to be executed.