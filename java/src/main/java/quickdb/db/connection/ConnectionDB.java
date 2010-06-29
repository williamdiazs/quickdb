package quickdb.db.connection;

import java.sql.*;

/**
 *
 * @author Diego Sarmentero
 */
public class ConnectionDB {

    protected Connection connection = null;
    protected Statement statement = null;
    protected ResultSet rs = null;
    private boolean tryAgain = true;
    //Connection Data
    private String host;
    private String port;
    private String nameDB;
    private String username;
    private String password;
    private String url;
    private String driver;
    protected String schema;

    public ConnectionDB(String... dbParameters) {
        this.host = dbParameters[0];
        this.port = dbParameters[1];
        this.nameDB = dbParameters[2];
        this.username = dbParameters[3];
        this.password = dbParameters[4];
    }

    private void connect() {
        try {
            Class.forName(this.driver);
            connection = DriverManager.getConnection(url, username, password);
            this.connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver couldn't be loaded. " +
                    e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error trying to connect. " +
                    e.getMessage());
        }
    }

    public void connectMySQL() {
        this.driver = "com.mysql.jdbc.Driver";
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + nameDB;
        this.connect();
    }

    public void connectPostgres() {
        this.driver = "org.postgresql.Driver";
        this.url = "jdbc:postgresql://" + host + ":" + port + "/" + nameDB;
        this.connect();
    }

    public void connectSQLServer() {
        this.driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        this.url = "jdbc:microsoft:sqlserver://" + host + ":" + port + ";" +
                "database=" + nameDB;
        this.connect();
    }

    public void connectSQLite() {
        this.driver = "org.sqlite.JDBC";
        this.url = "jdbc:sqlite:" + nameDB + ".db";
        this.connect();
    }

    public void connectFirebird() {
        this.driver = "org.firebirdsql.jdbc.FBDriver";
        this.url = "jdbc:firebirdsql:" + host + "/" + port + ":" + nameDB;
        this.connect();
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.err.println(
                    "'cerrarConexion()' Error trying to close connection. " +
                    e.getMessage());
        }
    }

    public void openBlock(String tableName) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery("SELECT * FROM " + tableName + ";");
            rs.moveToInsertRow();
            tryAgain = true;
        } catch (SQLException e) {
            if (tryAgain) {
                this.connect();
                tryAgain = false;
                this.openBlock(tableName);
            }
        }
    }

    public void insertField(String columnName, Object columnValue) throws SQLException {
        try {
            rs.updateObject(columnName, columnValue);
        } catch (SQLException e) {
            throw e;
        } catch (NullPointerException nullEx) {
            SQLException sqlEx = new SQLException();
            throw sqlEx;
        }
    }

    public int insertRow(String query) throws SQLException {
        int index = 0;
        try {
            statement = connection.createStatement();
            statement.execute(query + ";");
            rs = statement.getGeneratedKeys();
            rs.next();
            index = rs.getInt(1);
            statement.close();
        } catch (SQLException e) {
            throw e;
        } catch (NullPointerException nullEx) {
            SQLException sqlEx = new SQLException();
            throw sqlEx;
        }

        return index;
    }

    public int closeBlock() throws java.lang.Exception {
        int index = 0;
        try {
            if (rs != null) {
                rs.insertRow();
                rs.close();
                rs = statement.executeQuery("SELECT LAST_INSERT_ID();");
                rs.next();
                index = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            rs.close();
            statement.close();
        }

        return index;
    }

    public boolean existTable(String tableName) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1;");
            return rs.next();
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return false;
    }

    public ResultSet updateField(String table, String where) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery("SELECT * FROM " + table + " WHERE " + where + ";");
            tryAgain = true;
        } catch (SQLException e) {
            if (tryAgain) {
                this.connect();
                tryAgain = false;
                return this.updateField(table, where);
            } else {
                System.err.println("Error trying to update field. " + e.getMessage());
            }
        }
        return rs;
    }

    public ResultSet select(String select) {
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(select + ";");
            tryAgain = true;
            return rs;
        } catch (SQLException e) {
            if (tryAgain) {
                this.connect();
                tryAgain = false;
                return this.select(select);
            } else {
                System.err.println("'Select()'. " +
                        e.getMessage());
            }
        }
        return null;
    }

    public ResultSet selectWhere(String table, String where) {
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM " + table + " WHERE " + where + ";");
            tryAgain = true;
            return rs;
        } catch (SQLException e) {
            if (tryAgain) {
                this.connect();
                tryAgain = false;
                return this.selectWhere(table, where);
            } else {
                System.err.println("'SelectWhere()'. " +
                        e.getMessage());
            }
        }
        return null;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public ResultSet getRs() {
        return this.rs;
    }

    public void initTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public void cancelTransaction() throws SQLException {
        connection.rollback();
    }

    public void confirmTransaction() throws SQLException {
        connection.commit();
    }

    public void executeQuery(String sql) throws SQLException {
        try {
            statement = connection.createStatement();

            statement.executeUpdate(sql + ";");

            statement.close();
            tryAgain = true;
        } catch (Exception e) {
            if (tryAgain) {
                this.connect();
                tryAgain = false;
                this.executeQuery(sql);
            } else {
                throw new SQLException();
            }
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteRows(String tableName, String where) throws Exception {
        // Creamos una statement SQL
        statement = connection.createStatement();

        // Ejecutamos la consulta
        statement.executeUpdate("DELETE FROM " + tableName + " WHERE " + where + ";");

        statement.close();
    }

    public String getSchema(){
        return this.schema;
    }

}
