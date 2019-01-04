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
            <%---If the errorMessage attribute of the request object is not empty, display
            the errorMessage.---%>
            <c:if test="${not empty errorMessage}">
                <p id="scheduleError" class="errorMessage">
                    <c:out value="${errorMessage}"></c:out>
                </p>
            </c:if>

            <%---This form, with the action attribute 'schedule', will submit the form data
            to the ScheduleServlet. When the form is submitted, the return value of the
            validate() javascriptvfunction is returned. If it returns true, the form data
            will be submitted to the ScheduleServlet. If it returns false, the request will
            not go to the server and the user must correct invalid input.---%>
            <form action="schedule" name="courseForm" onsubmit="return validate();">
                <%---Upon successful submission of the form, this hidden input field will
                be used by the ScheduleServlet to indicate that it should
                add the course to the Schedule object.---%>
                <input type="hidden" name="action" value="add"/>

                <div class="col-xs-12 col-sm-4" id="columnOne">
                    <p id="userInfoTitle" class="titles">Student Information:</p>

                    <%---Display the student information that was entered on the studentInfo.jsp
                    page. The student information from this page was used to create a Student object,
                    and the Student object was stored in the session object, so EL can be used to
                    retrieve its attributes.---%>

                    <%---Display the studentId attribute of the Student object.---%>
                    <label for="studentId" id="studentIdLabel" class="labels">Student id:</label>
                    <span id="studentId" class="values">${student.studentId}</span><br/>

                    <%---Display the firstName attribute of the Student object.---%>
                    <label for="firstName" class="labels">First name:</label>
                    <span id="firstName" class="values">${student.firstName}</span><br/>

                    <%---Display the lastName attribute of the Student object.---%>
                    <label for="lastName" class="labels">Last name:</label>
                    <span id="lastName" class="values">${student.lastName}</span><br/>
                </div>

                <div class="col-xs-12 col-sm-8" id="columnTwo">
                    <p id="courseInfoTitle" class="titles">Course Information: </p>

                    <%---Get the course name for the course.---%>
                    <label for="courseName" class="labels">Course name:</label><br/>
                    <p id="courseNameError" class="errorMessage" style="visibility: hidden;">
                        Course name should contain only alphabetic characters.</p>
                    <%---Input field for the name of the course. As the user is typing,
                    validate the course name with the validateCourseName() javascript function.---%>
                    <input type="text" id="courseName" name="cName" onkeyup="validateCourseName()"
                           required/><br/>

                    <%---Get the course number for the course.---%>
                    <label for="courseNumber" class="labels">Course number:</label><br/>
                    <p id="courseNumberError" class="errorMessage" style="visibility: hidden;">
                        Course number should contain only numbers.</p>
                    <%---Input field for the number of the course. As the user is typing,
                    validate the course number with the validateCourseNumber() javascript function.---%>
                    <input type="text" id="courseNumber" name="cNum"
                           onkeyup="validateCourseNumber()" required/><br/>

                    <%---Get the course start time for the course.---%>
                    <label for="startCourseTime" class="labels">Start course time:</label><br/>
                    <p id="startCourseTimeError" class="errorMessage" style="visibility: hidden;">
                        The course time must be in HH:MM format, between the hours of 08:00AM-10:00PM.</p>
                    <%---Input field for the start time of the course. As the user is typing,
                    validate the course start time with the validateCourseStartTime()
                    javascript function.---%>
                    <input type="text" id="startCourseTime" name="cStart"
                           onkeyup="validateStartCourseTime()" required/>
                    <%---Get the time of day, AM or PM, of the course start time.---%>
                    <select id="courseStartAMPM" name="courseStartTimeOfDay">
                        <option value="AM" name="cStartAM">A.M.</option>
                        <option value="PM" name="cStartPM">P.M.</option>
                    </select>
                    <br/>

                    <%---Get the course end time for the course.---%>
                    <label for="endCourseTime" class="labels">End course time:</label><br/>
                    <p id="endCourseTimeError" class="errorMessage" style="visibility: hidden;">
                        The course time must be in HH:MM format, between the hours of 08:00AM-10:00PM.</p>
                    <%---Input field for the end time of the course. As the user is typing,
                    validate the course end time with the validateCourseEndTime()
                    javascript function.---%>
                    <input type="text" id="endCourseTime" name="cEnd"
                           onkeyup="validateEndCourseTime()" required/>
                    <%---Get the time of day, AM or PM, of the course end time.---%>
                    <select id="courseEndAMPM" name="courseEndTimeOfDay">
                        <option value="AM" name="cEndAM">A.M.</option>
                        <option value="PM" name="cEndPM">P.M.</option>
                    </select>
                    <br/>

                    <span id="courseDaysCheckboxes" class="labels">Course days:</span><br/>
                    <%---Get the course days for the course.---%>
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

                    <%---The submit button that will submit the courseForm form data to the server,
                    if the validate() function returns true.---%>
                    <input type="submit" value="Add Course"/>
                </div>
            </form>
        </div>
        <div class="row" id="rowTwo">
            <div class="col-sm-4"></div>
            <div class="col-sm-8">
                <%---This form, with the action attribute 'DBConn', will submit the form data
                to the DBConnServlet. When the form is submitted, the return value of the
                validateScheduleName() javascriptvfunction is returned. If it returns true, the form data
                will be submitted to the DBConnServlet. If it returns false, the request will
                not go to the server and the user must correct invalid input.---%>
                <form action="DBConn" name="submitForm" method="POST"
                      onsubmit="return validateScheduleName()">

                    <%---Upon successful submission of the form, this hidden input field will
                    be used by the DBConnServlet to indicate that it should add the course(s)
                    and schedule into the course and schedule tables of the enterprise database.---%>
                    <input type="hidden" name="action" value="confirm">

                    <%---Get the name of the schedule from the user.---%>
                    <label for="scheduleName" class="labels" id="scheduleLabel">Schedule Name:</label>
                    <br/>
                    <%---If the user has not yet submitted the form, then the
                    name attribute of the Schedule object that is stored in the session
                    object will be empty. Thus, get the user's input for the schedule name
                    by displaying the input field for it.---%>
                    <c:if test="${empty schedule.name}">
                        <p class="errorMessage" style="visibility: hidden;" id="scheduleNameError">
                            Please enter a schedule name consisting of only alphabetic characters.
                        </p>
                        <%---The input field for the name of the schedule. As the user is typing,
                        the validateScheduleName() javascript function will test the user
                        input.---%>
                        <input type="text" id="scheduleName" name="sName"
                               onkeyup="validateScheduleName()" required/>
                        <br/>
                    </c:if>

                    <%---If the name attribute of the schedule object is not empty,
                    that is, the user has already submitted this form to the server and clicked
                    back after going to confirm.jsp to add more courses, then simply
                    display the name attribute of the schedule. It is not necessary to get
                    the name attribute from the user again since it has already been set, and
                    this condition is true when the schedule has already been submittted to
                    the database.---%>
                    <c:if test="${not empty schedule.name}">
                        <span class="values" id="scheduleNameValue">${schedule.name}</span>
                    </c:if>

                    <input type="submit" value="Submit Schedule">
                </form>

                <%---If the user attempted to add a course with times that conflicted with
                another course already previously added, then the conflict attribute of the
                request object will be set, so display the errorMessage that was set.---%>
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
            <%---This is the row that will display the user's schedule as a calendar.---%>
            <div class="row" id="rowThree">

                <%---Since a Course object may occur on Monday-Friday, but not necessarily
                all of those days, it is necessary to iterate through each key of the
                courseMap HashMap (where each key is the day of the week) to get the courses
                that occur on that day and display them in the schedule display.
                In order to pad and lengthen a course on the schedule display according to time,
                two variables are created:
                    lastEndTime, which is the end time of the last course. It initially starts
                    at 0, to represent an offset from 08:00A.M., since this is the earliest
                    time a course may begin. During each iteration of the current ArrayList value
                    associated with the key (day of the week), this variable is updated to
                    hold the value of the previous course's end time, in minutes.

                    displacement, which is how far 'down' a course should be padded (using margin) in the
                    column. During each loop iteration, the value of displacement is the start
                    of the course, in minutes (the courseStartMinutes attribute of a Course
                    object), minus the lastEndTime. Then, this value is used to set the margin-top
                    style of the course. This allows a course that occurs on multiple days to
                    always appear in the same 'row' as it does on other days, regardless of
                    courses that may occur on that day.

                The courseName, courseNumber, courseStart, and courseEnd attributes of the Course
                object(s) in each ArrayList value are used to display information about the course.
                The below HTML is only shown if the Schedule object is present in the
                session object; that is, the user has entered at least one course.---%>
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
