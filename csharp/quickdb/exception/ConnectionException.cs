using System;

namespace cat.quickdb.exception
{	
	
	public class ConnectionException : Exception
	{
		
		public ConnectionException() : base(){}
		
		public ConnectionException(string message) : base(message){}
	}
}
