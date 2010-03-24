package cat.quickdb.db.dbms.firebird;

import java.util.HashMap;
import java.sql.Date;

public class DataType {

    private HashMap<Class, String> firebirdDataType;

    public DataType() {
        this.firebirdDataType = new HashMap<Class, String>();
        this.firebirdDataType.put(Integer.class, "integer");
        this.firebirdDataType.put(Double.class, "double precision");
        this.firebirdDataType.put(Float.class, "float");
        this.firebirdDataType.put(String.class, "varchar");
        this.firebirdDataType.put(Boolean.class, "boolean");
        this.firebirdDataType.put(Long.class, "bigint");
        this.firebirdDataType.put(Short.class, "smallint");
        this.firebirdDataType.put(Date.class, "date");
    }

    public String getDataType(Class clazz, int length) {
        if (clazz == String.class) {
            length = 150;
        }else if((clazz == Integer.class) && (length == 1)) {
            length = 11;
        }

        if (clazz == String.class) {
            return this.firebirdDataType.get(clazz).concat("(" + length + ")");
        } else {
            return this.firebirdDataType.get(clazz);
        }
    }
}
