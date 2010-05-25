package quickdb.db.dbms.postgre;

import java.util.HashMap;
import java.sql.Date;

/**
 *
 * @author Diego Sarmentero
 */
public class DataTypePostgre {

    private HashMap<Class, String> mysqlDataType;

    public DataTypePostgre() {
        this.mysqlDataType = new HashMap<Class, String>();
        this.mysqlDataType.put(Integer.class, "integer");
        this.mysqlDataType.put(Double.class, "double precision");
        this.mysqlDataType.put(Float.class, "real");
        this.mysqlDataType.put(String.class, "character varying");
        this.mysqlDataType.put(Boolean.class, "boolean");
        this.mysqlDataType.put(Long.class, "bigint");
        this.mysqlDataType.put(Short.class, "smallint");
        this.mysqlDataType.put(Date.class, "date");
    }

    public String getDataType(Class clazz, int length) {
        if (clazz == String.class) {
            length = 150;
        }else if((clazz == Integer.class) && (length == 1)) {
            length = 11;
        }

        if (clazz == String.class) {
            return this.mysqlDataType.get(clazz).concat("(" + length + ")");
        } else {
            return this.mysqlDataType.get(clazz);
        }
    }
}
