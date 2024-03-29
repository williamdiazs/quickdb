# -*- coding: utf-8 -*-
"""
@autor Emilio Ramirez
"""
class ConnectionDB:
    """This class manage the connection with the Database"""

    def __init__(self, host='localhost', port=3306, namedb='',
                    user='', password='', scheme=''):
        """ Constructor of class
        @param String host of the dbms
        @param String name of the database
        @param String user of the database
        @param String password of the user on database
        @param String scheme (only in postgres)"""
        self._host = host
        self._port = port
        self._namedb = namedb
        self._user = user
        self._password = password
        self._scheme = scheme
        self._tryAgain = True
        self._connection = None
        self._cursor = None

    def connect_mysql(self):
        """Provide the connection with MySQL db and set the cursor """
        try:
            self.dbapi = __import__('MySQLdb')
            self._connection = self.dbapi.connect(host=self._host,
                            port=self._port, user=self._user,
                            passwd=self._password, db=self._namedb)
            self._cursor = self._connection.cursor()
        except (ImportError):
            print "Driver MySQL couldn't be loaded."
        except (self.dbapi.Error), e:
            print "Error trying to connect:", e

    def close_connection(self):
        """Close the cursor and the connection's DBMS"""
        try:
            self._cursor.close()
            self._connection.close()
        except (Exception), e:
            print "Error trying close the cursor or connection with db", e

    def insert_row(self, tablename, data):
        """Insert a row in a table
        @param String tablename
        @param List data"""
        try:
            self._cursor.execute("INSERT INTO "+ tablename + "(" + ",".join(data[0]) + ")"\
            " VALUES (" + ("%s,"*len(data[1]))[:-1] + ")", data[1])
        except (Exception), e:
            print "insert_row error:",e

    def update(self, tablenames):
        """Update a row in a table
        @param String tablename"""
        pass

    def delete(self, tablename, where):
        """Delete a row in a table
        @param String tablename
        @param String where"""
        try:
            self._cursor.execute("DELETE FROM "+ tablename +" WHERE "+ where)
        except (Exception), e:
            print "delete error:",e

    def select(self, select):
        """Query select with the complete string query
        @param String select
        return check _cursor methods fetchone, fetchmany o fetchall"""
        try:
            self._cursor.execute(select)
        except (Exception), e:
            print "select error:",e

    def select_where(self, tablename, where):
        """Query select with only where clausule
        @param String tablename
        @param String where
        return check _cursor methods fetchone, fetchmany o fetchall"""
        try:
            self._cursor.execute("SELECT * FROM " + tablename + " WHERE " + where)
        except (Exception), e:
            print "select_where error:",e

    def commit(self):
        """Commit the transactions"""
        try:
            self._connection.commit()
        except (Exception), e:
            print "commit error:",e

if __name__ == '__main__':

    import datetime,random
    #create the connection
    conn_db = ConnectionDB(namedb='quickdb', user='quickdb', password='quickdb')
    conn_db.connect_mysql()

    #some data test
    nombres = ["pab","tsb","aed","pdp","gda"]
    col = ['nombre','fecha', 'salary', 'date']
    #~ import datetime
    val = [ nombres[random.randint(0, 4)], random.randint(1000, 2000), 50.69, datetime.date.today()]
    data = [col, val]

    #test insert_row method
    #conn_db.insert_row('libro', data)

    #test the select method
    #~ conn_db.select("SELECT * FROM libro")
    #~ for tupla in conn_db._cursor.fetchall():
        #~ print tupla

    #test the select_where method
    #~ conn_db.select_where("libro", "nombre='aed'")
    #~ for tupla in conn_db._cursor.fetchall():
        #~ print tupla

    #test delete method
    #conn_db.delete('libro', 'nombre="analisis"')

    #test the commit method
    conn_db.commit()

    #test the close_connection method
    conn_db.close_connection()

