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

        <title>Course Schedule</title>
    </head>

    <body>
        <div class="row" id="rowOne">
            <c:if test="${not empty errorMessage}">
                <p id="scheduleError" class="errorMessage">
                    <c:out value="${errorMessage}"></c:out>
                </p>
            </c:if>
            <%---This form, with the action attribute 'schedule', will redirect
            to the ScheduleServlet.---%>
            <form action="schedule" name="courseForm" onsubmit="return validate();">
                <input type="hidden" name="action" value="add"/>

                <div class="col-xs-12 col-sm-4" id="columnOne">
                    <p id="userInfoTitle" class="titles">Student Information:</p>

                    <label for="studentId" id="studentIdLabel" class="labels">Student id:</label>
                    <span id="studentId" class="values">${student.studentId}</span><br/>

                    <label for="firstName" class="labels">First name:</label>
                    <span id="firstName" class="values">${student.firstName}</span><br/>


                    <label for="lastName" class="labels">Last name:</label>
                    <span id="lastName" class="values">${student.lastName}</span><br/>
                </div>

                <div class="col-xs-12 col-sm-8" id="columnTwo">
                    <p id="courseInfoTitle" class="titles">Course Information: </p>

                    <label for="courseName" class="labels">Course name:</label><br/>
                    <p id="courseNameError" class="errorMessage" style="visibility: hidden;">
                        Course name should contain only alphabetic characters.</p>
                    <input type="text" id="courseName" name="cName" onkeyup="validateCourseName()"
                           required/><br/>

                    <label for="courseNumber" class="labels">Course number:</label><br/>
                    <p id="courseNumberError" class="errorMessage" style="visibility: hidden;">
                        Course number should contain only numbers.</p>
                    <input type="text" id="courseNumber" name="cNum"
                           onkeyup="validateCourseNumber()" required/><br/>

                    <label for="startCourseTime" class="labels">Start course time:</label><br/>
                    <p id="startCourseTimeError" class="errorMessage" style="visibility: hidden;">
                        The course time must be in HH:MM format, between the hours of 08:00AM-10:00PM.</p>
                    <input type="text" id="startCourseTime" name="cStart"
                           onkeyup="validateStartCourseTime()" required/>
                    <select id="courseStartAMPM" name="courseStartTimeOfDay">
                        <option value="AM" name="cStartAM">A.M.</option>
                        <option value="PM" name="cStartPM">P.M.</option>
                    </select>
                    <br/>

                    <label for="endCourseTime" class="labels">End course time:</label><br/>
                    <p id="endCourseTimeError" class="errorMessage" style="visibility: hidden;">
                        The course time must be in HH:MM format, between the hours of 08:00AM-10:00PM.</p>
                    <input type="text" id="endCourseTime" name="cEnd"
                           onkeyup="validateEndCourseTime()" required/>
                    <select id="courseEndAMPM" name="courseEndTimeOfDay">
                        <option value="AM" name="cEndAM">A.M.</option>
                        <option value="PM" name="cEndPM">P.M.</option>
                    </select>
                    <br/>


                    <span id="courseDaysCheckboxes" class="labels">Course days:</span><br/>

                    <table id="daysCheckboxTable">
                        <tr>
                            <th><span class="labels">Monday:</span></th>
                            <th><span class="labels">Tuesday:</span></th>
                            <th><span class="labels">Wednesday:</span></th>
                            <th><span class="labels">Thursday:</span></th>
                            <th><span class="labels">Friday:</span></th>
                        </tr>
                        <tr>
                            <td>
                                <input type="checkbox" id="monday" name="courseDays" value="MONDAY"/>
                            </td>
                            <td>
                                <input type="checkbox" id="tuesday" name="courseDays" value="TUESDAY"/>
                            </td>
                            <td>
                                <input type="checkbox" id="wednesday" name="courseDays" value="WEDNESDAY"/>
                            </td>
                            <td>
                                <input type="checkbox" id="thursday" name="courseDays" value="THURSDAY"/>
                            </td>
                            <td>
                                <input type="checkbox" id="friday" name="courseDays" value="FRIDAY"/>
                            </td>
                        </tr>
                    </table>

                    <input type="submit" value="Add Course"/>
                </div>
            </form>
        </div>
        <div class="row" id="rowTwo">
            <div class="col-sm-4"></div>
            <div class="col-sm-8">
                <form action="DBConn" name="submitForm" method="POST"
                      onsubmit="return validateScheduleName()">
                    <input type="hidden" name="action" value="confirm">

                    <label for="scheduleName" class="labels" id="scheduleLabel">Schedule Name:</label>
                    <br/>
                    <c:if test="${empty schedule.name}">
                        <p class="errorMessage" style="visibility: hidden;" id="scheduleNameError">
                            Please enter a schedule name consisting of only alphabetic characters.
                        </p>
                        <input type="text" id="scheduleName" name="sName"
                               onkeyup="validateScheduleName()" required/>
                        <br/>
                    </c:if>
                    <c:if test="${not empty schedule.name}">
                        <span class="values" id="scheduleNameValue">${schedule.name}</span>
                    </c:if>

                    <input type="submit" value="Submit Schedule">
                </form>

                <c:if test="${not empty conflict}">
                    <div id="courseConflict" class="errorMessage">
                        FAILED TO ADD COURSE.<br/>
                        The course you attempted to add:<br/>
                        <p class="conflictCourse" id="failedCourse">
                            Course ID: ${addedCourse.courseId}<br/>
                            Course Name: ${addedCourse.courseName}<br/>
                            Course Number: ${addedCourse.courseNumber}<br/>
                            Time: ${addedCourse.courseStart} - ${addedCourse.courseEnd}
                        </p>
                        interferes with the following course you already added:
                        <p class="conflictCourse" id="conflictCourse">
                            Course ID: ${conflict.courseId}<br/>
                            Course Name: ${conflict.courseName}<br/>
                            Course Number: ${conflict.courseNumber}<br/>
                            Time: ${conflict.courseStart} to ${conflict.courseEnd}
                        </p><br/>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="container-fluid">
            <div class="row" id="rowThree">
                <%---Since each courseDays value for a course is an array, need to also loop
                thru this---%>
                <div class="col-xs-12">
                    <c:if test="${not empty schedule.courseMap}">
                        <div class="grid-container" id="scheduleCalendar">
                            <div class="grid-item" id="mondayColumn">
                                <p id="mondayTitle" class="titles">Monday</p>
                                <c:set var="lastEndTime" value="0"/>
                                <c:forEach items="${schedule.courseMap['Monday']}" var="course">
                                    <c:set var="displacement" value="${course.courseStartMinutes - lastEndTime}"/>
                                    <p class="courseItem" style="height: ${course.courseLength}px;
                                            margin-top: ${displacement}px;">
                                        <c:set var="lastEndTime" value="${course.courseEndMinutes}"/>
                                        Course Name: ${course.courseName} <br/>
                                        Course Number:${course.courseNumber} <br/>
                                        Times: ${course.courseStart} - ${course.courseEnd} <br/>
                                    </p>
                                </c:forEach>
                            </div>
                            <div class="grid-item" id="tuesdayColumn">
                                <p id="tuesdayTitle" class="titles">Tuesday</p>
                                <c:set var="lastEndTime" value="0"/>
                                <c:forEach items="${schedule.courseMap['Tuesday']}" var="course">
                                    <c:set var="displacement" value="${course.courseStartMinutes - lastEndTime}"/>
                                    <p class="courseItem" style="height: ${course.courseLength}px;
                                            margin-top: ${displacement}px;">
                                        <c:set var="lastEndTime" value="${course.courseEndMinutes}"/>
                                        Course Name: ${course.courseName} <br/>
                                        Course Number:${course.courseNumber} <br/>
                                        Times: ${course.courseStart} - ${course.courseEnd} <br/>
                                    </p>
                                </c:forEach>
                            </div>
                            <div class="grid-item" id="wednesdayColumn">
                                <p id="wednesdayTitle" class="titles">Wednesday</p>
                                <c:set var="lastEndTime" value="0"/>
                                <c:forEach items="${schedule.courseMap['Wednesday']}" var="course">
                                    <c:set var="displacement" value="${course.courseStartMinutes - lastEndTime}"/>
                                    <p class="courseItem" style="height: ${course.courseLength}px;
                                            margin-top: ${displacement}px;">
                                        <c:set var="lastEndTime" value="${course.courseEndMinutes}"/>
                                        Course Name: ${course.courseName} <br/>
                                        Course Number:${course.courseNumber} <br/>
                                        Times: ${course.courseStart} - ${course.courseEnd} <br/>
                                    </p>
                                </c:forEach>
                            </div>
                            <div class="grid-item" id="thursdayColumn">
                                <p id="thursdayTitle" class="titles">Thursday</p>
                                <c:set var="lastEndTime" value="0"/>
                                <c:forEach items="${schedule.courseMap['Thursday']}" var="course">
                                    <c:set var="displacement" value="${course.courseStartMinutes - lastEndTime}"/>
                                    <p class="courseItem" style="height: ${course.courseLength}px;
                                            margin-top: ${displacement}px;">
                                        <c:set var="lastEndTime" value="${course.courseEndMinutes}"/>
                                        Course Name: ${course.courseName} <br/>
                                        Course Number:${course.courseNumber} <br/>
                                        Times: ${course.courseStart} - ${course.courseEnd} <br/>
                                    </p>
                                </c:forEach>
                            </div>
                            <div class="grid-item" id="fridayColumn">
                                <p id="fridayTitle" class="titles">Friday</p>
                                <c:set var="lastEndTime" value="0"/>
                                <c:forEach items="${schedule.courseMap['Friday']}" var="course">
                                    <c:set var="displacement" value="${course.courseStartMinutes - lastEndTime}"/>
                                    <p class="courseItem" style="height: ${course.courseLength}px;
                                            margin-top: ${displacement}px;">
                                        <c:set var="lastEndTime" value="${course.courseEndMinutes}"/>
                                        Course Name: ${course.courseName} <br/>
                                        Course Number:${course.courseNumber} <br/>
                                        Times: ${course.courseStart} - ${course.courseEnd} <br/>
                                    </p>
                                </c:forEach>
                            </div>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </body>
</html>
