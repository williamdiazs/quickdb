package quickdb.db.dbms;

import quickdb.db.AdminBase;
import quickdb.db.connection.ConnectionDB;
import quickdb.db.connection.ConnectionDBFirebird;
import quickdb.db.connection.ConnectionDBPostgre;
import quickdb.db.connection.IConnectionDB;
import quickdb.db.connection.ProxyConnectionDB;
import quickdb.db.dbms.firebird.Firebird;
import quickdb.db.dbms.mysql.MySQL;
import quickdb.db.dbms.postgre.PostgreSQL;
import quickdb.db.dbms.sqlite.SQLite;
import quickdb.reflection.EntityManager;

/**
 *
 * @author Diego Sarmentero
 */
public class DbmsInterpreter {

    public static String[] properties;

    public static ConnectionDB connect(AdminBase.DATABASE db, String... args){
        DbmsInterpreter.properties = args;
        ConnectionDB conex = null;
        switch (db) {
            case MariaDB:
            case MYSQL:
                conex = new ConnectionDB(args);
                conex.connectMySQL();
                break;
            case SQLSERVER:
                conex = new ConnectionDB(args);
                conex.connectSQLServer();
                break;
            case POSTGRES:
                conex = new ConnectionDBPostgre(args);
                conex.connectPostgres();
                break;
            case SQLite:
                conex = new ConnectionDB("", "", args[0], "", "");
                conex.connectSQLite();
                break;
            case FIREBIRD:
                conex = new ConnectionDBFirebird(args);
                conex.connectFirebird();
                break;
        }

        return conex;
    }

    public static IConnectionDB connectLogging(AdminBase.DATABASE db, String path, String... args){
        DbmsInterpreter.properties = args;
        IConnectionDB conex = null;
        ProxyConnectionDB.configureLogger(path);
        switch (db) {
            case MYSQL:
                conex = (IConnectionDB) ProxyConnectionDB.newInstance(new ConnectionDB(args));
                conex.connectMySQL();
                break;
            case SQLSERVER:
                conex = (IConnectionDB) ProxyConnectionDB.newInstance(new ConnectionDB(args));
                conex.connectSQLServer();
                break;
            case POSTGRES:
                conex = (IConnectionDB) ProxyConnectionDB.newInstance(new ConnectionDBPostgre(args));
                conex.connectPostgres();
                break;
            case SQLite:
                conex = (IConnectionDB) ProxyConnectionDB.newInstance(new ConnectionDB("", "", args[0], "", ""));
                conex.connectSQLite();
                break;
            case FIREBIRD:
                conex = (IConnectionDB) ProxyConnectionDB.newInstance(new ConnectionDBFirebird(args));
                conex.connectFirebird();
                break;
        }

        return conex;
    }

    public static boolean createTable(AdminBase.DATABASE db, AdminBase admin, Object entity,
            Object[] objects, EntityManager manager){

        boolean value = false;
        switch(db){
            case SQLite:
                value = SQLite.createTable(admin, entity, objects, manager);
                break;
            case MYSQL:
                value = MySQL.createTable(admin, entity, objects, manager);
                break;
            case POSTGRES:
                value = PostgreSQL.createTable(admin, entity, objects, manager);
                break;
            case FIREBIRD:
                value = Firebird.createTable(admin, entity, objects, manager);
                break;
        }

        return value;
    }

}
