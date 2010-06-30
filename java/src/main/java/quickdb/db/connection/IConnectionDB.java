package quickdb.db.connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gato
 */
public interface IConnectionDB {

    public void connectMySQL();

    public void connectPostgres();

    public void connectSQLServer();

    public void connectSQLite();

    public void connectFirebird();

    public void closeConnection();

    public void openBlock(String tableName);

    public void insertField(String columnName, Object columnValue) throws SQLException;

    public int insertRow(String query) throws SQLException;

    public int closeBlock() throws java.lang.Exception;

    public boolean existTable(String tableName);

    public ResultSet updateField(String table, String where);

    public ResultSet select(String select);

    public ResultSet selectWhere(String table, String where);

    public Connection getConnection();

    public ResultSet getRs();

    public void initTransaction() throws SQLException;

    public void cancelTransaction() throws SQLException;

    public void confirmTransaction() throws SQLException;

    public void executeQuery(String sql) throws SQLException;

    public void disconnect();

    public void deleteRows(String tableName, String where) throws Exception;

    public String getSchema();

}
