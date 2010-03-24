package cat.quickdb.db.dbms.mysql;

import java.util.HashMap;
import java.sql.Date;

public class DataType {

    private HashMap<Class, String> mysqlDataType;

    public DataType() {
        //MYSQL
        this.mysqlDataType = new HashMap<Class, String>();
        this.mysqlDataType.put(Integer.class, "INTEGER");
        this.mysqlDataType.put(Double.class, "DOUBLE");
        this.mysqlDataType.put(Float.class, "FLOAT");
        this.mysqlDataType.put(String.class, "VARCHAR");
        this.mysqlDataType.put(Boolean.class, "TINYINT(1)");
        this.mysqlDataType.put(Long.class, "BIGINT");
        this.mysqlDataType.put(Short.class, "SMALLINT");
        this.mysqlDataType.put(Date.class, "DATE");
    }

    public String getDataType(Class clazz, int length) {
        if (clazz == String.class) {
            length = 150;
        }else if((clazz == Integer.class) && (length == 1)) {
            length = 11;
        }

        if (clazz == Boolean.class || clazz == Date.class ||
                clazz == Double.class) {
            return this.mysqlDataType.get(clazz);
        } else {
            return this.mysqlDataType.get(clazz).concat("(" + length + ")");
        }
    }
}
