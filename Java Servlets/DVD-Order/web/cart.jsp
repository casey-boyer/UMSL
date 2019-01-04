<%--Add the taglib for the JSTL core library.--%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--The link to the stylesheet for the page.--%>
        <link rel="stylesheet" href="styles/cart.css" type="text/css"/>
        <%--The title of the page, which will be displayed
        on the tab in the customer's browser, clearly
        indicates the purpose of the page.--%>
        <title>Cart</title>
    </head>
    <body>
        <%--This div element displays the title of the page, and has
        the id "pageTitle" for styling, and the classes "centerText" and
        "pageHeader" for styling. The div element clearly
        indicates to the customer that their requested
        DVD(s) were processed.--%>
        <div id="pageTitle" class="centerText pageHeader">Items in Cart</div><br/>
           
        <%--This table displays the DVD(s) the customer selected.
        It contains 6 columns, with the headers cover, title, price, amount, and quantity.
        The 6th column does not have text in the table header, and is used to display the
        "Remove" button.
        It uses a foreach loop to loop through the items arraylist in the cart object that is stored
        in the session variable.
        
        For each cover row in the table, the loop outputs the item's product's object cover
        variable, which contains the image url for the dvd cover.
        
        For each title row in the table, the loop outputs the item's product's object
        title variable, which contains the title String for the dvd.
        
        For each price row in the table, the loop outputs the item's product's object
        priceCurrencyFormat method, which returns the price of the dvd formatted in for text.
        
        For each amount row in the table, the loop outputs the item's totalCurrencyFormat
        method, which returns the total of the (DVD price) * (DVD quantity) in text format.
        
        For each quantity row in the table, there is form element used for updating
        the quantity of the DVD. Within the form element, there is a hidden input
        field with the name productCode, and the value, which is outputted by the loop,
        is the item's product's productCode variable. Additionally, a text input field
        displays the current quantity of the DVD, and this input field has the name
        quantity and the value, which is outputted by the loop, is the item's 
        quantity variable. To update the quantity, an input element with type
        "submit" is the submit button, with the value "Update."
        Thus, when the user updates the quantity, the servlet can retrieve the 
        value by requesting the parameter with the name quantity.
        
        Then, for the table column that does not contain a table header with text, the
        column contains a form element; the form element contains an input element with
        type "submit" so it is a button, and the value is "Remove" so it indicates that
        if the user clicks it, the DVD will be removed. The form element also contains a hidden input field
        with the name attribute "productCode", with the value of the DVD's/Product's productCode
        value, which is outputted by the for loop as the item's product's productCode variable. 
        Additionally, the form element contains another hidden input field with the name 
        "quantity" and value "0". Thus, when the "Remove" button is clicked,
        the servlet will use the quantity value "0" to remove it from the cart.--%>
        <table class="pageText tableStyle">
            <tr>
                <th class="tableHeaders">Cover</th>
                <th class="tableHeaders">Title</th>
                <th class="tableHeaders">Price</th>
                <th class="tableHeaders">Amount</th>
                <th class="tableHeaders">Quantity</th>
                <th></th>
            </tr>
            <c:forEach var="item" items="${cart.items}">
            <tr>
                <td><img src="<c:out value='${item.product.cover}'/>" class="movieImages"></td>
                <td><c:out value='${item.product.title}'/></td>
                <td>${item.product.priceCurrencyFormat}</td>
                <td><c:out value='${item.totalCurrencyFormat}'/></td>
                <td>
                    <%--This form element allows the user to update the DVD's quantity.
                    The hidden input field with the name "productCode" has the value
                    of the item's product's productCode variable. The text input
                    field uses the item's quantity variable to display the current
                    quantity of the DVD, and also to let the user change it.
                    When the "Update" button is clicked, the servlet
                    can retrieve the DVD's new quantity by requesting the
                    quantity parameter and productCode parameter.--%>
                    <form action="" method="post">
                        <input type="hidden" name="productCode" 
                            value="<c:out value='${item.product.productCode}'/>">
                        <input type=text name="quantity" 
                               value="<c:out value='${item.quantity}'/>" id="quantity" class="quantityBoxes">
                        <input type="submit" value="Update" class="buttonStyles">
                        </form>
                </td>
                <td>
                    <form action="" method="post">
                        <%--This form element allows the user to remove the
                        corresponding DVD. The hidden input field with the name
                        "productCode" has the value of the item's product's productCode
                        variable. The other hidden input field with the name "quantity"
                        has a value of "0", so when the user clicks the "Remove" button,
                        the servlet can remove the DVD by requesting the productCode 
                        parameter and quantity parameter.--%>
                        <input type="hidden" name="productCode" 
                            value="<c:out value='${item.product.productCode}'/>">
                        <input type="hidden" name="quantity" 
                            value="0">
                        <input type="submit" value="Remove Item" class="buttonStyles">
                    </form>
                </td>
            </tr>
            </c:forEach>
        </table>
        
        <%--This form processes the user's request if they wish to continue shopping.
        It has the action "order" so the servlet can process the request.--%>
        <form action="order" method="post">
            <%--This hidden input field has the name "action" and value "shop", so
            when the user clicks the "Continue Shopping" button, the servlet can request
            the action parameter and redirect the user to the index.jsp page.--%>
            <input type="hidden" name="action" value="shop">
            <%--This input field is a button that displays the
            text "Continue", so it is clearly indicated to the customer
            that clicking this button will return them to the DVD order page.
            It has an id for convention purposes and the class "buttonStyles" for styling.
            The div element is used to center the button on the page.--%>
            <div class="centerText">
                <input type="submit" value="Continue Shopping" id="contShoppingButton" class="buttonStyles">
            </div>
        </form>
        
        <br/>
        <%--This form processes the user's request if they wish to checkout.
        It has the action "order" so the servlet can process the request.--%>
        <form action="order" method="post">
            <%--This hidden input field has the name "action" and value "checkout", so
            when the user clicks the "Checkout" button, the servlet can request the
            action parameter and redirect the user to the checkout.jsp page.--%>
            <input type="hidden" name="action" value="checkout">
            <%--This input field is a button that displays the text "Checkout", so it
            is clearly indicated to the customer that clicking this button will redirect
            them to the checkout page. It has an id for convention purposes and the class
            "buttonStyles" for styling.
            The div element is used to center the button on the page.--%>
            <div class="centerText">
                <input type="submit" value="Checkout" id="checkoutButton" class="buttonStyles centerElement">
            </div>
        </form>
    </body>
</html>

