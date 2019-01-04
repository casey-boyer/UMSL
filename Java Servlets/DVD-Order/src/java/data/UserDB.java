package data;

/*Import the java.sql.*; package to use JDBC classes.*/
import java.sql.*;

/*Import the User class from the business package, so the selectUser
method of the UserDB class can return a User object.*/
import business.User;

public class UserDB {
    /*This method returns a User object from the data stored at a particular
    row where the email provided matches in the User table. This is done by using the email argument
    passed to the method in the WHERE statement of the SQL query.
    First, the method retrieves a Connection object from the ConnectionPool object
    in order to connect to the hw5 database.
    Then, the method declares a PreparedStatement object and ResultSet object
    and intializes them to null.
    The query selects an entire row from the User table, where the email
    column equals the email that was passed as an argument. In the WHERE clause of
    the query, an '?' is used so the PreparedStatement can set this value
    and prevent SQL injections.
    After this, the method enters a try-catch-finally block to process the query and
    get the corresponding row from the query.
    In the try block, the PreparedStatement object is created by using the
    prepareStatement() method of the Connection object and passing the query
    String as an argument. Then, the setString() method of the PreparedStatement
    object sets the '?' value in the query String to the email String. Then, the
    executeQuery() method of the PreparedStatement object is invoked, and the
    result is stored in the ResultSet object. After this, and if() statement
    checks to see if the next() row of the ResultSet exists; if so, the User
    object is created and intialized with a no argument constructor, and the
    email, firstName, lastName, and password variables of the User object
    are initialized by using the ResultSet getString() method,
    where each argument corresponds to the column in the User table.
    After this if statement, the User object is returned.
    If the row could not be retrieved from the database,
    the method catches the SQLException exception object and prints
    the corresponding error message.
    In the finally block, the ResultSet and PreparedStatement objects are
    closed, and the Connection object is free-d and returned to the connection pool.*/
    public static User selectUser(String email) {
        //Get the instance of the connection pool
        ConnectionPool pool = ConnectionPool.getInstance();
        //Get a connection from the connection pool
        Connection connection = pool.getConnection();
        //Declare the PreparedStatement object
        PreparedStatement ps = null;
        //Declare the ResultSet object
        ResultSet rs = null;

        //Create and initialize the query String, which selects an
        //entire row from the User table where the email column
        //is a particular value; this value will be supplied later by the prepared statement
        String query = "SELECT * FROM User "
                + "WHERE Email = ?";
        //Try to sucessfuly execute the query
        try {
            //Create the PreparedStatement object
            ps = connection.prepareStatement(query);
            //Set the '?' value in the query string to the email
            ps.setString(1, email);
            //Create the ResultSet object by executing the query
            rs = ps.executeQuery();
            //Declare the User object
            User user = null;
            //If the query row exists in the ResultSet object
            if (rs.next()) {
                //Initialize the User object
                user = new User();
                //Use the setFirstName() method of the User object and getString()
                //method of the ResultSet object to get the users's first name
                user.setFirstName(rs.getString("FirstName"));
                //Use the setLastName() method of the User object and the getString()
                //method of the ResultSet object to get the user's last name
                user.setLastName(rs.getString("LastName"));
                //Use the setEmail() method of the User object and the getString()
                //method of the ResultSet object to get the user's email
                user.setEmail(rs.getString("Email"));
                //Use the setPassword() method of the User object and the
                //getString() method of the ResultSet object to get the user's password
                user.setPassword(rs.getString("Password"));
            }
            //Return the user object
            return user;
        } catch (SQLException e) {
            //If the query could not be executed, catch the SQLException exception object
            //and print the corresponding error message
            System.out.println("Could not execute query: " + e);
            //Return null
            return null;
        } finally {
            //Close the ResultSet and PreparedStatement objects
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            //Free the connection object
            pool.freeConnection(connection);
        }
    }
    
    /*This method returns a boolean value, true or false, which indicates if the
    email passed to the method exists in the User table.*/
    public static boolean emailExists(String email) {
       //Get the instance of the connection pool
       ConnectionPool pool = ConnectionPool.getInstance();
       //Get a connection from the connection pool
       Connection connection = pool.getConnection();
       //Declare the preparedStatement object
       PreparedStatement ps = null;
       //Declare the resultSet object
       ResultSet rs = null;

       /*Create and initialize the query string, which selects the value of
       the email field from the User table, where the Email field equals a particular
       value*/
       String query = "SELECT Email FROM User "
               + "WHERE Email = ?";
       //Try to sucessfully execute the query
       try {
           //Create the preparedStatement object
           ps = connection.prepareStatement(query);
           //Set the '?' value in the query String to the email value passed to the method
           ps.setString(1, email);
           //Create the ResultSet object by executing the query
           rs = ps.executeQuery();
           //Return the boolean value of the next() method of the resultSet object,
           //which indicates if the query retrieved the email (true) or not (false)
           return rs.next();
       } catch (SQLException e) {
           //If the query could not be executed, catch the SQLException exception object
           //and print the corresponding error message
           System.out.println(e);
           //Return false
           return false;
       } finally {
           //Close the resultSet and preparedStatement objects
           DBUtil.closeResultSet(rs);
           DBUtil.closePreparedStatement(ps);
           //Free the connection object
           pool.freeConnection(connection);
       }
    }
     
