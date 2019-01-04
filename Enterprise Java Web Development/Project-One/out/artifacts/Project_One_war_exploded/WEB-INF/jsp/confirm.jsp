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
            <div class="row" id="scheduleRow">
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
