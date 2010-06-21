using System;

namespace cat.quickdb.exception
{
	
	public class QueryException : Exception
	{
		
		public QueryException() : base(){}
		
		public QueryException(string message) : base(message){}
	}
}
