function validateForm() {
    window.alert("IN VALIDATE FORM NONE OF THIS IS WORKING!!!!!!!!!!!!!!!!1");
    console.log("in validate form");

    return false;
}

function validate() {
    //Find a match for any non-digit character
    var numPattern = /\D+/;

    //Find a match for any non-word character or digit
    var namePattern = /(\W+\s^|\d+)/;

    //Need to check if the times dont make sense (start time is after end time);
    //Can add 'AM' or 'PM' list with this to convert to military time
    //var timePattern = /([0]{1}[0-9]{1}|[1]{1}[0-2]{1}){1}:{1}([0]{1}[0-9]{1}|[1-5]{1}[0-9]{1}){1}/;

    var formName = "courseForm";
    var studentId = document.forms[formName]["sId"].value;
    var firstName = document.forms[formName]["fName"].value;
    var lastName = document.forms[formName]["lName"].value;
    var courseID = document.forms[formName]["cId"].value;
    var courseName = document.forms[formName]["cName"].value;
    var courseNumber = document.forms[formName]["cNum"].value;
    var courseStart = document.forms[formName]["cStart"].value;
    var courseEnd = document.forms[formName]["cEnd"].value;
    var courseDays = document.getElementsByName("courseDays");

    if (isEmpty(studentId) || isEmpty(firstName) || isEmpty(lastName)
        || isEmpty(courseID) || isEmpty(courseName) || isEmpty(courseNumber)
        || isEmpty(courseStart) || isEmpty(courseEnd)) {
        console.log("Student id is empty");
        return false;
    }
    else if (numPattern.test(studentId)) {
        console.log("Invalid student id");
        return false;
    }
    else if (namePattern.test(firstName)) {
        console.log("Invalid first name");
        return false;
    }
    else if (namePattern.test(lastName)) {
        console.log("Invalid last name");
        return false;
    }
    else if (numPattern.test(courseID)) {
        console.log("Invalid course id");
        return false;
    }
    else if (namePattern.test(courseName)) {
        console.log("Invalid course name");
        return false;
    }
    else if (numPattern.test(courseNumber)) {
        console.log("Invalid course number");
        return false;
    }
    else if (courseDays.length === 0) {
        console.log("Need to check at least one day");
        return false;
    }
    else {
        console.log("passed all other conditions");
        window.alert("THIS SHOULD BE RETURNING FALSE!!!!!!!!");
        return false;
    }
}

function isEmpty(inputField) {
    if (inputField === "") {
        console.log("Empty");
        return true;
    }
}