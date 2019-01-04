package business;

/*Import the java.io.Serializable interface so the Javabean may
  implement it; this indicates this class is a Javabean class
  which contains instance variables, a no-argument constructor,
  and getter and setter methods for each instance variable.
  Additionally, import the java.text.NumberFormat class
  to format the price of the DVD.*/
import java.io.Serializable;
import java.text.NumberFormat;

public class Product implements Serializable {
    /*These are the instance variables of the Product class, which
    correspond to the dvds the user can select within the HTML form. They are
    private, so they may only be accessed and/or modified through
    the getter and setter methods.*/
    private String cover;
    private String title;
    private String price;
    private int productCode;
    
    /*A no-argument constructor must be provided for the Javabean Product class.
    This initializes each instance variable to "", and the productCode to 0.*/
    public Product()
    {
        cover = "";
        title = "";
        price = "";
        productCode = 0;
    }
    
    /*A setter method that, when called with a String arugment, sets
    the image source or cover of the DVD and uses the
    'this' reference variable to refer to the current object and sets
    the object's cover variable to the string passed.
    */
    public void setCover(String cover)
    {
        this.cover = cover;
    }
    
    /*A getter method that, when called, returns a String which contains
    the image URL location of the DVD cover.
    */
    public String getCover()
    {
        return cover;
    }
    
    /*A setter method that, when called with a String arugment, sets
    the title of the DVD and uses the 'this' reference variable to refer to 
    the current object and sets the object's title variable to the string passed.
    */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    /*A getter method that, when called, returns a String which contains
    the title of the DVD.
    */
    public String getTitle()
    {
        return title;
    }
    
    /*A setter method that, when called with a String arugment, sets
    the price of the DVD and uses the 'this' reference variable to refer to 
    the current object and sets the object's price variable to the string passed.*/
    public void setPrice(String price)
    {
        this.price = price;
    }
    
    /*A getter method that, when called, returns a String which contains
    the price of the DVD.
    */
    public String getPrice()
    {
        return price;
    }
    
    /*A setter method that, when called with an integer argument, sets
    the product code of the DVD and uses the 'this' reference variable to
    refer to the current object and sets the object's productCode variable
    to the integer passed.*/
    public void setProductCode(int productCode)
    {
        this.productCode = productCode;
    }
    
    /*A getter method that, when called, returns an integer whose value is
    the productCode of the DVD.*/
    public int getProductCode()
    {
        return productCode;
    }
    
    /*A getter method that returns a String of the current Product object's
    price variable. The string that is returned is formatted as "$xx.xx" where
    each x represents the price of the Product object.*/
    public String getPriceCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        Double doubPrice = Double.parseDouble(this.price);
        return currency.format(doubPrice);
    }
    
}