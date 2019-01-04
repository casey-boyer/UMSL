package data;

/*Import the java.sql.*; package to use JDBC classes.*/
import java.sql.*;

/*Import the Product class from the business package, so the selectProduct
method of the ProductDB class can return a Product object.*/
import business.Product;

public class ProductDB {

    /*This method maps the Product object to the ProductList table in
    the DvdDB, and returns a Product object from the data stored at a particular
    row in the ProductList table. This is done by using the productCode argument
    passed to the method in the WHERE statement of the SQL query.
    First, the method retrieves a Connection object from the ConnectionPool object
    in order to connect to the DvdDB database.
    Then, the method declares a PreparedStatement object and ResultSet object
    and intializes them to null. Additionally, the method parses the productCode
    integer (passed as an argument) to a String, so it may be used in the PreparedStatement
    setString() object.
    The query selects an entire row from the ProductList table, where the pid
    column equals the productCode (passed as an argument). In the WHERE clause of
    the query, an '?' is used so the PreparedStatement can set this value
    and prevent SQL injections.
    After this, the method enters a try-catch-finally block to process the query and
    get the corresponding row from the query.
    In the try block, the PreparedStatement object is created by using the
    prepareStatement() method of the Connection object and passing the query
    String as an argument. Then, the setString() method of the PreparedStatement
    object sets the '?' value in the query String to the productCode. Then, the
    executeQuery() method of the PreparedStatement object is invoked, and the
    result is stored in the ResultSet object. After this, and if() statement
    checks to see if the next() row of the ResultSet exists; if so, the Product
    object is created and intialized with a no argument constructor, and the
    title, cover, price, and productCode variables of the Product object
    are initialized by using the ResultSet getString() and getInt() methods,
    where each argument corresponds to the column in the ProductList table.
    After this if statement, the product object is returned.
    If the row could not be retrieved from the database,
    the method catches the SQLException exception object and prints
    the corresponding error message.
    In the finally block, the ResultSet and PreparedStatement objects are
    closed, and the Connection object is free-d and returned to the connection pool.*/
    public static Product selectProduct(int productCode) {
        //Get the instance of the connection pool
        ConnectionPool pool = ConnectionPool.getInstance();
        //Get a connection from the connection pool
        Connection connection = pool.getConnection();
        //Declare the PreparedStatement object
        PreparedStatement ps = null;
        //Declare the ResultSet object
        ResultSet rs = null;
        //Parse the productCode integer to a string
        String prodCode = Integer.toString(productCode);

        //Create and initialize the query String, which selects an
        //entire row from the ProductList table where the pid column
        //is a particular value; this value will be supplied later by the prepared statement
        String query = "SELECT * FROM ProductList "
                + "WHERE pid = ?";
        //Try to sucessfuly execute the query
        try {
            //Create the PreparedStatement object
            ps = connection.prepareStatement(query);
            //Set the '?' value in the query string to the productCode
            ps.setString(1, prodCode);
            //Create the ResultSet object by executing the query
            rs = ps.executeQuery();
            //Declare the Product object
            Product product = null;
            //If the query row exists in the ResultSet object
            if (rs.next()) {
                //Initialize the Product object
                product = new Product();
                //Use the setTitle() method of the product object and getString()
                //method of the ResultSet object to get the product's title
                product.setTitle(rs.getString("Title"));
                //Use the setCover() method of the product object and the getString()
                //method of the ResultSet object to get the product's cover
                product.setCover(rs.getString("Cover"));
                //Use the setPrice() method of the product object and the getStirng()
                //method of the ResultSet object to get the product's price
                product.setPrice(rs.getString("Price"));
                //Use the setProductCode() method of the product object and the
                //getInt() method of the ResultSet object to get the product's product code
                product.setProductCode(rs.getInt("pid"));
            }
            //Return the product object
            return product;
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
}