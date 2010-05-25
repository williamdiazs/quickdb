package quickdb.db.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Diego Sarmentero
 */
public class ConnectionDBFirebird extends ConnectionDB{

    private String table;

    public ConnectionDBFirebird(String... dbParameters){
        super(dbParameters);
    }

    @Override
    public void openBlock(String tableName) {
        this.table = tableName;
        super.openBlock(tableName);
    }

    @Override
    public int closeBlock() throws java.lang.Exception {
        int index = 0;
        try {
            if (rs != null) {
                rs.insertRow();
                rs.close();
                rs = statement.executeQuery("select gen_id(" +
                        "GEN_"+this.table+"_ID, 0) NuevoID from rdb$database;");
                rs.next();
                index = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            this.table = "";
            rs.close();
            statement.close();
        }

        return index;
    }

    @Override
    public boolean existTable(String tableName) {
        try {
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery("SELECT FIRST 1 * FROM " + tableName + ";");
            return rs.next();
        } catch (SQLException e) {
            //System.err.println(e.getMessage());
        }
        return false;
    }

}
