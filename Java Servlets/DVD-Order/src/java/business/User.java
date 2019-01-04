package business;

/*Import the java.io.Serializable interface so the Javabean may
  implement it; this indicates this class is a Javabean class
  which contains instance variables, a no-argument constructor,
  and getter and setter methods for each instance variable.*/
import java.io.Serializable;

public class User implements Serializable {
    /*These are the instance variables of the User class, which
    correspond to the input fields within the HTML form. They are
    private, so they may only be accessed and/or modified through
    the getter and setter methods.*/
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    
    /*A no-argument constructor must be provided for the Javabean User class.
    This initializes the firstName, lastName, email, and password instance variables to "".*/
    public User() {
        firstName = "";
        lastName = "";
        email = "";
        password = "";
    }
    
    /*A getter method which, when called, returns a String that contains
    the first name of the customer.*/
    public String getFirstName() {
        return firstName;
    }
    
    /*A setter method for the firstName variable of the user.
    It accepts a string, the first name, as an argument, and uses the
    'this' reference variable to refer to the current object and sets
    the object's firstName variable to the string passed.*/
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /*A getter method which, when called, returns a String that contains
    the last name of the customer.*/
    public String getLastName() {
        return lastName;
    }
    
    /*A setter method for the lastName variable of the user.
    It accepts a string, the last name, as an argument, and uses the
    'this' reference variable to refer to the current object and sets
    the object's lastName variable to the string passed.*/
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /*A getter method which, when called, returns a String that contains
    the email of the customer.*/
    public String getEmail() {
        return email;
    }
    
    /*A setter method for the email variable of the user.
    It accepts a string, the email, as an argument, and uses the
    'this' reference variable to refer to the current object and sets
    the object's email variable to the string passed.*/
    public void setEmail(String email) {
        this.email = email;
    }
    
    /*A getter method which, when called, returns a String that contains
    the password of the customer.*/
    public String getPassword() {
        return password;
    }
    
    /*A setter method for the password variable of the user.
    It accepts a String, the password, as an argument, and uses the
    'this' reference variable to refer to the current object and sets
    the object's password variable to the string passed.*/
    public void setPassword(String password) {
        this.password = password;
    }
}
