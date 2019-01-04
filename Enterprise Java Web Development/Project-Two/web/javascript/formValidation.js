var usernamePattern = /(\W+|\s+)/; //Find any non-word character or space
var matchDigit = /\d+/; //Match a digit one or more times
var matchSpecialCharacter = /(\W+|_+)/; //Match a non-word character or underscore one or more times
var matchUppercaseCharacter = /[A-Z]+/; //Match an uppercase character one or more times
var matchLowercaseCharacter = /[a-z]+/; //Match a lowercase character one or more times
var namePattern = /(\W+|\d+|_+)/; //Find any non-word character or digit

//Match 1 or more digits, followed by: a space and letters, 2 or more times, and end with letters
var streetAddressPattern =/^\d+(\s[a-zA-Z]+){2,}$/;

//Begin with letters, followed by any number of letters; optional:
//  space separator, hyphen - separator, or period . space separator
var cityPattern = /^[a-zA-Z]+(?:([\s-]|(\.\s))[a-zA-Z]+)*$/;

//Match 5 digits, or optionally match 5 digits, followed by a hyphen, followed by 4 more digits. Must
//end with digits.
var zipcodePattern = /^\d{5}(-\d{4})?$/;

//Match a year from 1950 - 2018
var yearRangePattern = /^(19[5-9]\d|200\d|201[0-8])$/;

//Match 3 digits, followed by a space, period, or hyphen, then match 3 more digits, followed
//by a space, period, or hyphen, and then match 4 more digits. Must end with 4 digits.
var phoneNumberPattern = /^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/;

//Match alphabetic characters, with spaces or commas in between. Must end with an alphabetic character.
var areasOfInterestPattern = /^[a-z]+((\s|,)+[a-z]+)*$/i;

//Match alphabetic characters, with an optional space or period between alphabetic characters. Must end
//with an alphabetic character.
var alphabeticCharactersPattern = /^[a-zA-Z]+(?:[. ][a-zA-z]+)*$/;

//Change the action attribute of the form with the specified name to the value provided by the
//formSubmissionOption variable (which will be GET or POST).
function formSubmission(formName, formSubmissionOption) {
    //Get the submission method value by retrieving the value of the select box with the specified id
    var submissionMethod = document.getElementById(formSubmissionOption).value;

    //Get the form element by retrieving the element with the id specified by formName
    var form = document.getElementById(formName);

    //Set the action attribute according to the value of the formSubmissionOption select box
    if (submissionMethod === "post")
        form.setAttribute("method", "POST");
    else if (submissionMethod === "get")
        form.setAttribute("method", "GET");
}

//Validate the sign in form
function validateSignInForm() {
    var signInForm = "signIn";
    var username = document.forms[signInForm]["username"].value;
    var password = document.forms[signInForm]["pwd"].value;

    formSubmission("signInForm", "formSubmissionOption");

    //The username or password fields may not be empty. Validate the username and password
    //to ensure they match the correct pattern
    if (isEmpty(username) || isEmpty(password)) {
        return false;
    }
    if (usernamePattern.test(username)) {
        return false;
    }
    else if (!validatePassword()) {
        return false;
    }
    else
        return true;
}

//Validate the registration form
function validateRegisterForm() {
    var form = "register";
    var username = document.forms[form]["username"].value;
    var password = document.forms[form]["pwd"].value;
    var firstName = document.forms[form]["firstName"].value;
    var lastName = document.forms[form]["lastName"].value;
    var city = document.forms[form]["city"].value;
    var zipcode = document.forms[form]["zipcode"].value;
    var addressOne = document.forms[form]["addressStreetOne"].value;
    var addressTwo = document.forms[form]["addressStreetTwo"].value;

    formSubmission("registerForm", "formSubmissionOption");

    if (isEmpty(username) || isEmpty(password) || isEmpty(firstName) || isEmpty(lastName) ||
        isEmpty(addressOne)|| isEmpty(city) || isEmpty(zipcode)) {
        return false;
    }
    else if (usernamePattern.test(username))
        return false;
    else if (!validatePassword())
        return false;
    else if (namePattern.test(firstName))
        return false;
    else if (namePattern.test(lastName))
        return false;
    else if (!cityPattern.test(city))
        return false;
    else if (!zipcodePattern.test(zipcode))
        return false;
    else if (!streetAddressPattern.test(addressOne))
        return false;
    else if (!validateAddressTwo())
        return false;
    else
        return true;

}

