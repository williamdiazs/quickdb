using System;

namespace cat.quickdb.attribute
{
	
	[AttributeUsage(AttributeTargets.Field)]
	public class TableAttribute : Attribute
	{
		private string name;
		
		public TableAttribute(){}
		
		public string Name
		{
			get{return this.name;}
			set{this.name = value;}
		}
	}
}
