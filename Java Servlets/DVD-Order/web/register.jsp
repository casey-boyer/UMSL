<%--Add the taglib for the JSTL core library.--%>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--The link to the stylesheet for the page.--%>
        <link rel="stylesheet" href="styles/register.css" type="text/css"/>
        <%--The title of the page, which will be displayed
        on the tab in the customer's browser, clearly
        indicates the purpose of the page.--%>
        <title>Register</title>
    </head>
    <body>
        <%--This div element displays the title of the page, and has
        the id "pageTitle" for styling, and the classes "centerText" and
        "pageHeader" for styling.--%>
        <div id="pageTitle" class="centerText pageHeader">New User Registration</div>
        
        <%--This p element displays the message string that the
        servlet forwards when it is set in the request attribute.
        The message string will either:
            1. Notify the user that the email entered is unavailable, if
                the user changes the email to one that exists in the User table,
            2. Notify the user if the information provided in the form could
                not sucessfully be inserted into the User table,
            3. If the required attribute of the input boxes fails,
                it will tell the user to enter the necessary information
                in those boxes.--%>
        <div id="noMatch">
            <c:if test="${msg != null}">
                *${msg}
            </c:if>
        </div><br/>
        
        <%--This form processes the information entered by the user.
        It has the action "order" so the servlet can process the request.--%>
        <form action="order" method="post">
            <%--A hidden input field with the name "action" and value "register."
            When the user submits the form, the servlet will retrieve the value of
            the input field with the name "action," and will use the
            "register" value to process the user's information and appropriately
            redirect the user.--%>
            <input type="hidden" name="action" value="register"/>
            
            <%--This is the label for the Email input box. The for attribute
            specifies that when the label is clicked, the corresponding input
            box with the same id gets focus. Additionally, it has it's
            own id (for convention purposes) and is part of the
            class "pageLabels" for styling.--%>
            <label id="emailLabel" class="pageLabels" for="userEmailInput">Email:</label>
            
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
                The value attribute is equal to the email value of the user object that
                is stored in the session object.
                --%>
                <input type="email" id="userEmail" name="userEmailInput" class="inputBoxes"
                        pattern="[A-z|0-9|_]+[@]{1}[A-z]+[.]{1}[A-z]+" required 
                        value="${user.email}"/><br/>
            
            <%--This is the label for the password input box. The for attribute
            specifies that when the label is clicked, the corresponding input
            box with the same id gets focus. Additionally, it has it's
            own id (for convention purposes) and is part of the
            class "pageLabels" for styling.--%>
            <label id="passwordLabel" class="pageLabels" for="userPass">Password:</label>
            
                <%--The input box for the customer's password. Specifies that this
                is a password input box (so the characters entered are masked), has
                the id "userPass" (so when the customer clicks on the password label,
                the input box will be selected), has the name "pwd" so the servlet
                can extract text from it, and belongs to the class "inputBoxes" for
                styling. It also has the required attribute so the customer
                cannot submit the form without filling in this input box.
                The value attribute is equal to the password value of the user object
                that is stored in the session object.--%>
                <input type="password" id="userPass" name="pwd" class="inputBoxes" required
                       value="${user.password}"/><br/>
                
            <%--This is the label for the first name input box. The for attribute
            specifies that when the label is clicked, the corresponding input
            box with the same id gets focus. Additionally, it has it's
            own id (for convention purposes) and is part of the
            class "pageLabels" for styling.--%>
            <label id="firstNameLabel" class="pageLabels" for="userFirstName">First Name:</label>
                <%--The input box for the customer's first name. Specifies that this
                is a text input box, has the id "userFirstName" (so when the customer
                clicks on the name label, the input box will be selected), has
                the name "userFNameInput" so the servlet can extract the text from it,
                and belongs to the class "inputBoxes" for styling. Additionally, the
                pattern attribute specifies the first character must be alphabetic,
                followed by one or more alphabetic characters or a whitespace
                (case insensitive). It also has the required attribute so the
                customer cannot submit the form without filling in this input box.--%>
                <input type="text" id="userFirstName" name="userFNameInput" class="inputBoxes"
                       pattern="[A-Za-z ]+" required/><br/>
                
            <%--This is the label for the last name input box. The for attribute
            specifies that when the label is clicked, the corresponding input
            box with the same id gets focus. Additionally, it has it's
            own id (for convention purposes) and is part of the
            class "pageLabels" for styling.--%>
            <label id="lastNameLabel" class="pageLabels" for="userLastName">Last Name:</label>
                <%--The input box for the customer's last name. Specifies that this
                is a text input box, has the id "userLastName" (so when the customer
                clicks on the name label, the input box will be selected), has
                the name "userLNameInput" so the servlet can extract the text from it,
                and belongs to the class "inputBoxes" for styling. Additionally, the
                pattern attribute specifies the first character must be alphabetic,
                followed by one or more alphabetic characters or a whitespace
                (case insensitive). It also has the required attribute so the
                customer cannot submit the form without filling in this input box.--%>
                <input type="text" id="userLastName" name="userLNameInput" class="inputBoxes"
                       pattern="[A-Za-z ]+" required/><br/>
                
            <%--The submit button for the form. It displays the text "Join Now" and
            has the id "submitButton" for styling.--%>
            <input type="submit" value="Join Now" id="submitButton"/>
        </form>
    </body>
</html>
