using System;

namespace cat.quickdb.attribute
{
	
	[AttributeUsage(AttributeTargets.Field)]
	public class TableAttribute : Attribute
	{
		private string name;
		private bool cache;
		private bool cacheUpdate;
		private string[] before;
		private string[] after;
		
		public TableAttribute(){}
		
		public string Name
		{
			get{return this.name;}
			set{this.name = value;}
		}
	}
}
