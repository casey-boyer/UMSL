<%--
  Created by IntelliJ IDEA.
  User: Casey
  Date: 9/22/2018
  Time: 11:35 PM
  To change this template use File | Settings | File Templates.
--%>
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
            <c:if test="${not empty errorMessage}">
                <p id="scheduleError" class="errorMessage">
                    <c:out value="${errorMessage}"/>
                </p>
            </c:if>
            <%---This form, with the action attribute 'schedule', will redirect
            to the ScheduleServlet.---%>
            <c:if test="${empty schedule}">
                <form action="DBConn" name="studentForm" onsubmit="return validateStudent();">
                    <input type="hidden" name="action" value="student"/>

                    <div class="col-xs-12 col-sm-4" id="columnOne">
                        <p id="userInfoTitle" class="titles">Student Information:</p>

                        <label for="studentId" id="studentIdLabel" class="labels">Student id:</label>
                        <br/>
                        <p id="studentIdError" class="errorMessage" style="visibility: hidden;">
                            Student ID should contain only numbers.</p>
                        <input type="text" id="studentId" name="sId" value="${student.studentId}"
                               onkeyup="validateStudentId()" required><br/>


                        <label for="firstName" class="labels">First name:</label>

                        <br/>
                        <p id="firstNameError" class="errorMessage" style="visibility: hidden;">
                            First name should contain only alphabetic characters.
                        </p>
                        <input type="text" id="firstName" name="fName" value="${student.firstName}"
                               onkeyup="validateFirstName()" required/><br/>


                        <label for="lastName" class="labels">Last name:</label>

                        <br/>
                        <p id="lastNameError" class="errorMessage" style="visibility: hidden;">
                            Last name should contain only alphabetic characters.
                        </p>
                        <input type="text" id="lastName" name="lName" value="${student.lastName}"
                               onkeyup="validateLastName()" required/><br/>

                        <input type="submit" value="Submit"/>
                    </div>
                </form>
            </c:if>
            <c:if test="${not empty schedule}">
                <c:redirect url="schedule.jsp"/>
            </c:if>
    </body>
</html>
