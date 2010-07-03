package cat.quickdb.tests;

import quickdb.db.AdminBase;

public class QuickDBTests {

    public static final AdminBase.DATABASE db = AdminBase.DATABASE.MYSQL;
    public static final String host = "localhost";
    public static final String port = "3306";
    //Postgre: 5432
    //Firebird: 3050
    public static final String instanceDB = "testQuickDB";
    //Postgre: prueba
    //Firebird: /home/gato/employee.fdb
    public static final String user = "root";
    //Postgre: postgres
    //Firebird: SYSDBA
    public static final String pass = "";
    //Postgre: postgres
    //Firebird: firebird
    public static final String scheme = "";
    //public static final String scheme = "testing";    //Only for Postgre

}
