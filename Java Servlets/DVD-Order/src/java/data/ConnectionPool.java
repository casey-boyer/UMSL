package data;

/*Import the java.sql.*; package to use JDBC classes.
  Import javax.sql.DataSource interface to use the
  Jakarta-Commons connection pool to store the database connections.
  Import the javax.naming.IntialContext class to retrieve a DataSoruce
  object.
  Import the javax.naming.NamingException to catch a NamingException object
  if the DataSource object cannot be initialized.
*/
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionPool {

    /*The instance variables of the ConnectionPool object, which hold the
    ConnectionPool and DataSource objects. They are private so they
    cannot be accessed outside the class, and they are static so they
    only change with one instance of the ConnectionPool object. They
    are originally initialized to null, as they will be initialized in
    the ConnectionPool() constructor.*/
    private static ConnectionPool pool = null;
    private static DataSource dataSource = null;

    /*The constructor for the ConnectionPool() class. The constructor is private,
    so only a single instance of the ConnectionPool class may be created.
    Within the constructor, an InitialContext object is created and used to
    return a DataSource object. This is done by using the lookup() method of
    the InitialContext object, and passing the String which includes the database
    named specified in the name attribute in the context.xml file; then, the result
    is converted to a DataSource object, and the dataSource object of the ConnectionPool
    instance is initialized. If the lookup() method fails to locate the database and
    the dataSource object is not initialized, the catch() block catches the NamingException
    object error.*/
    private ConnectionPool() {
        try {
            //Create an InitialContext object
            InitialContext ic = new InitialContext();
            //Initialize the dataSource object by locating the database, which is
            //specified in the name attribute in the context.xml file
            dataSource = (DataSource) ic.lookup("java:/comp/env/jdbc/dvd_db");
        } catch (NamingException e) {
            //If the lookup() method fails, catch the NamingException exception object
            System.out.println("Naming exception error" + e);
        }
    }

    /*This method returns a reference to the ConnectionPool object.
    The synchronized keyword ensures that only one thread can invoke this method
    at any given time. Additionally, the static keyword ensures only one instance
    of the ConnectionPool object can be created.
    If the ConnectionPool object exists, a reference to the ConnectionPool
    object is returned. If it does not exist, the method creates a ConnectionPool
    object by invoking the ConnectionPool constructor, and returns the
    reference to the connectionPool.*/
    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            //If the ConnectionPool object does not exist, invoke the private
            //constructor and return the reference to it
            pool = new ConnectionPool();
        }
        //Return the reference to the ConnectionPool instance
        return pool;
    }

    /*This method returns a Connection object to access the database.
    This method is invoked once the connection pool is created.
    If the Connection object cannot be returned, the method catches the
    SQLException exception object, prints the corresponding error, and 
    returns null.*/
    public Connection getConnection() {
        try {
            //Return a Connection object by using the DataSource.getConnection()
            //method of the dataSource object
            return dataSource.getConnection();
        } catch (SQLException e) {
            //If the Connection object cannot be retrieved, catch the SQLException
            //exception object, print the corresponding error message,
            //and return null
            System.out.println("Could not retrieve the Connection object: " + e);
            return null;
        }
    }

    /*This method closes the Connection object passed as an argument, which
    returns the connection to the connection pool.
    If the close() method of the Connection object throws an exception, the
    SQLException is caught and the corresponding error message is printed.*/
    public void freeConnection(Connection c) {
        try {
            //Close the connection of the Connection object passed to the method
            c.close();
        } catch (SQLException e) {
            //If the Connection object cannot be closed, print the corresponding
            //error message
            System.out.println("Cannot close the Connection object: " + e);
        }
    }
}