//Validate the workHistory form
function validateWorkHistoryForm() {
    formSubmission("workHistoryForm", "formSubmissionOption");
    var form = "workHistory";

    var jobTitle = document.forms[form]["jobTitle"].value;
    var companyName = document.forms[form]["companyName"].value;
    var yearsOfService = document.forms[form]["yearsOfService"].value;

    if (isEmpty(jobTitle) || isEmpty(companyName) || isEmpty(yearsOfService))
        return false;
    else if (!validateJobTitle())
        return false;
    else if (!validateCompanyName())
        return false;
    else if (!validateYearsOfService())
        return false;
    else
        return true;
}

//Validate the educationHistory form
function validateEducationHistoryForm() {
    formSubmission("educationHistoryForm", "formSubmissionOption");
    var form = "educationHistory";

    var degreeType = document.forms[form]["degreeType"].value;
    var degreeDiscipline = document.forms[form]["degreeDiscipline"].value;
    var yearAchieved = document.forms[form]["yearAchieved"].value;
    var universityName = document.forms[form]["universityName"].value;

    if (isEmpty(degreeType) || isEmpty(degreeDiscipline) || isEmpty(yearAchieved) || isEmpty(universityName))
        return false;
    else if (!validateDegreeType())
        return false;
    else if (!validatedegreeDiscipline())
        return false;
    else if (!validateDegreeYear())
        return false;
    else if (!validateUniversityName())
        return false;
    else
        return true;
}

//Validate the editDetailsForm
function validateEditDetailsForm() {
    formSubmission("editDetailsForm", "formSubmissionOption");

    //Name of the edit details form
    var form = "editDetails";
    var password = document.forms[form]["pwd"].value;
    var firstName = document.forms[form]["firstName"].value;
    var lastName = document.forms[form]["lastName"].value;
    var addressStreetOne = document.forms[form]["addressStreetOne"].value;
    var addressStreetTwo = document.forms[form]["addressStreetTwo"].value;
    var city = document.forms[form]["city"].value;
    var zipcode = document.forms[form]["zipcode"].value;
    var birthday = document.forms[form]["birthday"].value;
    var phoneNumber = document.forms[form]["phoneNumber"].value;
    var cellPhoneNumber = document.forms[form]["cellPhoneNumber"].value;
    var areasOfInterest = document.forms[form]["areasOfInterest"].value;

    //The form fields for the password, first name, last name, adddress street one, city, and zipcode
    //are not optional.
    if (isEmpty(password) || isEmpty(firstName) || isEmpty(lastName) || isEmpty(addressStreetOne)
        || isEmpty(city) || isEmpty(zipcode)) {
        console.log("one of the required fields is empty");
        return false;
    }
    else if (!validatePassword())
        return false;
    else if (namePattern.test(firstName))
        return false;
    else if (namePattern.test(lastName))
        return false;
    else if (!streetAddressPattern.test(addressStreetOne))
        return false;
    else if (!cityPattern.test(city))
        return false;
    else if (!zipcodePattern.test(zipcode))
        return false;
    else if (!validateAddressTwo())
        return false;
    else if (!validatePhoneNumber())
        return false;
    else if (!validateCellPhoneNumber())
        return false;
    else if (!validateAreasOfInterest())
        return false;
    else if (!validateBirthday())
        return false;
    else
        return true;
}

//Validate the username field
function validateUsername() {
    var usernameField = document.getElementById("username");
    var usernameError = document.getElementById("usernameRequirements");

    //If the username is empty, or has a non-word character or space, or is less than
    //6 characters, style the input to reflect an error and display an error message.
    if (isEmpty(usernameField.value) || usernamePattern.test(usernameField.value) ||
        (usernameField.value.length < 6)) {
        styleError(usernameField, usernameError, false);
        return false;
    }
    else {
        styleError(usernameField, usernameError, true);
        return true;
    }
}

