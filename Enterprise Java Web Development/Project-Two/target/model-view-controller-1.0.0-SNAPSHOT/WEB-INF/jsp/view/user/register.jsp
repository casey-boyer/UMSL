
<!DOCTYPE html>
<html>
    <head>
        <title>Register</title>

        <%---This is the only way intellij will recognize ANY external static files.---%>
        <style>
            <jsp:directive.include file="/styles/profile.css" />
            <jsp:directive.include file="/styles/formStyles.css"/>
        </style>
    </head>
    <body>
        <div class="card">
            <span class="description">
                <c:if test="${not empty errorMessage}">
                    ${errorMessage}
                </c:if>

                <form:form method="post" modelAttribute="registerForm" name="register" id="registerForm"
                    onsubmit="return validateRegisterForm()">

                    <form:label path="username">Username:</form:label><br />
                    <p id="usernameRequirements" class="inputRequirements inputErrorMsg hideElement">
                        Username may only contain letters, numbers, and underscore characters. The username
                        must be at least 6 characters.
                    </p>

                    <form:input path="username" type="text" id="username" onkeyup="validateUsername()"/><br />
                    <br />

                    <form:label path="password">Password:</form:label><br />
                    <p id="passwordRequirements" class="inputRequirements inputErrorMsg hideElement">
                        All passwords are at least 8 characters, and must contain an uppercase letter, a lowercase
                        letter, a number, and a special character.
                    </p>
                    <form:input path="password" type="password" id="pwd" onkeyup="validatePassword()"/><br />
                    <br />

                    <form:label path="firstName">First Name:</form:label><br />
                    <p id="firstNameRequirements" class="inputRequirements inputErrorMsg hideElement">
                        First name should contain only letters.
                    </p>
                    <form:input path="firstName" type="text" id="firstName" onkeyup="validateName()"/><br />
                    <br/>

                    <form:label path="lastName">Last Name:</form:label><br />
                    <p id="lastNameRequirements" class="inputRequirements inputErrorMsg hideElement">
                        Last name should contain only letters.
                    </p>
                    <form:input path="lastName" type="text" id="lastName" onkeyup="validateName()"/><br />

                    <br/>
                    <form:label path="addressStreetOne">Address Street One:</form:label><br />
                    <p id="addressStreetOneRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The first street address must follow the expected address format, such as: 0000 Street Name
                    </p>
                    <form:input path="addressStreetOne" type="text" id="addressStreetOne"
                                onkeyup="validateAddressOne()"/><br />

                    <br/>
                    <form:label path="addressStreetTwo">Address Street Two:</form:label><br />
                    <p id="addressStreetTwoRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The second street address is optional, but it must follow the expected address format,
                        such as: 0000 Street Name
                    </p>
                    <form:input path="addressStreetTwo" type="text" id="addressStreetTwo"
                                onkeyup="validateAddressTwo()"/><br />
                    <br/>

                    <form:label path="city">City:</form:label><br />
                    <p id="cityRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The city should begin with letters, and may contain only letters, a hyphen, or period.
                    </p>
                    <form:input path="city" type="text" id="city" onkeyup="validateCity()"/><br />
                    <br/>

                    <form:label path="state">State:</form:label><br />
                    <form:select path="state">
                        <form:options items="${stateMap.stateMap}"/>
                    </form:select>
                    <br/>

                    <br/>
                    <form:label path="zipcode">Zipcode:</form:label><br />
                    <p id="zipcodeRequirements" class="inputRequirements inputErrorMsg hideElement">
                        The zipcode should be a sequence of 5 digits, or a sequence of 5 digits, followed
                        by a hyphen, and then followed by a sequence of 4 digits.
                    </p>
                    <form:input path="zipcode" type="text" id="zipcode" onkeyup="validateZipcode()"/><br />
                    <br/>
                    <input type="submit" value="Submit" />
                </form:form>

                <select id="formSubmissionOption">
                    <option value="post" id="postOption">POST</option>
                    <option value="get" id="getOption">GET</option>
                </select>

                <a href="<c:url value="/signIn" />">Sign In</a>
            </span>
        </div>

        <script>
            <jsp:directive.include file="/javascript/formValidation.js"/>
        </script>
    </body>
</html>
