using System;
using System.Reflection;
using System.Collections;
using System.Collections.Generic;
using cat.quickdb.db;
using cat.quickdb.attribute;

namespace cat.quickdb.reflection
{
	
	public class EntityManager
	{
		
		public enum OPERATION
		{
			SAVE, MODIFY, DELETE, OBTAIN, OTHER
		}
		private Stack<string> primaryKey;
		private Stack<int> primaryKeyValue;
		private Stack<ArrayList> collection;
		private Stack<string> nameCollection;
		private List<string> manyRestore;
		private bool dropDown;
		private bool hasParent;
		private Object originalChild;
		private int inheritance;
		private ReflectionUtilities reflec;
		
		public EntityManager()
		{
			this.reflec = new ReflectionUtilities();
			this.primaryKey = new Stack<string>();
			this.collection = new Stack<ArrayList>();
			this.dropDown = true;
			this.nameCollection = new Stack<string>();
			this.primaryKeyValue = new Stack<int>();
			this.hasParent = false;
			this.inheritance = 0;
			this.manyRestore = new List<string>();
		}
		
		/// <summary>
		/// The ArrayList returned contain in the firste row the name of the Table
		/// and then contain an String Array (Object[]) in the other rows
		/// representing the name of the field and the data, and finally if exist a
		/// sql statement or an empty string if there isn't a sql statement
		/// </summary>
		/// <param name="admin">
		/// A <see cref="AdminBase"/>
		/// </param>
		/// <param name="obj">
		/// A <see cref="System.Object"/>
		/// </param>
		/// <param name="oper">
		/// A <see cref="OPERATION"/>
		/// </param>
		/// <returns>
		/// A <see cref="ArrayList"/>
		/// </returns>
		public ArrayList entity2Array(AdminBase admin, object obj, OPERATION oper)
		{
			ArrayList array = new ArrayList();
			bool sql = true;
			bool ignore = false;
			string statement = "";
			
			array.Add(this.readClassName(obj));
			bool tempParent = this.hasParent;
			this.hasParent = false;
			
			this.primaryKey.Push("id");
			PropertyInfo[] properties = obj.GetType().GetProperties();
			for(int i = 0; i < properties.Length; i++)
			{
				object[] objs = new object[2];
				string field = properties[i].Name;
				objs[0] = field;
				Attribute att = null;
				
				Attribute[] attributes = (Attribute[]) properties[i].GetCustomAttributes(false);
				foreach(Attribute a in attributes)
				{
					if(a is ColumnAttribute)
					{
						if(((ColumnAttribute) a).Name.Length != 0)
						{
							objs[0] = ((ColumnAttribute) a).Name;
						}
						
						ignore = ((ColumnAttribute) a).Ignore;
						att = a;
						if(((ColumnAttribute) a).Type == Properties.TYPES.PRIMARYKEY)
						{
							this.primaryKey.Pop();
							this.primaryKey.Push(objs[0].ToString());
							continue;
						}
					}
					else
					{
						continue;
					}
				}
				
				if(ignore)
				{
					ignore = false;
					continue;
				}
				
				if(sql)
				{
					if(oper == OPERATION.MODIFY ||
					   oper == OPERATION.DELETE)
					{
						string nameF = this.primaryKey.Peek();
						int valueId = (int) properties[i].GetValue(obj, new object[0]);
						statement = nameF + "=" + valueId;
					}
					else
					{
						statement = this.primaryKey.Peek() + " > 0";
					}
					sql = false;
				}
				
				object result = properties[i].GetValue(obj, new object[0]);
				
			}
			
			return array;
		}
		
		private string readClassName(object obj)
		{
			this.hasParent = false;
			Attribute[] entity = Attribute.GetCustomAttributes(obj.GetType());
			string entityName = obj.GetType().Name;
			
			for(int i = 0; i < entity.Length; i++)
			{
				if((entity[i] is TableAttribute) && 
				   (((TableAttribute) entity[i]).Name.Length != 0))
				{
					entityName = ((TableAttribute) entity[i]).Name;
				}
				else if(entity[i] is ParentAttribute)
				{
					this.hasParent = true;
				}
			}
			
			if((entity.Length == 0) &&
			   (obj.GetType().BaseType.Namespace == obj.GetType().Namespace))
			{
				this.hasParent = true;
			}
			
			return entityName;
		}
		
	}
}