//Validate the password field
function validatePassword() {
    var passwordField = document.getElementById("pwd");
    var passwordError = document.getElementById("passwordRequirements");

    //If the password field is empty, or less than 8 characters, or does not contain a digit, special
    //character, lowercase letter, and uppercase letter, style the input error and return false.
    if (isEmpty(passwordField.value) || (passwordField.value.length < 8) ||
        (!matchDigit.test(passwordField.value)) || (!matchSpecialCharacter.test(passwordField.value)) ||
        (!matchUppercaseCharacter.test(passwordField.value)) ||
        (!matchLowercaseCharacter.test(passwordField.value))) {
        styleError(passwordField, passwordError, false);
        return false;
    }
    else {
        styleError(passwordField, passwordError, true);
        return true;
    }
}

//Validate the firstName and lastName input fields/
function validateName() {
    var nameField;
    var nameFieldError;

    if (document.activeElement.id === "firstName") {
        nameField = document.getElementById("firstName");
        nameFieldError = document.getElementById("firstNameRequirements");
    }
    else if (document.activeElement.id === "lastName") {
        nameField = document.getElementById("lastName");
        nameFieldError = document.getElementById("lastNameRequirements")
    }

    //If the input field is empty or contains any other characters besides alphabetic characters,
    //return false.
    if (isEmpty(nameField.value) || namePattern.test(nameField.value)) {
        styleError(nameField, nameFieldError, false);
        return false;
    }
    else {
        styleError(nameField, nameFieldError, true);
        return true;
    }
}

//Validate the city field
function validateCity() {
    var city = document.getElementById("city");
    var cityError = document.getElementById("cityRequirements");

    //If the city field is empty, or contains characters other than alphabetic characters, period or space,
    //return false.
    if (isEmpty(city.value) || (!cityPattern.test(city.value))) {
        styleError(city, cityError, false);
        return false;
    }
    else {
        styleError(city, cityError, true);
        return true;
    }
}

//Validate the zipcode field
function validateZipcode() {
    var zipcode = document.getElementById("zipcode");
    var zipcodeError = document.getElementById("zipcodeRequirements");

    //If the zipcode is empty, or does not match the patterns DDDDD or DDDDD-DDDD, return false.
    if (isEmpty(zipcode.value) || (!zipcodePattern.test(zipcode.value))) {
        styleError(zipcode, zipcodeError, false);
        return false;
    }
    else {
        styleError(zipcode, zipcodeError, true);
        return true;
    }
}

//Validate the Street Address one field
function validateAddressOne() {
    var addressOne = document.getElementById("addressStreetOne");
    var addressOneError = document.getElementById("addressStreetOneRequirements");

    //If the address one field is empty, or does not match the "123 Name St" format, return false
    if (isEmpty(addressOne.value) || (!streetAddressPattern.test(addressOne.value))) {
        styleError(addressOne, addressOneError, false);
        return false;
    }
    else {
        styleError(addressOne, addressOneError, true);
        return true;
    }
}

//Validate the second address field
function validateAddressTwo() {
    var addressTwo = document.getElementById("addressStreetTwo");
    var addressTwoError = document.getElementById("addressStreetTwoRequirements");

    //If the second address field is not empty, ensure that it matches the "123 Name St" format
    if (!isEmpty(addressTwo.value)) {
        if (!streetAddressPattern.test(addressTwo.value)) {
            styleError(addressTwo, addressTwoError, false);
            return false;
        }
        else {
            styleError(addressTwo, addressTwoError, true);
            return true;
        }
    }
    else {
        styleError(addressTwo, addressTwoError, true);
        return true;
    }
}

//Validate the phone number field
function validatePhoneNumber() {
    var phoneNumber = document.getElementById("phoneNumber");
    var phoneNumberError = document.getElementById("phoneNumberRequirements");

    //If the phone number field is not empty, ensure that it matches the
    //DDD-DDD-DDDD OR DDD DDD DDDD OR DDD.DDD.DDDD format.
    if (!isEmpty(phoneNumber.value)) {
        if (!phoneNumberPattern.test(phoneNumber.value)) {
            styleError(phoneNumber, phoneNumberError, false);
            return false;
        }
        else {
            styleError(phoneNumber, phoneNumberError, true);
            return true;
        }
    }
    else {
        styleError(phoneNumber, phoneNumberError, true);
        return true;
    }
}

