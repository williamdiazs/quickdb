using System;

namespace cat.quickdb.attribute
{
	
	[AttributeUsage(AttributeTargets.Property)]
	public class ColumnAttribute : Attribute
	{
		private string name;
		private Properties.TYPES type;
		private string getter;
		private string setter;
		private string table;
		private string collectionClass;
		private bool ignore;
		
		public ColumnAttribute()
		{
			this.name = "";
			this.type = Properties.TYPES.PRIMITIVE;
			this.getter = "";
			this.setter = "";
			this.table = "";
			this.collectionClass = "";
			this.ignore = false;
		}
		
		public string Name
		{
			get
			{
				return this.name;
			}
			set
			{
				this.name = value;
			}
		}
		
		public Properties.TYPES Type
		{
			get
			{
				return this.type;
			}
			set
			{
				this.type = value;
			}
		}
		
		public string Getter
		{
			get
			{
				return this.getter;
			}
			set
			{
				this.getter = value;
			}
		}
		
		public string Setter
		{
			get
			{
				return this.setter;
			}
			set
			{
				this.setter = value;
			}
		}
		
		public string Table
		{
			get
			{
				return this.table;
			}
			set
			{
				this.table = value;
			}
		}
		
		public bool Ignore
		{
			get{return this.ignore;}
			set{this.ignore = value;}
		}
		
		public string CollectionClass
		{
			get{return this.collectionClass;}
			set{this.collectionClass = value;}
		}
		
	}
	
}