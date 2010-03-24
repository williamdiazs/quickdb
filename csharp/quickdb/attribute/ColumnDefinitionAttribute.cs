using System;

namespace cat.quickdb.attribute
{
	
	[AttributeUsage(AttributeTargets.Field)]
	public class ColumnDefinitionAttribute : Attribute
	{
		private Definition.DATATYPE type;
		private int length;
		private bool notNull;
		private string defaultValue;
		private bool autoIncrement;
		private bool unique;
		private bool primary;
		private Definition.COLUMN_FORMAT format;
		private Definition.STORAGE storage;
		
		public ColumnDefinitionAttribute()
		{
			this.type = Definition.DATATYPE.VARCHAR;
			this.length = -1;
			this.notNull = true;
			this.defaultValue = "";
			this.autoIncrement = false;
			this.unique = false;
			this.primary = false;
			this.format = Definition.COLUMN_FORMAT.DEFAULT;
			this.storage = Definition.STORAGE.DEFAULT;
		}
		
		public Definition.DATATYPE Type
		{
			get{return this.type;}
			set{this.type = value;}
		}
		
		public int Length
		{
			get{return this.length;}
			set{this.length = value;}
		}
		
		public bool NotNull
		{
			get{return this.notNull;}
			set{this.notNull = value;}
		}
		
		public string DefaultValue
		{
			get{return this.defaultValue;}
			set{this.defaultValue = value;}
		}
		
		public bool AutoIncrement
		{
			get{return this.autoIncrement;}
			set{this.autoIncrement = value;}
		}
		
		public bool Unique
		{
			get{return this.unique;}
			set{this.unique = value;}
		}
		
		public bool Primary
		{
			get{return this.primary;}
			set{this.primary = value;}
		}
		
		public Definition.COLUMN_FORMAT Format
		{
			get{return this.format;}
			set{this.format = value;}
		}
		
		public Definition.STORAGE Storage
		{
			get{return this.storage;}
			set{this.storage = value;}
		}
		
	}
	
}