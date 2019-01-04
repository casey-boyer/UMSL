/*The styling for an invalid input, which gives a red background*/
invalidInputStyle = "background-color: #da817c;";

/*The styling for valid input, which removes the red background*/
correctInputStyle = "background-color: none;";

/*The styling to display or hide an element that will show an error*/
displayError = "visibility: visible;"; //Display the error
hideError = "visibility: hidden;"; //Hide the error

//Function to validate the input fields in the studentForm on the studentInfo.jsp
//page before submitting the form data to the server
function validateStudent() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    //The name of the form which will contain the student input values
    var formName = "studentForm";

    //Get the studentId, firstName, and lastName input values provided by the user
    var studentId = document.forms[formName]["studentId"].value;
    var firstName = document.forms[formName]["firstName"].value;
    var lastName = document.forms[formName]["lastName"].value;

    if (isEmpty(studentId) || isEmpty(firstName) || isEmpty(lastName)) {
        //If any of the input fields are empty, return false
        return false;
    }
    else if (numPattern.test(studentId)) {
        //If the studentId contains any characters other than a digit, return false
        return false;
    }
    else if (namePattern.test(firstName)) {
        //If the firstName contains any non-alphabetic characters, return false
        return false;
    }
    else if (namePattern.test(lastName)) {
        //If the lastName contains any non-alphabetic characters, return false
        return false;
    }
    else {
        //If all other conditions were passed, then return true to submit the form
        //to the server
        return true;
    }
}

//Function to validate the input fields in the courseForm on the schedule.jsp
//page before submitting the form data to the server
function validate() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    //Find a match for a time in the format 00:00 to 12:59
    var timePattern = /^(?:1[0-2]|0[0-9]):[0-5][0-9]$/;

    //The name of the form that contains the input elements to validate
    var formName = "courseForm";

    //Get the values of the courseName, courseNumber, courseStart, courseEnd, and courseDays
    //input fields
    var courseName = document.forms[formName]["cName"].value;
    var courseNumber = document.forms[formName]["cNum"].value;
    var courseStart = document.forms[formName]["cStart"].value;
    var courseEnd = document.forms[formName]["cEnd"].value;
    var courseDays = document.getElementsByName("courseDays");

    //Check if any checkboxes (days) for the course were checked
    var numUnchecked = 0;

    for(var i = 0; i < courseDays.length; i++) {
        if (!courseDays[i].checked) {
            //Increment the number of unchecked days
            numUnchecked++;
        }
    }

    //If the number of unchecked checkboxes is equal to the amount of available
    //checkboxes, then no days for the course were selected. Return false.
    if (numUnchecked === courseDays.length) {
        console.log("No days checked for the course.");
        return false;
    }

    if (isEmpty(courseName) || isEmpty(courseNumber)
        || isEmpty(courseStart) || isEmpty(courseEnd)) {
        //If any of the text input fields are empty return false
        return false;
    }
    else if (namePattern.test(courseName)) {
        //If the courseName field contains any non-alphabetic characters, return false
        return false;
    }
    else if (numPattern.test(courseNumber)) {
        //If the courseNumber field contains any non-numeric characters, return false
        return false;
    }
    else if (!timePattern.test(courseStart)) {
        //If the courseStart time does not equal a time in the range of 00:00-12:59 format,
        //return false
        console.log("Incorrect time for course start time");
        return false;
    }
    else if(!timePattern.test(courseEnd)) {
        //If the courseEnd time does not equal a time in the range of 00:00-12:59 format,
        //return false
        console.log("Incorrect time for course end time");
        return false;
    }
    else if (!validateTime(courseStart, courseEnd)) {
        //Check if the courseStart and courseEnd times are not the same,
        //do not overlap, and occur within the hours of 08:00AM-10:00PM. If not,
        //return false
        console.log("Invalid times for the course start time and course end time");
        return false;
    }
    else {
        //If all conditions were passed, return true to submit the courseForm data
        //to the server
        console.log("passed all conditions");
        return true;
    }
}