//Validate the cellphone field
function validateCellPhoneNumber() {
    var cellPhoneNumber = document.getElementById("cellPhoneNumber");
    var cellPhoneNumberError = document.getElementById("cellPhoneNumberRequirements");

    //If the cellphone number field is not empty, ensure that it matches the
    //DDD-DDD-DDDD OR DDD DDD DDDD OR DDD.DDD.DDDD format.
    if (!isEmpty(cellPhoneNumber.value)) {
        if (!phoneNumberPattern.test(cellPhoneNumber.value)) {
            styleError(cellPhoneNumber, cellPhoneNumberError, false);
            return false;
        }
        else {
            styleError(cellPhoneNumber, cellPhoneNumberError, true);
            return true;
        }
    }
    else {
        styleError(cellPhoneNumber, cellPhoneNumberError, true);
        return true;
    }
}

//Validate the birthday field
function validateBirthday() {
    var birthday = document.getElementById("birthday");
    var birthdayRequirements = document.getElementById("birthdayRequirements");

    //If the birthday field is not empty, validate that it is a proper date() object
    if (!isEmpty(birthday.value)) {
        var birthdayArr = birthday.value.split("/");

        //If the split birthday array contains less than 3 elements or greater than 3
        //elements, then the user did not enter the date correctly.
        if (birthdayArr.length < 3 || birthdayArr.length > 3) {
            styleError(birthday, birthdayRequirements, false);
            return false;
        }
        else {
            var date = new Date(birthday.value);

            //If the month, day, or year fields are not a number, OR the year is greater than 2018,
            //return false
            if ((isNaN(date.getMonth())) || isNaN(date.getDay()) || isNaN(date.getFullYear())
                || (date.getFullYear() > 2018)) {
                styleError(birthday, birthdayRequirements, false);
                return false;
            }
            else {
                styleError(birthday, birthdayRequirements, true);
                return true;
            }
        }
    }
    else {
        styleError(birthday, birthdayRequirements, true);
        return true;
    }
}

//Validate the areas of interest field
function validateAreasOfInterest() {
    var areasOfInterest = document.getElementById("areasOfInterest");
    var areasOfInterestError = document.getElementById("areasOfInterestRequirements");

    //If the areas of interest field is not empty, validate that it contains only alphabetic characters
    //separated by spaces and/or commas.
    if (!isEmpty(areasOfInterest.value)) {
        if (!areasOfInterestPattern.test(areasOfInterest.value)) {
            styleError(areasOfInterest, areasOfInterestError, false);
            return false;
        }
        else {
            styleError(areasOfInterest, areasOfInterestError, true);
            return true;
        }
    }
    else {
        styleError(areasOfInterest, areasOfInterestError, true);
        return true;
    }
}

//Validate the degreeType field
function validateDegreeType() {
    var degreeType = document.getElementById("degreeType");
    var degreeTypeError = document.getElementById("degreeTypeRequirements");

    //If the degreeType field is empty, return false. Otherwise, ensure that it contains only
    //alphabetic characters, with an optional space or period separator between characters
    if (isEmpty(degreeType.value)) {
        styleError(degreeType, degreeTypeError, false);
        return false;
    }
    else if (!alphabeticCharactersPattern.test(degreeType.value)) {
        styleError(degreeType, degreeTypeError, false);
        return false;
    }
    else {
        styleError(degreeType, degreeTypeError, true);
        return true;
    }
}

//Validate the degree discpline field
function validatedegreeDiscipline() {
    var degreeDiscipline = document.getElementById("degreeDiscipline");
    var degreeDisciplineError = document.getElementById("degreeDisciplineRequirements");

    //If the degreeDiscipline field is empty, return false. Otherwise, ensure that it contains only
    //alphabetic characters, with an optional space or period separator between characters
    if (isEmpty(degreeDiscipline.value)) {
        styleError(degreeDiscipline, degreeDisciplineError, false);
        return false;
    }
    else if (!alphabeticCharactersPattern.test(degreeDiscipline.value)) {
        styleError(degreeDiscipline, degreeDisciplineError, false);
        return false;
    }
    else {
        styleError(degreeDiscipline, degreeDisciplineError, true);
        return true;
    }
}

//Validate the degree year field
function validateDegreeYear() {
    var year = document.getElementById("yearAchieved");
    var yearError = document.getElementById("yearAchievedRequirements");

    //If the yearAchieved field is empty, return false. Otherwise, ensure that it contains any value from
    //1950-2018
    if (isEmpty(year.value)) {
        styleError(year, yearError, false);
        return false;
    }
    else if (!yearRangePattern.test(year.value)) {
        styleError(year, yearError, false);
        return false;
    }
    else {
        styleError(year, yearError, true);
        return true;
    }
}

