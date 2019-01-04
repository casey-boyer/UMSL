invalidInputStyle = "background-color: #da817c;";
correctInputStyle = "background-color: none;";

displayError = "visibility: visible;";
hideError = "visibility: hidden;";

function validateStudent() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    var formName = "studentForm";
    var studentId = document.forms[formName]["studentId"].value;
    var firstName = document.forms[formName]["firstName"].value;
    var lastName = document.forms[formName]["lastName"].value;

    if (isEmpty(studentId) || isEmpty(firstName) || isEmpty(lastName)) {
        return false;
    }
    else if (numPattern.test(studentId)) {
        return false;
    }
    else if (namePattern.test(firstName)) {
        return false;
    }
    else if (namePattern.test(lastName)) {
        return false;
    }
    else {
        return true;
    }
}

function validate() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    //Find a match for a time in the format 00:00 to 12:59
    var timePattern = /^(?:1[0-2]|0[0-9]):[0-5][0-9]$/;

    var formName = "courseForm";
    var courseName = document.forms[formName]["cName"].value;
    var courseNumber = document.forms[formName]["cNum"].value;
    var courseStart = document.forms[formName]["cStart"].value;
    var courseEnd = document.forms[formName]["cEnd"].value;
    var courseDays = document.getElementsByName("courseDays");

    var numUnchecked = 0;
    for(var i = 0; i < courseDays.length; i++) {
        if (!courseDays[i].checked) {
            numUnchecked++;
        }
    }

    if (numUnchecked === courseDays.length) {
        console.log("no days checked");
        return false;
    }

    if (isEmpty(courseName) || isEmpty(courseNumber)
        || isEmpty(courseStart) || isEmpty(courseEnd)) {
        return false;
    }
    else if (namePattern.test(courseName)) {
        return false;
    }
    else if (numPattern.test(courseNumber)) {
        return false;
    }
    else if (!timePattern.test(courseStart)) {
        window.alert("Incorrect time");
        console.log("incorrect time");
        return false;
    }
    else if(!timePattern.test(courseEnd)) {
        console.log("incorrect end time");
        return false;
    }
    else if (!validateTime(courseStart, courseEnd)) {
        console.log("returning false for validate time");
        return false;
    }
    else {
        console.log("passed all conditions");
        return true;
    }
}

function validateTime(startTime, endTime) {
    var startHour = Number(startTime.substring(0, 2));
    var startHourTime = document.getElementById("courseStartAMPM").value;
    var endHour = Number(endTime.substring(0, 2));
    var endHourTime = document.getElementById("courseEndAMPM").value;

    if (startHourTime === "AM") {
        if (!/^(?:1[01]|0[89]):[0-5][0-9]$/.test(startTime)) {
            console.log("Invalid start time for AM");
            return false;
        }
    }
    else {
        if (!/^(?:1[02]|0[0-9]):[0-5][0-9]$/.test(startTime)) {
            console.log("Invalid start time for PM");
            return false;
        }
    }

    if (endHourTime === "AM") {
        if (!/^(?:1[01]|0[89]):[0-5][0-9]$/.test(endTime)) {
            console.log("Invalid end time for AM");
            return false;
        }
    }
    else {
        if (!/^(?:1[02]|0[0-9]):[0-5][0-9]$/.test(endTime)) {
            console.log("invalid end time for PM");
            return false;
        }
    }

    if ((startTime === endTime) && (startHourTime === endHourTime)) {
        console.log("start time === end time");
        return false;
    }
    else if ( (endHour < startHour) && (startHourTime === endHourTime) &&
        (startHour !== 12)) {
        //12 indicates either midnight or an afternoon time
        console.log("endhour < startHour");
        return false;
    }
    else {
        return true;
    }
}

function validateStudentId() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    var studentId = document.getElementById("studentId");
    var errorMessage = document.getElementById("studentIdError");

    if(numPattern.test(studentId.value) || isEmpty(studentId.value)) {
        console.log("incorrect student id");
        studentId.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        studentId.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

function validateFirstName() {
    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    var firstName = document.getElementById("firstName");
    var firstNameError = document.getElementById("firstNameError");

    if (namePattern.test(firstName.value) || isEmpty(firstName.value)) {
        firstName.style = invalidInputStyle;
        firstNameError.style = displayError;
    }
    else {
        firstName.style = correctInputStyle;
        firstNameError.style = hideError;
    }
}

function validateLastName() {
    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    var lastName = document.getElementById("lastName");
    var lastNameError = document.getElementById("lastNameError");

    if (namePattern.test(lastName.value) || isEmpty(lastName.value)) {
        lastName.style = invalidInputStyle;
        lastNameError.style = displayError;
    }
    else {
        lastName.style = correctInputStyle;
        lastNameError.style = hideError;
    }
}

function validateCourseName() {
    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    var courseName = document.getElementById("courseName");
    var courseNameError = document.getElementById("courseNameError");

    if (namePattern.test(courseName.value) || isEmpty(courseName.value)) {
        courseName.style = invalidInputStyle;
        courseNameError.style = displayError;
    }
    else {
        courseName.style = correctInputStyle;
        courseNameError.style = hideError;
    }
}

function validateCourseNumber() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    var courseNumber = document.getElementById("courseNumber");
    var errorMessage = document.getElementById("courseNumberError");

    if(numPattern.test(courseNumber.value) || isEmpty(courseNumber.value)) {
        courseNumber.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        courseNumber.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

function validateStartCourseTime() {
    //Find a match for a time in the format 00:00 to 12:59
    var timePattern;

    if (document.getElementById("courseStartAMPM").value === "AM")
        timePattern = /^(?:1[01]|0[89]):[0-5][0-9]$/;
    else
        timePattern = /^(?:1[02]|0[0-9]):[0-5][0-9]$/;

    var startCourseTime = document.getElementById("startCourseTime");
    var errorMessage = document.getElementById("startCourseTimeError");

    if (!timePattern.test(startCourseTime.value) || isEmpty(startCourseTime.value)) {
        startCourseTime.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        startCourseTime.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

function validateEndCourseTime() {
    //Find a match for a time in the format 00:00 to 12:59
    var timePattern;

    if (document.getElementById("courseEndAMPM").value === "AM")
        timePattern = /^(?:1[01]|0[89]):[0-5][0-9]$/;
    else
        timePattern = /^(?:1[02]|0[0-9]):[0-5][0-9]$/;

    var endCourseTime = document.getElementById("endCourseTime");
    var errorMessage = document.getElementById("endCourseTimeError");

    if (!timePattern.test(endCourseTime.value) || isEmpty(endCourseTime.value)) {
        endCourseTime.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        endCourseTime.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

function validateScheduleName() {
    var namePattern = /(\W+\s^|\d+)/;

    var scheduleName = document.getElementById("scheduleName").value;
    var errorMessage = document.getElementById("scheduleNameError");

    if (scheduleName == null)
        scheduleName = document.getElementById("scheduleNameValue").innerHTML;

    if (namePattern.test(scheduleName)) {
        scheduleName.style = invalidInputStyle;
        errorMessage.style = displayError;
        return false;
    }
    else {
        scheduleName.style = correctInputStyle;
        errorMessage.style = hideError;
        return true;
    }
}

function isEmpty(inputField) {
    if (inputField === "") {
        console.log("Empty field");
        return true;
    }
}