package cat.quickdb.db.connection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionDBPostgre extends ConnectionDB {

    private String table;

    public ConnectionDBPostgre(String... dbParameters) {
        super(dbParameters);
        if(dbParameters.length > 5){
            this.schema = dbParameters[5];
        }else{
            this.schema = "public";
        }
    }

    private String convertToPostgre(String table) {
        return this.schema + ".\"" + table + "\"";
    }

    private String convertToPostgreComplex(String sql) {
        if(!sql.startsWith("CREATE")){
            String[] split = sql.split("FROM|WHERE|from|where");
            String temp = split[1];

            temp = temp.replaceAll("JOIN|join|=|ON|on|((\\w)+\\.(\\w)+)", "").trim();
            String[] tables = temp.split("(\\s)+");

            for (String s : tables) {
                sql = sql.replaceAll(s, this.schema + ".\"" + s + "\"");
            }
        }

        return sql;
    }

    @Override
    public void openBlock(String tableName) {
        this.table = tableName;
        super.openBlock(this.convertToPostgre(tableName));
    }

    @Override
    public int closeBlock() throws java.lang.Exception {
        int index = 0;
        try {
            if (rs != null) {
                rs.insertRow();
                rs.close();
                rs = statement.executeQuery("SELECT currval('" + this.table + "_seq');");
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
        return super.existTable(this.convertToPostgre(tableName));
    }

    @Override
    public ResultSet updateField(String table, String where) {
        return super.updateField(this.convertToPostgre(table), where);
    }

    @Override
    public ResultSet select(String select) {
        return super.select(this.convertToPostgreComplex(select));
    }

    @Override
    public ResultSet selectWhere(String table, String where) {
        return super.selectWhere(this.convertToPostgre(table), where);
    }

    @Override
    public void deleteRows(String tableName, String where) throws Exception {
        super.deleteRows(this.convertToPostgre(tableName), where);
    }

    @Override
    public void executeQuery(String sql) throws SQLException {
        super.executeQuery(this.convertToPostgreComplex(sql));
    }
}
