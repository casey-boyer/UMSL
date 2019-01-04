<html>
    <head>
        <%---This is the only way intellij will recognize ANY external static files.---%>
        <style>
            <jsp:directive.include file="/styles/profile.css" />
            <jsp:directive.include file="/styles/formStyles.css"/>
        </style>
            <%---For jQuery datepicker---%>
            <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
            <link rel="stylesheet" href="/resources/demos/style.css">
            <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
            <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
            <script>
                $( function() {
                    $( "#birthday" ).datepicker();
                } );
            </script>
    </head>

    <body>
        <div class="card">
            <p class="title">Profile Details: </p>

            <span class="description">

                <form:form method="POST" modelAttribute="user" action="?editUserDetails"
                           enctype="multipart/form-data" onsubmit="return validateEditDetailsForm()"
                           id="editDetailsForm" name="editDetails">

                    <form:label path="profilePicture">Profile picture: </form:label>
                    <form:input path="profilePicture" type="file" accept="image/*"/>
                    <br/>

                    <form:label path="password">Password: </form:label>
                    <p id="passwordRequirements" class="inputRequirements inputErrorMsg hideElement">
                        All passwords are at least 8 characters, and must contain an uppercase letter, a lowercase
                        letter, a number, and a special character.
                    </p>
                    <form:input path="password" type="password" value="${user.password}"
                        onkeyup="validatePassword()" id="pwd"/>
                    <br/>

                    <form:label path="firstName">First name: </form:label>
                    <p id="firstNameRequirements" class="inputRequirements inputErrorMsg hideElement">
                        First name should contain only letters.
                    </p>
                    <form:input path="firstName" type="text" value="${user.firstName}"
                        onkeyup="validateName()" id="firstName"/>
                    <br/>

                    <form:label path="lastName">Last name: </form:label>
                    <p id="lastNameRequirements" class="inputRequirements inputErrorMsg hideElement">
                        Last name should contain only letters.
                    </p>
                    <form:input path="lastName" type="text" value="${user.lastName}"
                        onkeyup="validateName()" id="lastName"/>
                    <br/>

                    <form:label path="addressStreetOne">Address Street One: </form:label>
                    <p id="addressStreetOneRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The first street address must follow the expected address format, such as: 0000 Street Name
                    </p>
                    <form:input path="addressStreetOne" type="text" value="${user.addressStreetOne}"
                        onkeyup="validateAddressOne()" id="addressStreetOne"/>
                    <br/>

                    <form:label path="addressStreetTwo">Address Street Two: </form:label>
                    <p id="addressStreetTwoRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The second street address is optional, but it must follow the expected address format,
                        such as: 0000 Street Name
                    </p>
                    <form:input path="addressStreetTwo" type="text" value="${user.addressStreetTwo}"
                        onkeyup="validateAddressTwo()" id="addressStreetTwo"/>
                    <br/>

                    <form:label path="city">City: </form:label>
                    <p id="cityRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The city should begin with letters, and may contain only letters, a hyphen, or period.
                    </p>
                    <form:input path="city" type="text" value="${user.city}"
                                onkeyup="validateCity()" id="city"/>
                    <br/>

                    <form:label path="state">State:</form:label><br />
                    <form:select path="state">
                        <form:options items="${user.stateMap.stateMap}"/>
                    </form:select>
                    <br/>

                    <form:label path="timezone">Timezone: </form:label><br/>
                    <form:select path="timezone">
                        <form:options items="${user.timeZoneMap.timezoneMap}"/>
                    </form:select>
                    <br/>

                    <form:label path="zipcode">Zipcode: </form:label>
                    <p id="zipcodeRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The zipcode should be a sequence of 5 digits, or a sequence of 5 digits, followed
                        by a hyphen, and then followed by a sequence of 4 digits.
                    </p>
                    <form:input path="zipcode" type="text" value="${user.zipcode}"
                                onkeyup="validateZipcode()" id="zipcode"/>
                    <br/>

                    <form:label path="birthday">Birthday: </form:label>
                    <p id="birthdayRequirements" class="inputErrorMsg hideElement">
                        The birthday should be in a MM/DD/YYYY format.
                    </p>
                    <form:input path="birthday" type="text" value="${user.birthday}"
                                id="birthday" onkeyup="validateBirthday()"/>
                    <br/>

                    <form:label path="phoneNumber">Phone Number: </form:label>
                    <p id="phoneNumberRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The phone number may be in the XXX-XXX-XXXX, XXX.XXX.XXXX, or XXX XXX XXXX format.
                    </p>
                    <form:input path="phoneNumber" type="text" value="${user.phoneNumber}"
                                onkeyup="validatePhoneNumber()" id="phoneNumber"/>
                    <br/>

                    <form:label path="cellPhoneNumber">Cell phone Number: </form:label>
                    <p id="cellPhoneNumberRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The cell phone number may be in the XXX-XXX-XXXX, XXX.XXX.XXXX, or XXX XXX XXXX format.
                    </p>
                    <form:input path="cellPhoneNumber" type="text" value="${user.cellPhoneNumber}"
                                onkeyup="validateCellPhoneNumber()" id="cellPhoneNumber"/>
                    <br/>

                    <form:label path="areasOfInterest">Areas of Interest: </form:label>
                    <p id="areasOfInterestRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The areas of interest field may not contain special characters, digits, or underscores.
                    </p>
                    <form:input path="areasOfInterest" type="text" value="${user.areasOfInterest}"
                            onkeyup="validateAreasOfInterest()" id="areasOfInterest"/>
                    <br/>

                    <input type="submit" value="Update"/>
                </form:form>
            </span>

            <select id="formSubmissionOption">
                <option value="post" id="postOption">POST</option>
                <option value="get" id="getOption">GET</option>
            </select>
        </div>
        <script>
            <jsp:directive.include file="/javascript/formValidation.js"/>
        </script>
    </body>
</html>
