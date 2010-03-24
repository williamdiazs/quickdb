using System;
using System.Collections;

namespace cat.quickdb.attribute
{
	
	public class Definition
	{
		
		public enum DATATYPE {
	        BIT, TINYINT, SMALLINT, MEDIUMINT, INT, INTEGER,
	        BIGINT, REAL, DOUBLE, FLOAT, DECIMAL, NUMERIC, DATE, TIME, TIMESTAMP,
	        DATETIME, YEAR, CHAR, VARCHAR, BINARY, VARBINARY, TINYBLOB, BLOB,
	        MEDIUMBLOB, LONGBLOB, TINYTEXT, TEXT, MEDIUMTEXT, LONGTEXT, ENUM, SET
	    };
		
		public enum COLUMN_FORMAT {
	        FIXED, DYNAMIC, DEFAULT
	    };
		
		public enum STORAGE {
	        DISK, MEMORY, DEFAULT
	    };
		
		private Hashtable typeValue;
		private Hashtable formatValue;
    	private Hashtable storageValue;
		
		public Definition()
		{
			this.init();
		}
		
		private void init() {
	        this.typeValue = new Hashtable();
	        this.typeValue.Add(DATATYPE.BIT, "BIT");
	        this.typeValue.Add(DATATYPE.TINYINT, "TINYINT");
	        this.typeValue.Add(DATATYPE.SMALLINT, "SMALLINT");
	        this.typeValue.Add(DATATYPE.MEDIUMINT, "MEDIUMINT");
	        this.typeValue.Add(DATATYPE.INT, "INT");
	        this.typeValue.Add(DATATYPE.INTEGER, "INTEGER");
	        this.typeValue.Add(DATATYPE.BIGINT, "BIGINT");
	        this.typeValue.Add(DATATYPE.REAL, "REAL");
	        this.typeValue.Add(DATATYPE.DOUBLE, "DOUBLE");
	        this.typeValue.Add(DATATYPE.FLOAT, "FLOAT");
	        this.typeValue.Add(DATATYPE.DECIMAL, "DECIMAL");
	        this.typeValue.Add(DATATYPE.NUMERIC, "NUMERIC");
	        this.typeValue.Add(DATATYPE.DATE, "DATE");
	        this.typeValue.Add(DATATYPE.TIME, "TIME");
	        this.typeValue.Add(DATATYPE.TIMESTAMP, "TIMESTAMP");
	        this.typeValue.Add(DATATYPE.DATETIME, "DATETIME");
	        this.typeValue.Add(DATATYPE.YEAR, "YEAR");
	        this.typeValue.Add(DATATYPE.CHAR, "CHAR");
	        this.typeValue.Add(DATATYPE.VARCHAR, "VARCHAR");
	        this.typeValue.Add(DATATYPE.BINARY, "BINARY");
	        this.typeValue.Add(DATATYPE.VARBINARY, "VARBINARY");
	        this.typeValue.Add(DATATYPE.TINYBLOB, "TINYBLOB");
	        this.typeValue.Add(DATATYPE.BLOB, "BLOB");
	        this.typeValue.Add(DATATYPE.MEDIUMBLOB, "MEDIUMBLOB");
	        this.typeValue.Add(DATATYPE.LONGBLOB, "LONGBLOB");
	        this.typeValue.Add(DATATYPE.TINYTEXT, "TINYTEXT");
	        this.typeValue.Add(DATATYPE.TEXT, "TEXT");
	        this.typeValue.Add(DATATYPE.MEDIUMTEXT, "MEDIUMTEXT");
	        this.typeValue.Add(DATATYPE.LONGTEXT, "LONGTEXT");
	        this.typeValue.Add(DATATYPE.ENUM, "ENUM");
	        this.typeValue.Add(DATATYPE.SET, "SET");
	
	        this.formatValue = new Hashtable();
	        this.formatValue.Add(COLUMN_FORMAT.DEFAULT, "DEFAULT");
	        this.formatValue.Add(COLUMN_FORMAT.DYNAMIC, "DYNAMIC");
	        this.formatValue.Add(COLUMN_FORMAT.FIXED, "FIXED");
	
	        this.storageValue = new Hashtable();
	        this.storageValue.Add(STORAGE.DEFAULT, "DEFAULT");
	        this.storageValue.Add(STORAGE.DISK, "DISK");
	        this.storageValue.Add(STORAGE.MEMORY, "MEMORY");
	    }
		
		public String obtainDataType(DATATYPE type) {
	        return this.typeValue[type].ToString();
	    }
	
	    public String obtainColumnFormat(COLUMN_FORMAT format) {
	        return this.formatValue[format].ToString();
	    }
	
	    public String obtainStorage(STORAGE store) {
	        return this.storageValue[store].ToString();
	    }
		
	}
}
