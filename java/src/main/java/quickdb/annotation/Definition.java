package quickdb.annotation;

import quickdb.db.AdminBase;
import java.util.HashMap;

/**
 *
 * @author Diego Sarmentero
 */
public class Definition {

    public enum DATATYPE {
        BIT, BOOLEAN, SMALLINT, INT, INTEGER, BIGINT, REAL, DOUBLE, FLOAT,
        DECIMAL, NUMERIC, DATE, TIME, TIMESTAMP, DATETIME, CHAR,
        VARCHAR, BINARY, VARBINARY, TEXT
    };

    public enum COLUMN_FORMAT {

        FIXED, DYNAMIC, DEFAULT
    };

    public enum STORAGE {

        DISK, MEMORY, DEFAULT
    };
    private HashMap<DATATYPE, String> typeValue;
    private HashMap<COLUMN_FORMAT, String> formatValue;
    private HashMap<STORAGE, String> storageValue;

    public Definition(AdminBase.DATABASE db) {
        this.init(db);
    }

    private void init(AdminBase.DATABASE db) {
        this.typeValue = new HashMap<Definition.DATATYPE, String>();
        switch(db){
            case POSTGRES:
                this.typeValue.put(DATATYPE.BIT, "bit");
                this.typeValue.put(DATATYPE.BOOLEAN, "boolean");
                this.typeValue.put(DATATYPE.SMALLINT, "SMALLINT");
                this.typeValue.put(DATATYPE.INT, "integer");
                this.typeValue.put(DATATYPE.INTEGER, "integer");
                this.typeValue.put(DATATYPE.BIGINT, "bigint");
                this.typeValue.put(DATATYPE.REAL, "real");
                this.typeValue.put(DATATYPE.DOUBLE, "double precision");
                this.typeValue.put(DATATYPE.FLOAT, "real");
                this.typeValue.put(DATATYPE.DECIMAL, "decimal");
                this.typeValue.put(DATATYPE.NUMERIC, "numeric");
                this.typeValue.put(DATATYPE.DATE, "date");
                this.typeValue.put(DATATYPE.TIME, "time");
                this.typeValue.put(DATATYPE.TIMESTAMP, "timestamp");
                this.typeValue.put(DATATYPE.DATETIME, "timestamp");
                this.typeValue.put(DATATYPE.CHAR, "character");
                this.typeValue.put(DATATYPE.VARCHAR, "character varying");
                this.typeValue.put(DATATYPE.BINARY, "bytea");
                this.typeValue.put(DATATYPE.VARBINARY, "bit varying");
                this.typeValue.put(DATATYPE.TEXT, "text");
                break;
            case FIREBIRD:
                this.typeValue.put(DATATYPE.BIT, "bit");
                this.typeValue.put(DATATYPE.BOOLEAN, "boolean");
                this.typeValue.put(DATATYPE.SMALLINT, "smallint");
                this.typeValue.put(DATATYPE.INT, "int");
                this.typeValue.put(DATATYPE.INTEGER, "integer");
                this.typeValue.put(DATATYPE.BIGINT, "bigint");
                this.typeValue.put(DATATYPE.REAL, "real");
                this.typeValue.put(DATATYPE.DOUBLE, "double precision");
                this.typeValue.put(DATATYPE.FLOAT, "float");
                this.typeValue.put(DATATYPE.DECIMAL, "decimal");
                this.typeValue.put(DATATYPE.NUMERIC, "numeric");
                this.typeValue.put(DATATYPE.DATE, "date");
                this.typeValue.put(DATATYPE.TIME, "time");
                this.typeValue.put(DATATYPE.TIMESTAMP, "timestamp");
                this.typeValue.put(DATATYPE.DATETIME, "timestamp");
                this.typeValue.put(DATATYPE.CHAR, "char");
                this.typeValue.put(DATATYPE.VARCHAR, "varchar");
                this.typeValue.put(DATATYPE.TEXT, "nchar");
                break;
            default:
                this.typeValue.put(DATATYPE.BIT, "BIT");
                this.typeValue.put(DATATYPE.BOOLEAN, "TINYINT");
                this.typeValue.put(DATATYPE.SMALLINT, "SMALLINT");
                this.typeValue.put(DATATYPE.INT, "INT");
                this.typeValue.put(DATATYPE.INTEGER, "INTEGER");
                this.typeValue.put(DATATYPE.BIGINT, "BIGINT");
                this.typeValue.put(DATATYPE.REAL, "REAL");
                this.typeValue.put(DATATYPE.DOUBLE, "DOUBLE");
                this.typeValue.put(DATATYPE.FLOAT, "FLOAT");
                this.typeValue.put(DATATYPE.DECIMAL, "DECIMAL");
                this.typeValue.put(DATATYPE.NUMERIC, "NUMERIC");
                this.typeValue.put(DATATYPE.DATE, "DATE");
                this.typeValue.put(DATATYPE.TIME, "TIME");
                this.typeValue.put(DATATYPE.TIMESTAMP, "TIMESTAMP");
                this.typeValue.put(DATATYPE.DATETIME, "DATETIME");
                this.typeValue.put(DATATYPE.CHAR, "CHAR");
                this.typeValue.put(DATATYPE.VARCHAR, "VARCHAR");
                this.typeValue.put(DATATYPE.BINARY, "BINARY");
                this.typeValue.put(DATATYPE.VARBINARY, "VARBINARY");
                this.typeValue.put(DATATYPE.TEXT, "TEXT");
                break;
        }

        this.formatValue = new HashMap<COLUMN_FORMAT, String>();
        this.formatValue.put(COLUMN_FORMAT.DEFAULT, "DEFAULT");
        this.formatValue.put(COLUMN_FORMAT.DYNAMIC, "DYNAMIC");
        this.formatValue.put(COLUMN_FORMAT.FIXED, "FIXED");

        this.storageValue = new HashMap<STORAGE, String>();
        this.storageValue.put(STORAGE.DEFAULT, "DEFAULT");
        this.storageValue.put(STORAGE.DISK, "DISK");
        this.storageValue.put(STORAGE.MEMORY, "MEMORY");
    }

    public String obtainDataType(DATATYPE type) {
        return this.typeValue.get(type);
    }

    public String obtainColumnFormat(COLUMN_FORMAT format) {
        return this.formatValue.get(format);
    }

    public String obtainStorage(STORAGE store) {
        return this.storageValue.get(store);
    }
}