function validateTime(startTime, endTime) {
    /*Get the hour of the startTime, the time of day of the
    * startTime (AM or PM), the hour of the endTime, and the time of day
    * of the endTime (AM or PM).*/
    var startHour = Number(startTime.substring(0, 2));
    var startHourTime = document.getElementById("courseStartAMPM").value;
    var endHour = Number(endTime.substring(0, 2));
    var endHourTime = document.getElementById("courseEndAMPM").value;

    if (startHourTime === "AM") {
        //If the startTime occurs in the 'AM' time of day, test to see if the
        //startTime is within 08:00AM-11:00AM.
        if (!/^(?:1[01]|0[89]):[0-5][0-9]$/.test(startTime)) {
            //If not, return false.
            console.log("Invalid start time for AM; must be between 08:00AM-11:00AM.");
            return false;
        }
    }
    else {
        //If the startTime occurs in the 'PM' time of day, test to see if the
        //startTime is within 12:00PM-10:00PM.
        if (!/^(?:1[02]|0[0-9]):[0-5][0-9]$/.test(startTime)) {
            //If not, return false.
            console.log("Invalid start time for PM; must be between 12:00PM-10:00PM.");
            return false;
        }
    }

    if (endHourTime === "AM") {
        //If the endTime occurs in the 'AM' time of day, test to see if the
        //endTime is within 08:00AM-11:00AM.
        if (!/^(?:1[01]|0[89]):[0-5][0-9]$/.test(endTime)) {
            //If not, return false.
            console.log("Invalid end time for AM; must be between 08:00AM-11:00AM.");
            return false;
        }
    }
    else {
        //If the endTime occurs in the 'PM' time of day, test to see if the
        //endTime is within 12:00PM-10:00PM.
        if (!/^(?:1[02]|0[0-9]):[0-5][0-9]$/.test(endTime)) {
            //If not, return false.
            console.log("Invalid end time for PM; must be between 12:00PM-10:00PM.");
            return false;
        }
    }

    if ((startTime === endTime) && (startHourTime === endHourTime)) {
        //If the startTime and endTime are the same, return false.
        console.log("Invalid course times: start time is the same as end time.");
        return false;
    }
    else if ( (endHour < startHour) && (startHourTime === endHourTime) &&
        (startHour !== 12)) {
        //If the course end time is before the course start time, and they both
        //occur in the AM or PM (and it is not noon), return false as this is an impossible
        //course time value.
        console.log("Invalid course times: end time cannot be before start time.");
        return false;
    }
    else {
        //Otherwise, if all conditions passed, return true.
        return true;
    }
}

