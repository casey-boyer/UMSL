package business;

/*Import the java.io.Serializable interface so the Javabean may
  implement it; this indicates this class is a Javabean class
  which contains instance variables, a no-argument constructor,
  and getter and setter methods for each instance variable.
  Additionally, import the java.text.NumberFormat class
  to format the total price of the DVD based on its quantity.*/
import java.io.Serializable;
import java.text.NumberFormat;

public class Item implements Serializable {
    
    /*These are the instance variables of the Item class. 
    The product instance variable is a Product object, which 
    corresponds to the DVD selected by the user.
    The quantity instance variable is an integer which represents
    the quantity of the DVD selected by the user.*/
    private Product product;
    private int quantity;

    /*A no-argument constructor must be provided for the Javabean Item class.*/
    public Item() {}

    /*A setter method that, when called with a Product object argument,
    initializes the product instance variable of the class to the Product
    object passed.*/
    public void setProduct(Product p) {
        product = p;
    }

    /*A getter method that, when called, returns the product instance variable.*/
    public Product getProduct() {
        return product;
    }

    /*A setter method that, when called with an integer variable, intializes
    the quantity instance variable of the Item object and uses the
    'this' reference variable to refer to the current object and sets
    the object's quantity variable to the integer passed.*/
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /*A getter method that, when called, returns the object's quantity variable.*/
    public int getQuantity() {
        return quantity;
    }

    /*A getter method that, when called, returns a double variable that represents
    the quantity of a DVD multiplied by the price to retrieve a total for that DVD.*/
    public double getTotal() {
        //Parse the price variable of the product object to a double, and multiply
        //it by the current quantity.
        double total = Double.parseDouble(product.getPrice()) * quantity;
        return total;
    }

    /*A getter method that returns a String of the representing the total price of a DVD, which is the 
    price of the DVD multipled by the quantity. The string that is returned 
    is formatted as "$xx.xx" where each x represents the total of the DVD.*/
    public String getTotalCurrencyFormat() {
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        return currency.format(this.getTotal());
    }
}
