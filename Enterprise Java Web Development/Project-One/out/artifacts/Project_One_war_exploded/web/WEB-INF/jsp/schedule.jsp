<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%---TRY TO ADD THE FOLLOWING IN base.jspf---%>
<%@ page import="edu.umsl.model.Student" %>
<%@ page import="edu.umsl.model.Course" %>
<%@ page isELIgnored="false" %>
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
        <title>Course Schedule</title>
    </head>

    <body>
        <div class="row" id="rowOne">
            <%---This form, with the action attribute 'schedule', will redirect
            to the ScheduleServlet.---%>
            <form name="courseForm" onsubmit="validateForm()">
                <input type="hidden" name="action" value="add"/>

                <div class="col-sm-4" id="columnOne">
                    <p id="userInfoTitle" class="titles">Student Information:</p>

                    <label for="studentId" id="studentIdLabel" class="labels">Student id:</label><br/>
                    <input type="text" id="studentId" name="sId" value="${student.studentId}"
                           required><br/>

                    <label for="firstName" class="labels">First name:</label><br/>
                    <input type="text" id="firstName" name="fName" value="${student.firstName}"
                           required/><br/>

                    <label for="lastName" class="labels">Last name:</label><br/>
                    <input type="text" id="lastName" name="lName" value="${student.lastName}"
                           required/><br/>
                </div>

                <div class="col-sm-8" id="columnTow">
                    <p id="courseInfoTitle" class="titles">Course Information: </p>

                    <label for="courseId" class="labels">Course ID:</label><br/>
                    <input type="text" id="courseId" name="cId" required/><br/>

                    <label for="courseName" class="labels">Course name:</label><br/>
                    <input type="text" id="courseName" name="cName" required/><br/>

                    <label for="courseNumber" class="labels">Course number:</label><br/>
                    <input type="text" id="courseNumber" name="cNum" required/><br/>

                    <label for="startCourseTime" class="labels">Start course time:</label><br/>
                    <input type="text" id="startCourseTime" name="cStart" required/><br/>

                    <label for="endCourseTime" class="labels">End course time:</label><br/>
                    <input type="text" id="endCourseTime" name="cEnd" required/><br/>

                    <%---Need to fix this for mobile!!!!---%>
                    <span id="courseDaysCheckboxes" class="labels">Course days:</span><br/>

                    <label for="sunday" class="labels">Sunday:</label>
                    <label for="monday" class="labels">Monday:</label>
                    <label for="tuesday" class="labels">Tuesday:</label>
                    <label for="wednesday" class="labels">Wednesday:</label>
                    <label for="thursday" class="labels">Thursday:</label>
                    <label for="friday" class="labels">Friday:</label>
                    <label for="saturday" class="labels">Saturday:</label>
                    <br/>
                    <input type="checkbox" id="sunday" name="courseDays" value="SUNDAY"/>
                    <input type="checkbox" id="monday" name="courseDays" value="MONDAY"/>
                    <input type="checkbox" id="tuesday" name="courseDays" value="TUESDAY"/>
                    <input type="checkbox" id="wednesday" name="courseDays" value="WEDNESDAY"/>
                    <input type="checkbox" id="thursday" name="courseDays" value="THURSDAY"/>
                    <input type="checkbox" id="friday" name="courseDays" value="FRIDAY"/>
                    <input type="checkbox" id="saturday" name="courseDays" value="SATURDAY"/>
                    <br/>

                </div>

                <input type="submit" value="Submit"/>
            </form>
        </div>

        <%---Since each courseDays value for a course is an array, need to also loop
        thru this---%>
        <c:forEach items="${courses}" var="course">
            ${course.courseId} <br/>
            ${course.courseName} <br/>
            ${course.courseNumber} <br/>
            ${course.courseStart} <br/>
            ${course.courseEnd} <br/>

        </c:forEach>
    </body>
</html>
