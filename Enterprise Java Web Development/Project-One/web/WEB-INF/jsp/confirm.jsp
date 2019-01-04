<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Confirmation</title>

        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Latest compiled and minified CSS; for Bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <!-- jQuery library; for Bootstrap -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <!-- Latest compiled JavaScript; for Bootstrap -->
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

        <link rel="stylesheet" type="text/css" href="css/styles.css">
    </head>
    <body>
        <p class="titles">COURSE SCHEDULE: </p>
        <div class="row" id="studentInfoRow">
            <div class="col-xs-12">
                <p id="userInfoTitle" class="titles">Student Information:</p>

                <label for="studentId" id="studentIdLabel" class="labels">Student id:</label>
                <span id="studentId" class="values">${student.studentId}</span><br/>

                <label for="firstName" class="labels">First name:</label>
                <span id="firstName" class="values">${student.firstName}</span><br/>


                <label for="lastName" class="labels">Last name:</label>
                <span id="lastName" class="values">${student.lastName}</span><br/>

                <br/><br/>
                <label for="scheduleName" class="schedule">Schedule: </label>
                <span id="scheduleName" class="values">"${schedule.name}"</span><br/>
            </div>
        </div>
        <div class="container-fluid">
            <%---This is the row that will display the user's schedule as a calendar.---%>
            <div class="row" id="scheduleRow">
                <div class="col-xs-12">
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
                    The below HTML is always shown, as the confirm.jsp page is only reached after
                    the user submits their schedule, so the Schedule object will be present in the
                    session object.---%>
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
