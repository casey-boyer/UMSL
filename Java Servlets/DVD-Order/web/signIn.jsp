<%--Add the taglib for the JSTL core library.--%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--The link to the stylesheet for the page.--%>
        <link rel="stylesheet" href="styles/signIn.css" type="text/css"/>
        <%--The title of the page, which will be displayed
        on the tab in the customer's browser, clearly
        indicates the purpose of the page.--%>
        <title>Sign In</title>
    </head>
    <body>
        <%--This div element displays the title of the page, and has
        the id "pageTitle" for styling, and the classes "centerText" and
        "pageHeader" for styling.--%>
        <div id="pageTitle" class="centerText pageHeader">Sign In</div>
        <%--This p element displays the message string that the
        servlet forwards when it is set in the request attribute,
        and the url is redirected to index.jsp. The message string
        will either notify the user to select at least one DVD,
        or if the required attribute of the input boxes fails,
        it will tell the user to enter the necessary information
        in those boxes.--%>
        <p id="noMatch">
            <c:if test="${errMsg != null}">
                ${errMsg}
            </c:if>
        </p>
        <form action="order" method="post">
            <%--This is a hidden input field; although the
            customer cannot see it, the name attribute equals 
            "action" so the servlet can use the
            request.getParameter method; then, the servlet
            has the value "add" of the input field, so it
            can use this value to process the form data.--%>
            <input type="hidden" name="action" value="add"/>
            
            <%--The label for the customer's email. Has an id to identify the element,
            and belongs to the class "pageLabels" for styling. Additionally, the "for"
            attribute specifies that when the input box with the id "userEmail" is clicked,
            it will get focus.--%>
            <label class="pageLabels" id="emailLabel" for="userEmail">Email:</label>
                <%--The input box for the customer's email. Specifies that this is
                    an email input box, has the id "userEmail" (so when the customer
                    clicks on the email label, the input box gets focus), has the name
                    "userEmailInput" so the servlet can extract the contents of it,
                    and belongs to the class "inputBoxes" for styling. Additionally,
                    the pattern attribute specifies that the email must begin
                    with an alphabetic character (case insensitive), a number, or
                    underscore, followed by an '@' symbol, followed by one or more
                    alphabetic characters (case insensitive), followed by one period,
                    and then followed by one or more alphabetic characters
                    (case insensitive). For example, the customer may enter
                    "John99_Smith@movies.com", but not "J!n@99..12321". Also,
                    this input box has the required attribute, so the customer
                    must fill out the input box or otherwise cannot submit the form.
                    The value attribute is EL, so if the user refreshes the page,
                    clicks the back button, or if they do not select a dvd, the
                    name they typed is still available.
                    --%>
                    <input type="email" id="userEmail" name="userEmailInput" class="inputBoxes"
                           pattern="[A-z|0-9|_]+[@]{1}[A-z]+[.]{1}[A-z]+" required 
                           value="${user.email}"/>
                    <br/>
                
            <%--The label for the customer's password. Has an id to identify the element,
            and belongs to the class "pageLabels" for styling. Additionally, the "for"
            attribute specifies that when the label is clicked, the input box with the
            id "userPass" will get focus.--%>
            <label class="pageLabels" id="passwordLabel" for="userPass">Password:</label>
            
                <%--The input box for the customer's password. Specifies that this is a
                password input box (so the characters are masked), has the id "userPass" 
                (so when the customer clicks on the password label, the input box gets focus),
                has the name "pwd" so the servlet can extract the contents of it, and
                belongs to the class "inputBoxes" for styling. Additionally, the input box
                has the required attribute, so the customer cannot submit the form without
                providing a value for the input box.--%>
                <input type="password" id="userPass" name="pwd" class="inputBoxes" required/>
                <br/>
                
            <%--The submit button for the form, which displays the text "Submit"--%>
            <input type="submit" value="Submit" id="submitButton"/>
        </form>
    </body>
</html>
