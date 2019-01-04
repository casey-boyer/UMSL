package cart; //The package which the servlet belongs to.

/*Imported classes necessary for the servlet to process the form data.
  business.* selects all of the JavaBeans in the business package.
  business.Product is the Javabean class for storing the
  form information of the user's dvd choice.
  business.Cart is the Javabean class for storing each item selected by
  the user and retaining these items as the user continues their order.
  business.Item is the Javabean class for storing each Product object and
  the quantity of each DVD, as well as formatting the DVD price.
  java.io.IOException is used to throw an IOException error, if it occurs.
  javax.servlet.ServletException is used to throw a ServletException error,
  if it occurs.
  javax.servlet.http.HttpServlet allows the servlet to extend the HttpServlet
  class and override its doPost and doGet methods.
  javax.servlet.HttpServletRequest is used to request form input data,
  to set the attributes for the Javabean User class, and to accept the
  HttpServletRequest object as parameter for the doPost and doGet methods
  & use it for the forward() method.
  javax.servlet.http.HttpServletResponse is used as a parameter for the
  doPost and doGet methods, and for the forward() method.
*/

import business.*;
import data.ProductDB;
import data.UserDB;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException 
    {
        /*Intialize the url, a String object, to index.jsp,
        for the getRequestDispatcher() method
        (this may change depending upon the value of the action parameter)*/
        String url= "/index.jsp";
            
        /*Get the value of the parameter which has the value "action";
        this is the within hidden input boxes in the forms on each jsp page.*/
        String action = request.getParameter("action");
            
        /*If the "name" attribute with the value "action" does not exist,
        set the action String to "cart." This is done when the user selects
        an "Add to Cart" button on index.jsp, since there are no 
        name attributes with the value "action." By setting the action
        string to "cart," the servlet can process the user's DVD request.*/
        if(action == null) {
            action = "cart";
        }
        
        //Create a HttpSession object to retain the user's DVD selections
        //and the quantity of each DVD selection.
        HttpSession session = request.getSession();
        
        /*Otherwise, if a name attribute with the value "action" does exist
        and the value equals "shop," this indicates that the user is currently
        on the "cart.jsp" page and clicked the "Continue Shopping" button. Thus,
        the url is set to index.jsp so the user may select more DVDs.*/
        if(action.equals("shop")) {
            url = "/index.jsp";
        }
        else if(action.equals("cart"))
        {
            /*If the action equals cart, then the user is on the cart.jsp page.
            Process the user's DVD selection(s). This will take care of the user
            updating the quantity of the DVD and/or removing DVD(s), and ensure
            that if the user selects more DVD(s), the original DVD(s) they selected
            are still in their cart.*/
            
            //Get the product code of the DVD selected by the user, which
            //is retrieved from the index.jsp page when the user clicks the
            //"Add to Cart" button.
            String productCode = request.getParameter("productCode");
            
            /*An integer variable, quantity, will hold the quantity of a particular
            DVD selected by the user.
            The quantityString is the value of the parameter name="quantity" on
            the cart.jsp page.*/
            int quantity;
            String quantityString = request.getParameter("quantity");
            
            /*Get the quantity of the corresponding DVD.
            If the quantityString is null, this indicates that the user selected
            a DVD on the index.jsp page and clicked the "Add to Cart" button.
            Thus, if the user selects a DVD on the index.jsp page and this
            DVD is already in the cart, this retains the quantity of the DVD
            and adds one to the quantity, rather than resetting it to 0. 
            This is done by getting the item attribute stored in the session object 
            for that DVD, and ensuring that the item.getProduct.getProduct code is
            equivalent to the product code retrieved on the index.jsp page, 
            and setting the DVD to the quantity of that item attribute.
            
            However, if the user selects a DVD on the index.jsp page and this DVD
            is NOT in the cart, then the item object stored in the session object
            will not have a matching product code to that DVD, and thus the DVD's
            quantity is set to 1, since the user is adding a new DVD to the cart.
            
            Additionally, if the application is deployed for the first time OR if the
            user has already placed an order and returned to "index.jsp" from "checkout.jsp,"
            then the Item object has been removed from the session object and does not
            exist. This means the user is selecting the first DVD for their order,
            and the quantity should be one. For further selections, the Item
            object will exist in the session attribute (until the user checks out).
            
            Otherwise, if the quantityString is NOT null, then this indicates
            the user is on the cart.jsp page. Thus, parse the quantityString
            to an integer and set the quantity integer equal to this value. If
            the quantity is a negative number, set the quantity to 1. If the
            quantity is an incorrect value, such as an alphabetic character,
            set the quantity to 1.*/
            if(quantityString == null) {
                //Get the Item object stored in the session attribute.
                Item item = (Item) session.getAttribute("item");
                
                /*If the item object returned from the session object is null,
                then the user has either just visited the DVD order form OR
                the user has already processed an order at the "checkout.jsp"
                page and the Item object was removed from the session. Thus,
                this is the first DVD the user has selected, and the quantity must be
                one.*/
                if(item == null) {
                    //Set the quantity to one.
                    quantity = 1;
                }
                else {
                    //Get the product code of the Item object stored in the session
                    //attribute.
                    int itemProdCode = item.getProduct().getProductCode();
                    /*If the product code of the Item stored in the session attribute
                    is equal to the product code retrieved from the index.jsp page,
                    then this DVD is already in the cart; thus, retrieve the quantity
                    of the Item object and add one to it.
                    OTHERWISE, if the product code of the Item stored in the session
                    attribute is NOT equal to the product code retrieved from the 
                    index.jsp page, set the quantity integer to 1, since the
                    user is adding a new DVD to the cart.*/
                    if(itemProdCode == (Integer.parseInt(productCode))) {
                        //Increase the quantity of the DVD by one.
                        quantity = (item.getQuantity() + 1);
                    }
                    else {
                        //Set the quantity of the DVD to one.
                        quantity = 1;
                    }
                }
            }
            else {
                /*If the quantityString is NOT null, then the user is on the
                cart.jsp page and is updating the quantity of one of the DVDs.
                Parse the quantityString to an integer and set the quantity integer
                equal to this value.
                If the quantity is negative, set the value to 1.
                If the quantity entered by the user is NOT a number, i.e.
                an alphabetic character, symbol, etc, catch the exception
                and set the quantity equal to 1.*/
                try {
                    //Parse the quantityString to an integer.
                    quantity = Integer.parseInt(quantityString);
                    if (quantity < 0) {
                        //If quantity is negative, set the quantity equal to one.
                        quantity = 1;
                    }
                } catch (NumberFormatException nfe) {
                    //If the quantity entered is not a number, catch the
                    //exception and set the quantity equal to one.
                    quantity = 1;
                }
            }
            //String quantityString = request.getParameter("quantity");
            
            /*Get the user's cart by retreiving the Cart object from the current
            session. If the Cart object is null, then the user has made their
            first DVD selection, so create the Cart object. Otherwise, use the
            Cart object stored in the session object.*/
            Cart userCart = (Cart) session.getAttribute("cart");
            if(userCart == null) {
                //Create the Cart object.
                userCart = new Cart();
            }
            
            //Parse the product code parameter to an integer.
            int prodCode = Integer.parseInt(productCode);
            
            //Select the DVD from the database that matches the product code parameter,
            //and retrieve the corresponding product from it.
            Product product = (Product) ProductDB.selectProduct(prodCode);
            
            /*Create an Item object, which corresponds to an individual DVD 
            selected by the user. Set the Product and quantity instance variables
            of the Item object.*/
            Item lineItem = new Item();
            //Set the Item's product variable.
            lineItem.setProduct(product);
            //Set the Item's quantity variable.
            lineItem.setQuantity(quantity);
            
            /*Add the Item object to the Cart object. This ensures that
            if the user is on the cart.jsp page and they edit the
            quantity of a DVD, the Cart will correspondingly change what
            DVD(s) the user has selected and the amount of DVD(s).
            If the quantity is greater than zero, the Item object is
            added to the cart; this occurs when a user first selects
            a DVD or changes the quantity (on cart.jsp) to a value
            greater than 0.
            If the quantity equals 0, the Item object is removed from the Cart
            object. This occurs when a user manually enters '0' for the item
            quantity, or when the user clicks the "remove" button.*/
            if (quantity > 0) {
                //Add the Item object to the cart.
                userCart.addItem(lineItem);
            } else if (quantity == 0) {
                //Remove the Item object from the cart.
                userCart.removeItem(lineItem);
            }
            
            /*Set both the Item and Cart objects in the session object.
            In doing so, they can be retrieved when the user travels between
            index.jsp and cart.jsp; this ensures that the DVDs selected by the user
            and the quantity of the DVDs are retained when the user leaves the index.jsp
            or cart.jsp pages.*/
            session.setAttribute("item", lineItem);
            session.setAttribute("cart", userCart);
            
            /*If the userCart.getCount() method returns 0, this indicates that the
            user has no items in the cart. Thus, if the user is on the cart.jsp
            page and has only 1 dvd and removes this dvd or sets the dvd's quantity
            to 0, then they should be redirected to index.jsp. This prevents the user
            from checking out with an empty cart.
            If the action String equals "cart", set the url to cart.jsp. This
            is done when the user clicks the "Add to Cart" button, or when
            the user updates the quantity and/or removes a DVD.*/
            if(userCart.getCount() == 0) {
                url = "/index.jsp";
            }
            else {
                url = "/cart.jsp";
            }
        }
        else if(action.equals("checkout")) {
            /*If the action String equals "checkout", then the user has
            selected the "Checkout" button on the cart.jsp page. Thus,
            the url must be set to the sign in page, which is signIn.jsp.*/
            url = "/signIn.jsp";
        }
        else if(action.equals("add")) {
            /*If the action String equals "add", then the user has submitted
            the form on the signIn jsp page. Thus, the user's email
            and password provided must be checked to ensure:
            1. The email exists
            2. The password matches the password for the given email in the
            User table
            3. If the password does not match, display an error message
            4. If the email does not exist in the
            User table, have the customer make an account.*/
            
            //Create a User object to retrieve the customer's data
            User user = new User();
            
            /*Get the values of the email and password provided by the customer in the
            sign-in page form.*/
            String userEmail = request.getParameter("userEmailInput");
            String userPass = request.getParameter("pwd");
            
            /*Get the password that corresponds to the email in the User table
            provided by the customer*/
            String userDBPass = UserDB.getPass(userEmail);
            
            //If the user's password does not match the given email,
            //this message will be displayed on the sign in page.
            String errMsg = "";
            
            //Check if the user's email exists
            boolean emailExists = UserDB.emailExists(userEmail);
            
            if(!emailExists)
            {
                /*If the email provided by the customer does not exist
                in the User table, then set the email and password attributes
                of the User object and redirect the customer to the register
                jsp page. Additionally, reset the errMsg string.*/
                user.setEmail(userEmail);
                user.setPassword(userPass);
                url = "/register.jsp";
                errMsg = "";
            }
            else {
                /*The email exists, so check if the password entered by the user matches
                the password for the email stored in the User table*/
                if(!(userPass.equals(userDBPass))) {
                    /*If the password provided does not match the password field in
                    the User table that corresponds to the email provided by
                    the customer, then the customer has entered an incorrect password.
                    Set the errMsg String to notify the customer and store the
                    String in the request object so the signIn jsp page may access it.
                    Redirect the customer to the signIn jsp page so they may
                    attempt to sign in again.*/
                    errMsg = "The password for the email does not match.";
                    request.setAttribute("errMsg", errMsg);
                    url = "/signIn.jsp";
                }
                else {
                    /*The email and password match, reroute the user
                    to the checkout page. 
                    Set the User object equal to the User object returned
                    from the selectUser() method of the UserDB class, passing
                    the email provided as the method argument. This sets the
                    email, firstName, lastName, and password variables of the user
                    object.
                    Reset the errMsg string.*/
                    user = (User) UserDB.selectUser(userEmail);
                    url = "/checkout.jsp";
                    errMsg = "";
                }
            }
            
            /*Store the user object in the session object, so the user's
            email, first name, and last name may be accessed and/or stored
            later.*/
            session.setAttribute("user", user);
        }
        else if(action.equals("register")) {
            /*If the action String equals register, then the user provided
            an email on the sign in page that does not exist in the User table,
            and thus the customer must register and create an account and is
            on the register jsp page.*/
            
            /*This errMsg String will be used to display an error message on the
            register jsp page; for example, if the customer changes the email
            to one that already exists, this String will display the corresponding
            error.*/
            String errMsg = "";
            
            /*Get the email, first name, and last name provided by the customer in
            the form on the register jsp page. Even though the email value was
            already obtained on the sign in page, this is to ensure that if
            the customer changes the email to one that already exists, they will not
            be able to register with that email.*/
            String userEmail = request.getParameter("userEmailInput");
            String userFName = request.getParameter("userFNameInput");
            String userLName = request.getParameter("userLNameInput");
            
            //Get the user object stored in the session object.
            User user = (User) session.getAttribute("user");
            
            /*Check if the user's email was changed to an email that is already
            in use*/
            boolean emailExists = UserDB.emailExists(userEmail);
            
            /*If the HTML5 required attribute fails and the user submits an
            incomplete form, then they will be redirected to the register.jsp page
            and an error message will be displayed.*/
            if(userEmail == null || userFName == null || userLName == null
                    || userEmail.isEmpty() || userFName.isEmpty() || userLName.isEmpty())
            {
                /*Set the errMsg String, store it in the request object, and
                redirect the user to the register jsp page.*/
                errMsg = "Please fill out all of the fields.";
                request.setAttribute("msg", errMsg);
                url = "/register.jsp";
            }
            else if(emailExists) {
                /*If the customer changed the email to one that already exists
                in the User table, redirect the customer to the register jsp page
                and display an error message.*/
                errMsg = "The email '" + userEmail +"' is unavailable.";
                request.setAttribute("msg", errMsg);
                url = "/register.jsp";
            }
            else {
                /*Otherwise, if the email provided by the customer does not
                exist in the User table, attempt to add the customer's information
                to the User table.*/
                
                //Reset the errMsg String.
                errMsg = "";
                
                /*Set the User object's email, firstName, and lastName variables
                based on the input provided by the customer in the form.*/
                user.setEmail(userEmail);
                user.setFirstName(userFName);
                user.setLastName(userLName);

                /*Attempt to enter the customer's information into the User table.
                Since the insert() method of the UserDB class returns an integer,
                specifically 0 for failure, store the return value in the
                "successInsert" int variable to verify
                that the customer's info was inserted in the User table.*/
                int successInsert = UserDB.insert(user);

                if(successInsert == 0) {
                    /*If the customer's information could not be inserted in
                    the User table, redirect the customer to the register jsp
                    page and set the error message.*/
                    errMsg = "The information provided could not be stored.";
                    request.setAttribute("msg", errMsg);
                    url = "/register.jsp";
                }
                else {
                    /*Otherwise, if the customer's information was succesfully
                    inserted, redirect the customer to the checkout jsp page.
                    Reset the errMsg String.*/
                    errMsg = "";
                    url = "/checkout.jsp";
                }
            }
        }
        else if(action.equals("join")) {
            /*If the action String equals "join", then this indicates that the
            user has already reached the checkout page and is done with their
            order. Thus, the item, cart, and user attributes must be removed from
            the session object, so that if the user places another order,
            the contents of their previous Cart are not in this order,
            and they will have to sign in again or make a new account.*/
            session.removeAttribute("item");
            session.removeAttribute("cart");
            session.removeAttribute("user");
            /*Since the url String is initially set to "index.jsp," then the url
            string will be "index.jsp" at this else-if statement, and thus there
            is no need to change the url String.*/
        }
            
        /*Use the getServletContext() method to get a Servlet Context object
        so the request from this servlet may be forwarded to the url 
        (which may be index.html or thanks.jsp), then call the
        getRequestDispatcher() method of the Servlet Context object,
        passing the url String as an object, and then use the forward()
        method of the Servlet Context object to forward the request and
        response objects to the url.*/
        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, 
                          HttpServletResponse response) 
                          throws ServletException, IOException 
    {
        //Override the doGet method and send the request and response objects 
        //to the doPost() method, where they will be processed.
        doPost(request, response);
    }
}