    /*This method returns a String value that corresponds to the password value
    in the User table based on the email String passed as an argument.
    If the query successfully executes, the method returns the password value;
    otherwise, it returns null.*/
    public static String getPass(String email) {
        //Get the instance of the connection pool
        ConnectionPool pool = ConnectionPool.getInstance();
        //Get a connection from the connection pool
        Connection connection = pool.getConnection();
        //Declare the PreparedStatement object
        PreparedStatement ps = null;
        //Declare the ResultSet object
        ResultSet rs = null;
        //Declare the String the will contain the password of the email provided
        String userPass = "";
        
        /*Create and intialize the query String, which selects the password field
        from the User table when the Email field equals a particular value.*/
        String query = "SELECT Password FROM User "
                + "WHERE Email = ?";
        //Try to succesfully execute the query
        try {
            //Create the preparedStatement object
            ps = connection.prepareStatement(query);
            //Set the '?' in the query String to the email provided
            ps.setString(1, email);
            //Create the resultSet object by executing the query
            rs = ps.executeQuery();
            //If the resultSet object has a value
            if(rs.next()) {
                //Set the userPass String to the Password field in the User table
                //using the getString() method of the resultSet object and passing
                //an argument that indicates the Password field of the User table
                userPass = rs.getString("Password");
            }
            //Return the userPass String
            return userPass;
        } catch(SQLException e) {
            //If the query could not be executed, catch the SQLException exception object
            //and print the corresponding error message
            System.out.println(e);
            //Set the userPass String to null
            userPass = null;
            //Return the userPass String
            return userPass;
        } finally {
            //Close the ResultSet and PreparedStatement objects
            DBUtil.closeResultSet(rs);
            DBUtil.closePreparedStatement(ps);
            //Free the connection from the connection pool
            pool.freeConnection(connection);
        }
    }
    
    /*This method inserts a row into the User table and accepts a User object
    as an argument, and returns the number (int) of rows inserted. The
    query String inserts a value for Email, Password, FirstName, and LastName
    of the User table, and uses a prepared statement to prevent SQL injection attacks.
    Then, the method attempts to execute the query with a try-catch-finally block.
    In the try block, the values for the Email, Password, FirstName, and LastName fields
    of the User table are provided by calling the corresponding get methods of the User
    object passed as an argument. Then, the method returns the number of rows inserted.
    If an exception is thrown, the catch block catches the exception and returns 0,
    indicating 0 rows were inserted.*/
    public static int insert(User user) {
        //Get the instance of the connection pool
        ConnectionPool pool = ConnectionPool.getInstance();
        //Get a connection from the connection pool
        Connection connection = pool.getConnection();
        //Declare the PreparedStatement object
        PreparedStatement ps = null;

        /*Create and initialize the query String; this query inserts an entire
        row into the User table, specifically values for the Email, Password, FirstName
        and LastName columns, and uses '?' to prevent SQL injection attacks.*/
        String query
                = "INSERT INTO User (Email, Password, FirstName, LastName) "
                + "VALUES (?, ?, ?, ?)";
        //Try to successfully execute the query
        try {
            //Create the preparedStatement object
            ps = connection.prepareStatement(query);
            //Set the first '?' in the query String using the getEmail() method
            //of the User object
            ps.setString(1, user.getEmail());
            //Set the second '?' in the query String using the getPassword() method
            //of the User object
            ps.setString(2, user.getPassword());
            //Set the third '?' in the query String using the getFirstName() method
            //of the User object
            ps.setString(3, user.getFirstName());
            //Set the fourth '?' in the query String using the getLastName() method
            //of the User object
            ps.setString(4, user.getLastName());
            //Return the number of rows inserted into the User table
            return ps.executeUpdate();
        } catch (SQLException e) {
            //If the query could not be executed, catch the SQLException exception object
            //and print the corresponding error message
            System.out.println(e);
            //Return 0, indicating 0 rows were inserted.
            return 0;
        } finally {
            //Close the PreparedStatement object
            DBUtil.closePreparedStatement(ps);
            //Free the connection
            pool.freeConnection(connection);
        }
    }
}
