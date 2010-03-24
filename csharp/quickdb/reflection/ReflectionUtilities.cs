using System;
using System.Runtime.Remoting;
using System.Reflection;
using System.Collections;
using System.Collections.Generic;
using cat.quickdb.attribute;

namespace cat.quickdb.reflection
{
	
	public class ReflectionUtilities
	{
		
		public ReflectionUtilities()
		{
		}
		
		public List<string> isMany2Many(object obj)
		{
			List<string> manys = new List<string>();
			//Obtain the Table Name from this Object
			string table1 = this.readTableName(obj);
			
			PropertyInfo[] properties = obj.GetType().GetProperties();
			foreach(PropertyInfo prop in properties)
			{
				Attribute att = null;
				//Search if this field has the Column annotation
				Attribute[] attributes = (Attribute[]) prop.GetCustomAttributes(false);
				foreach(Attribute a in attributes)
				{
					if(a is	ColumnAttribute)
					{
						att = a;
					}
				}
				
				//Create an empty instance from the type of this field
				object result = this.emptyInstance(prop.PropertyType);
				
				if((result != null) && this.implementsCollection(obj, att))
				{
					
				}
			}
			
			return manys;
		}
		
		object obtainItemCollectionType(ICollection array, object obj, string field)
		{
			string className = "";
			object objCollec;
			if(array.Count != 0)
			{
				object[] objects = new object[array.Count];
				array.CopyTo(objects, 0);
				objCollec = objects[0];
			}
			else
			{
				PropertyInfo prop = obj.GetType().GetProperty(field);
				Attribute[] attributes = (Attribute[]) prop.GetCustomAttributes(false);
				foreach(Attribute a in attributes)
				{
					if(a is ColumnAttribute)
					{
						className = ((ColumnAttribute) a).CollectionClass;
					}
				}
				
				string path = obj.GetType().Namespace;
				
				if(className.Length == 0)
				{
					className = path + prop.Name;
				}
				else if(className.Contains("."))
				{
					className = path + className;
				}
				
				objCollec = this.emptyInstance(className, obj.GetType());
			}
			
			return objCollec;
		}
		
		public string readTableName(object obj)
		{
			Attribute[] attributes = (Attribute[]) obj.GetType().GetCustomAttributes(false);
			string entityName = obj.GetType().Name;
			for(int i = 0; i < attributes.Length; i++)
			{
				if((attributes[i] is TableAttribute) &&
				   (((TableAttribute) attributes[i]).Name.Length != 0))
				{
					entityName = ((TableAttribute) attributes[i]).Name;
				}
			}
			
			return entityName;
		}
		
		public object emptyInstance(params object[] type)
		{
			object result = null;
			try
			{
				if(this.isInstanceOfDate(type[0].GetType()))
				{
					result = DateTime.Now;
				}
				else if(type.Length == 1)
				{
					result = Activator.CreateInstance(((Type) type[0]));
				}
				else
				{
					result = Activator.CreateInstance(((Type)type[1]).GetType().Assembly.FullName, type[0].ToString());
				}
			}
			catch(Exception e)
			{
				return null;
			}
			
			return result;
		}
		
		private bool isInstanceOfDate(Type t)
		{
			if(t.IsInstanceOfType(DateTime.Now))
			{
				return true;
			}
			
			return false;
		}
		
		bool implementsCollection(object obj, Attribute att)
		{
			bool collec = false;
			
			if(att == null)
			{
				if((obj.GetType().GetInterface("IList") != null) ||
				   (obj.GetType().GetInterface("ICollection") != null))
				{
					collec = true;
				}
			}
			else
			{
				collec = (((ColumnAttribute) att).Type == Properties.TYPES.COLLECTION);
			}
			
			return collec;
		}
		
	}
}
