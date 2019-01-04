package data;

/*Import the java.sql.*; package to use JDBC classes.*/
import java.sql.*;

/*This class is a utility class for the database that attempts to
safely close the Statement, PreparedStatement, and ResultSet object(s).*/
public class DBUtil {

    /*A method which attemps to close the Statement object passed to the method.
    If the Statement object is not null, the method calls the Statement objects
    close() method to close the Statement object. If the closing the
    Statement object throws a SQLException error, the method catches this error
    and prints the corresponding error message.*/
    public static void closeStatement(Statement s) {
        try {
            //If the Statement object is not null
            if (s != null) {
                //Close the Statement object
                s.close();
            }
        } catch (SQLException e) {
            //If closing the Statement object throws a SQLException exception object,
            //catch the exception and print the corresponding error message.
            System.out.println("Could not close the Statement object: " + e);
        }
    }

    /*This method attempts to close the prepared statement Statement object
    passed as an argument.
    If the statement is not null, the method calls the Statement object's
    close method to close the Statement object. If closing the Statement
    object throws a SQLException exception, the method catches this exception
    and prints the corresponding error message.*/
    public static void closePreparedStatement(Statement ps) {
        try {
            //If the prepared statement Statement object is not null
            if (ps != null) {
                //Close the prepared statement Statement object
                ps.close();
            }
        } catch (SQLException e) {
            //If an error occurs closing the Statement object, catch the
            //SQLException exception object and print the corresponding error
            //message
            System.out.println("Could not close the prepared statement Statement object: " + e);
        }
    }

    /*This method closes the ResultSet object passed as an argument.
    If the ResultSet object is not null, the method calls the ResultSet object's
    close() method and closes it. If closing the ResultSet object throws a
    SQLException exception object, the method catches it and prints the
    corresponding error message.*/
    public static void closeResultSet(ResultSet rs) {
        try {
            //If the ResultSet object is not null
            if (rs != null) {
                //Close the ResultSet object
                rs.close();
            }
        } catch (SQLException e) {
            //If closing the ResultSet object throws an SQLException exception
            //object, catch the exception and print the corresponding error message.
            System.out.println("Could not close the ResultSet object: " + e);
        }
    }
}