//Validate the studentId input text while the user is typing.
function validateStudentId() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    //Get the studentId input field, and the errorMessage associated with it,
    var studentId = document.getElementById("studentId");
    var errorMessage = document.getElementById("studentIdError");

    if(numPattern.test(studentId.value) || isEmpty(studentId.value)) {
        //If the studentId matches any non-digit character, or is empty,
        //apply invalid style to the element and display the error.
        console.log("Incorrect student id.");
        studentId.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        studentId.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

//Validate the firstName input field while the user is typing.
function validateFirstName() {
    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    //Get the firstName input field, and the errorMessage associated with it.
    var firstName = document.getElementById("firstName");
    var firstNameError = document.getElementById("firstNameError");

    if (namePattern.test(firstName.value) || isEmpty(firstName.value)) {
        //If the firstName matches any non-alphabetic character, or is empty,
        //apply invalid style to the element and display the error.
        firstName.style = invalidInputStyle;
        firstNameError.style = displayError;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        firstName.style = correctInputStyle;
        firstNameError.style = hideError;
    }
}

//Validate the last name as the user is typing.
function validateLastName() {
    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    //Get the lastName input field, and the errorMessage associated with it.
    var lastName = document.getElementById("lastName");
    var lastNameError = document.getElementById("lastNameError");

    if (namePattern.test(lastName.value) || isEmpty(lastName.value)) {
        //If the lastName matches any non-alphabetic character, or is empty,
        //apply invalid style to the element and display the error.
        lastName.style = invalidInputStyle;
        lastNameError.style = displayError;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        lastName.style = correctInputStyle;
        lastNameError.style = hideError;
    }
}

//Validate the course name as the user is typing.
function validateCourseName() {
    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    //Get the courseName input field, and the errorMessage associated with it.
    var courseName = document.getElementById("courseName");
    var courseNameError = document.getElementById("courseNameError");

    if (namePattern.test(courseName.value) || isEmpty(courseName.value)) {
        //If the courseName matches any non-alphabetic character, or is empty,
        //apply invalid style to the element and display the error.
        courseName.style = invalidInputStyle;
        courseNameError.style = displayError;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        courseName.style = correctInputStyle;
        courseNameError.style = hideError;
    }
}

//Validate the course number as the user is typing.
function validateCourseNumber() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    //Get the courseNumber input field, and the errorMessage associated with it.
    var courseNumber = document.getElementById("courseNumber");
    var errorMessage = document.getElementById("courseNumberError");

    if(numPattern.test(courseNumber.value) || isEmpty(courseNumber.value)) {
        //If the studentId matches any non-digit character, or is empty,
        //apply invalid style to the element and display the error.
        courseNumber.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        courseNumber.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

//Validate the start course time as the user is typing.
function validateStartCourseTime() {
    //Find a match for a time in the format 00:00 to 12:59
    var timePattern;

    //If the start course time occurs in the AM, it must be between 08:00AM-11:00AM.
    //Otherwise, it must be between 12:00PM-10:00PM.
    if (document.getElementById("courseStartAMPM").value === "AM")
        timePattern = /^(?:1[01]|0[89]):[0-5][0-9]$/;
    else
        timePattern = /^(?:1[02]|0[0-9]):[0-5][0-9]$/;

    //Get the startCourseTime input field, and the errorMessage associated with it.
    var startCourseTime = document.getElementById("startCourseTime");
    var errorMessage = document.getElementById("startCourseTimeError");

    if (!timePattern.test(startCourseTime.value) || isEmpty(startCourseTime.value)) {
        //If the startCourseTime contains any non-digit character other than a ':', or
        //is not in the correct range depending on the time of the day (AM or PM), or is empty,
        //apply invalid style to the element and display the error.
        startCourseTime.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        startCourseTime.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

//Validate the end course time as the user is typing.
function validateEndCourseTime() {
    //Find a match for a time in the format 00:00 to 12:59
    var timePattern;

    //If the end course time occurs in the AM, it must be between 08:00AM-11:00AM.
    //Otherwise, it must be between 12:00PM-10:00PM.
    if (document.getElementById("courseEndAMPM").value === "AM")
        timePattern = /^(?:1[01]|0[89]):[0-5][0-9]$/;
    else
        timePattern = /^(?:1[02]|0[0-9]):[0-5][0-9]$/;

    //Get the endCourseTime input field, and the errorMessage associated with it.
    var endCourseTime = document.getElementById("endCourseTime");
    var errorMessage = document.getElementById("endCourseTimeError");

    if (!timePattern.test(endCourseTime.value) || isEmpty(endCourseTime.value)) {
        //If the endCourseTime contains any non-digit character other than a ':', or
        //is not in the correct range depending on the time of the day (AM or PM), or is empty,
        //apply invalid style to the element and display the error.
        endCourseTime.style = invalidInputStyle;
        errorMessage.style = displayError;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        endCourseTime.style = correctInputStyle;
        errorMessage.style = hideError;
    }
}

//Validate the schedule name as the user is typing.
function validateScheduleName() {
    var namePattern = /(\W+\s^|\d+)/;

    //Get the scheduleName input field, and the errorMessage associated with it.
    var scheduleName = document.getElementById("scheduleName").value;
    var errorMessage = document.getElementById("scheduleNameError");

    //If the document.getElementById("scheduleName").value returns null, then
    //the schedule name was already submitted; get the text value of it.
    if (scheduleName == null)
        scheduleName = document.getElementById("scheduleNameValue").innerHTML;

    if (namePattern.test(scheduleName)) {
        //If the scheduleName matches any non-alphabetic character, or is empty,
        //apply invalid style to the element and display the error.
        scheduleName.style = invalidInputStyle;
        errorMessage.style = displayError;

        //Return false
        return false;
    }
    else {
        //Otherwise, remove the invalid styling and hide the error message.
        scheduleName.style = correctInputStyle;
        errorMessage.style = hideError;

        //Return true
        return true;
    }
}

//Test if an input field is empty. If so, return true.
function isEmpty(inputField) {
    if (inputField === "") {
        console.log("Empty field");
        return true;
    }
}