//Validate the university name field
function validateUniversityName() {
    var universityName = document.getElementById("universityName");
    var universityNameError = document.getElementById("universityNameRequirements");

    //If the universityName field is empty, return false. Otherwise, ensure that it contains only
    //alphabetic characters, with an optional space or period separator between characters
    if (isEmpty(universityName.value)) {
        styleError(universityName, universityNameError, false);
        return false;
    }
    else if (!alphabeticCharactersPattern.test(universityName.value)) {
        styleError(universityName, universityNameError, false);
        return false;
    }
    else {
        styleError(universityName, universityNameError, true);
        return true;
    }
}

//Validate the job title field
function validateJobTitle() {
    var jobTitle = document.getElementById("jobTitle");
    var jobTitleError = document.getElementById("jobTitleRequirements");

    //If the jobTitle field is empty, return false. Otherwise, ensure that it contains only
    //alphabetic characters, with an optional space or period separator between characters
    if (isEmpty(jobTitle.value)) {
        styleError(jobTitle, jobTitleError, false);
        return false;
    }
    else if (!alphabeticCharactersPattern.test(jobTitle.value)) {
        styleError(jobTitle, jobTitleError, false);
        return false;
    }
    else {
        styleError(jobTitle, jobTitleError, true);
        return true;
    }
}

//Validate the company name field
function validateCompanyName() {
    var companyName = document.getElementById("companyName");
    var companyNameError = document.getElementById("companyNameRequirements");

    //If the companyName field is empty, return false. Otherwise, ensure that it contains only
    //alphabetic characters, with an optional space or period separator between characters
    if (isEmpty(companyName.value)) {
        styleError(companyName, companyNameError, false);
        return false;
    }
    else if (!alphabeticCharactersPattern.test(companyName.value)) {
        styleError(companyName, companyNameError, false);
        return false;
    }
    else {
        styleError(companyName, companyNameError, true);
        return true;
    }
}

//Validate the years of service field
function validateYearsOfService() {
    var yearsOfService = document.getElementById("yearsOfService");
    var yearsOfServiceError = document.getElementById("yearsOfServiceRequirements");

    //If the years of service field is empty, return false.
    if (isEmpty(yearsOfService.value)) {
        styleError(yearsOfService, yearsOfServiceError, false);
        return false;
    }
    else {
        //Validate that the input field is a DDDD-DDDD value.
        var yearsStr = yearsOfService.value;
        var indexOfHyphen = yearsStr.search("-");

        //If there is no hyphen in the input field, return false.
        if (indexOfHyphen === -1) {
            styleError(yearsOfService, yearsOfServiceError, false);
            return false;
        }
        else {
            //Split the input field value based on the hyphen.
            var yearsArr = yearsStr.split("-");

            //If the split() method returns an array of one element, or greater than 2 elements, return false.
            //There should only be two years.
            if ((yearsArr.length < 2) || (yearsArr.length > 2)) {
                styleError(yearsOfService, yearsOfServiceError, false);
                return false;
            }
            else {
                //Test if each year matches a year in the range 1950-2018. If not, return false.
                if (!yearRangePattern.test(yearsArr[0]) || !yearRangePattern.test(yearsArr[1])) {
                    styleError(yearsOfService, yearsOfServiceError, false);
                    return false;
                }
            }

            styleError(yearsOfService, yearsOfServiceError, true);
            return true;
        }
    }
}

//Style the input field according to whether it contains incorrect input, and display its corresponding
//error message.
function styleError(inputField, errorMessage, isCorrect) {
    if (!isCorrect) {
        //If the input field contains incorrect input, add the inputError class to the input element
        //and show its corresponding error message
        inputField.classList.add("inputError");
        errorMessage.classList.add("showElement");
    }
    else if (isCorrect) {
        //If the input field contains correct input, remove the inputError class from the input element,
        //and replace the showElement class on the input error message with the hideElement class
        inputField.classList.remove("inputError");
        errorMessage.classList.remove("showElement");
        errorMessage.classList.add("hideElement");
    }
}

//Check if the contents of an input field are empty
function isEmpty(inputField) {
    if (inputField === "")
        return true;
    else
        return false;
}