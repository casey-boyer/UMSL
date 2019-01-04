package business;

/*Import the java.io.Serializable interface so the Javabean may
  implement it; this indicates this class is a Javabean class
  which contains instance variables, a no-argument constructor,
  and getter and setter methods for each instance variable.
  Additonally, import the ArrayList class, so an array of
  Item objects may be created for the Cart object.*/
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Cart implements Serializable {

    /*The only instance variable of the Cart class, which is an ArrayList
    object that contains an array of Item objects.*/
    private ArrayList<Item> items;

    /*A no argument constructor that must be provided for the Javabean Cart class.
    The constructor intializes the items instances variable to a new ArrayList.*/
    public Cart() {
        items = new ArrayList<Item>();
    }

    /*A getter method that, when called, returns an ArrayList of item objects,
    which is the Cart object's instance variable items.*/
    public ArrayList<Item> getItems() {
        return items;
    }

    /*A getter method that, when called, returns the size of the Arraylist<Items>, which is the 
    Cart object's instance variable items.*/
    public int getCount() {
        return items.size();
    }

    /*A method that adds an item to the Cart objects items ArrayList. It accepts
    an Item object as an argument, and does not have a return value.
    The method retrieves the product code of the DVD and the quantity of the DVD
    by using the getter methods of the item object.
    Then, the method enters a for loop that terminates once the loop counter variable
    is equal to the size of the items ArrayList.
    Within the for loop, an Item object is created and initialized to the Item
    object in the items ArrayList at that current loop counter variable. Then,
    if the product code of that Item object equals the product code of the Item
    object passed to the method as an argument, the corresponding Item object's quantity
    is updated.
    After the for loop terminates, the item passed as an argument is add to the
    items ArrayList of the Cart object.*/
    public void addItem(Item item) {
        //Get the product code of the item object passed to the method, and parse
        //the product code to a String.
        String code = Integer.toString(item.getProduct().getProductCode());
        //Get the quantity of the item object passed to the method.
        int quantity = item.getQuantity();
        
        for (int i = 0; i < items.size(); i++) {
            //Get the item object from the items ArrayList at the current loop
            //counter value.
            Item lineItem = items.get(i);
            //If the productCode of the item object in the items ArrayList at the current loop counter
            //value EQUALS the productCode of the item object passed to the method, set the
            //quantity of the item object, and then exit the loop.
            if (Integer.toString(lineItem.getProduct().getProductCode()).equals(code)) {
                lineItem.setQuantity(quantity);
                return;
            }
        }
        //Add the item object to the items ArrayList.
        items.add(item);
    }

    /*A method that removes an item from the Cart object's items ArrayList. It accepts
    an Item object as an argument, and does not have a return value.
    The method retrieves the productCode of the item object passed as an argument to the method.
    Then, the method enters a for loop that terminates once the loop counter variable
    is equivalent to the size of the items ArrayList.
    Within the for loop, an Item object is created and intialized to the Item object
    in the items ArrayList at the current loop counter variable.
    Then, if the productCode of this item object is equal to the productCode of
    the Item object passed to the method, the item at the loop counter value index
    in the items ArrayList is removed from the ArrayList.*/
    public void removeItem(Item item) {
        //Get the productCode of the Item object passed to the method, and parse it
        //to a String for comparison purposes.
        String code = Integer.toString(item.getProduct().getProductCode());
        
        for (int i = 0; i < items.size(); i++) {
            //Get the Item object from the items ArrayList at the current loop
            //counter value.
            Item lineItem = items.get(i);
            //If the productCode of the item retrieved from the ArrayList at the current loop counter
            //value is equal to the productCode of the item object passed to the method,
            //then delete the item from the items ArrayList and exit the for loop.
            if (Integer.toString(lineItem.getProduct().getProductCode()).equals(code)) {
                items.remove(i);
                return;
            }
        }
    }
    
    /*A getter method that returns a String of the representing the total price 
    of the user's DVD selections. The string that is returned 
    is formatted as "$xx.xx" where each x represents the total of the DVDs.*/
    public String getTotalCurrencyFormat() {
        //Get a NumberFormat object
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        //Create a double variable called sum and initialize it to 0, it will
        //be used during the foor loop to keep a sum of the total price
        double sum = 0;
        
        for(int i = 0; i < items.size(); i++) {
            //Get the Item object from the items ArrayList at the current
            //loop counter variable.
            Item lineItem = items.get(i);
            
            //Add the current item's total variable to the sum.
            sum += lineItem.getTotal();
        }
        
        //Return the sum as a formatted String
        return currency.format(sum);
    }
}

