<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Latest compiled and minified CSS; for Bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <!-- jQuery library; for Bootstrap -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <!-- Latest compiled JavaScript; for Bootstrap -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <link rel="stylesheet" type="text/css" href="css/styles.css">
        <%---After editing the 'war exploded' artifact, this works, despite it still giving an error---%>
        <script type="text/javascript" src="javascript/formValidate.js"></script>

        <title>Student Information</title>
    </head>
    <body>
        <div class="row" id="rowOne">
            <%---If the errorMessage attribute of the request object is not empty, display
            the errorMessage.---%>
            <c:if test="${not empty errorMessage}">
                <p id="scheduleError" class="errorMessage">
                    <c:out value="${errorMessage}"/>
                </p>
            </c:if>

            <%---This form, with the action attribute 'DBConn', will redirect
            to the DBConnServlet to insert the student data into the student table of the
            enterprise database.
            When the form is submitted, the return value of the validateStudent() javascript
            function is returned. If it returns true, the form data will be submitted to the
            DBConnServlet. If it returns false, the request will not go to the server and
            the user must correct invalid input.---%>
            <form action="DBConn" name="studentForm" onsubmit="return validateStudent();">
                <%---Upon successful submission of the form, this hidden input field will
                be used by the DBConnServlet to indicate that it should
                add the student to the database.---%>
                <input type="hidden" name="action" value="student"/>

                <div class="col-xs-12 col-sm-4" id="columnOne">
                    <p id="userInfoTitle" class="titles">Student Information:</p>

                    <%---Get the student ID.---%>
                    <label for="studentId" id="studentIdLabel" class="labels">Student id:</label>
                    <br/>
                    <p id="studentIdError" class="errorMessage" style="visibility: hidden;">
                        Student ID should contain only numbers.</p>
                    <%---If the user already submitted the form, the studentId attribute of
                    the Student object in the session will be displayed.
                    As the user is typing their input, the validateStudentId() function
                    will be called.---%>
                    <input type="text" id="studentId" name="sId" value="${student.studentId}"
                           onkeyup="validateStudentId()" required><br/>

                    <%---Get the first name of the student.---%>
                    <label for="firstName" class="labels">First name:</label>
                    <br/>
                    <p id="firstNameError" class="errorMessage" style="visibility: hidden;">
                        First name should contain only alphabetic characters.
                    </p>
                    <%---If the user already submitted the form, the firstName attribute of
                    the Student object in the session will be displayed.
                    As the user is typing their input, the validateFirstName() function
                    will be called.---%>
                    <input type="text" id="firstName" name="fName" value="${student.firstName}"
                           onkeyup="validateFirstName()" required/><br/>

                    <%---Get the last name of the student.---%>
                    <label for="lastName" class="labels">Last name:</label>
                    <br/>
                    <p id="lastNameError" class="errorMessage" style="visibility: hidden;">
                        Last name should contain only alphabetic characters.
                    </p>
                    <%---If the user already submitted the form, the lastName attribute of
                    the Student object in the session will be displayed.
                    As the user is typing their input, the validateLastName() function
                    will be called.---%>
                    <input type="text" id="lastName" name="lName" value="${student.lastName}"
                           onkeyup="validateLastName()" required/><br/>

                    <%---Submit button to submit the form data to the server.---%>
                    <input type="submit" value="Submit"/>
                </div>
            </form>
    </body>
</html>
