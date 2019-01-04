<%--Add the taglib for the JSTL core library.--%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--The link to the stylesheet for the page.--%>
        <link rel="stylesheet" href="styles/checkout.css" type="text/css"/>
        <%--The title of the page, which will be displayed
        on the tab in the customer's browser, clearly
        indicates the purpose of the page.--%>
        <title>Thank You</title>
    </head>
    <body>
        <%--This div element displays the title of the page, and has
        the id "pageTitle" for styling, and the classes "centerText" and
        "pageHeader" for styling. The element clearly
        indicates to the customer that their information and requested
        DVD was processed and accepted.--%>
        <div id="pageTitle" class="pageHeader centerText">Thank you for your order!</div>
        
        <%--A p element which simply indicates to the customer that
        the text following it is their order request. It has an id
        for convention purposes, and belongs to the class "pageText"
        and "centerText" for styling.--%>
        <p id="pageDescription" class="pageText">Here is the information you 
            entered:</p>
        
        <%--The following labels output the "Email", "First Name", and "Last Name"
        text, and belong to the class pageLabels.
        The div elements display the customer's email, first name, and last name.
        Within the div elements, EL is used to access the user Object (stored in the
        session object) and the values of it's email, firstName, and lastName variables.--%>
        <label class="pageLabels" id="emailLabel">Email: </label>
            <div id="userEmail" class="pageText">${user.email}</div><br/>
        <label class="pageLabels" id="fNameLabel">First name: </label>
            <div id="userFName" class="pageText">${user.firstName}</div><br/>
        <label class="pageLabels" id="lNameLabel">Last name: </label>
            <div id="userLName" class="pageText">${user.lastName}</div><br/>
        
        <%--This table displays the DVD(s) the customer selected.
        The table has the classes pageText and tableStyle for styling.
        It contains 5 columns, with the headers cover, title, price, amount, and quantity.
        Each th element belongs to the class tableHeaders for styling.
        It uses a foreach loop to loop through the items ArrayList of the cart object forwarded by
        the session object.
        For each cover row, the for loop outputs the item's product's cover variable.
        For each title row, the for loop outputs the item's product's title variable.
        For each price row, the for loop outputs the item's product's getPriceCurrencyFormat()
        method, which returns the price of the DVD in text format.
        For each amount row, the for loop outputs the item's getTotalCurrencyFormat() method,
        which returns the total price of the DVD in text format.
        For each quantity row, the for loop outputs the item's quantity variable.--%>
        <table class="pageText tableStyle">
            <tr>
                <th class="tableHeaders">Cover</th>
                <th class="tableHeaders">Title</th>
                <th class="tableHeaders">Price</th>
                <th class="tableHeaders">Amount</th>
                <th class="tableHeaders">Quantity</th>
            </tr>
            <c:forEach var="item" items="${cart.items}">
            <tr>
                <td><img src="<c:out value='${item.product.cover}'/>" class="movieImages"/></td>
                <td><c:out value='${item.product.title}'/></td>
                <td>${item.product.priceCurrencyFormat}</td>
                <td><c:out value='${item.totalCurrencyFormat}'/></td>
                <td><c:out value='${item.quantity}'/></td>
            </tr>
            </c:forEach>
            <tr>
                <%--Display the user's price total. This is done by calling the
                getTotalCurrencyFormat() method of the Cart object stored in the session object.
                The td with id "totalAmount" styles it bold.--%>
                <td id="totalAmount">Total:</td>
                <td></td>
                <td></td>
                <td>${cart.totalCurrencyFormat}</td>
                <td></td>
            </tr>
        </table>
            
        <%--This p element indicates to the customer that they
        can order another DVD by clicking the back button in the browser
        or the return button within the hidden form below the text.
        It has an id for convention purposes and belongs to the class
        "pageText" and "centerText" for styling.--%>
        <p id="extraInfo" class="pageText centerText">To order another DVD, 
            click on the back button in your browser or the return
            button shown below.</p>
        
        <%--This is a hidden form which allows the user to return
        to the DVD order page to order another DVD. The action attribute
        of the form element is "order", so it corresponds with the CartServlet
        servlet mapping. The post method is used to not display any
        variables in the browser.--%>
        <form action="order" method="post">
            <%--This is a hidden input field with the name attribute
            "action" and the value "join". Thus, whenever the
            submit button is clicked, the servlet will request this
            action attribute, and since the value is "join", the servlet
            will set the url to "index.html" (this is done in the servlet).--%>
            <input type="hidden" name="action" value="join">
            
            <%--This input field is a submit button that displays the
            text "Return", so it is clearly indicated to the customer
            that clicking this button will return them to the DVD order page.
            It has an id for convention purposes and for styling.
            The div element belongs to the class centerText, which centers the button
            on the page.--%>
            <div class="centerText">
                <input type="submit" value="Return" id="submitButton">
            </div>
        </form>
    </body>
</html>
