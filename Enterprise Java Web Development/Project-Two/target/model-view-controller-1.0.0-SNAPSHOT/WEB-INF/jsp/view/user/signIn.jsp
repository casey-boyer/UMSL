<%@ page import="edu.umsl.site.form.SignInForm" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Sign In</title>

        <%---This is the only way intellij will recognize ANY external static files.---%>
        <style>
            <jsp:directive.include file="/styles/profile.css" />
            <jsp:directive.include file="/styles/formStyles.css" />
        </style>
    </head>
    <body class="card">
        <span class="description">
            <c:if test="${not empty errorMessage}">
                ${errorMessage}
            </c:if>

            <form:form method="post" modelAttribute="signInForm" id="signInForm" name="signIn"
                       onsubmit="return validateSignInForm()">
                <form:label path="username">Username:</form:label><br />
                <p id="usernameRequirements" class="inputRequirements inputErrorMsg hideElement">
                    Usernames only contain letters, numbers, and underscore characters. All usernames
                    are at least 6 characters.
                </p>
                <form:input path="username" type="text" id="username" onkeyup="validateUsername()"/><br />
                <br />

                <form:label path="password">Password:</form:label><br />
                <p id="passwordRequirements" class="inputRequirements inputErrorMsg hideElement">
                    All passwords are at least 8 characters.
                </p>
                <form:input path="password" id="pwd" type="password" onkeyup="validatePassword()"/><br />
                <input type="submit" value="Submit" id="submitSignInForm"/>
            </form:form>

            <select id="formSubmissionOption">
                <option value="post" id="postOption">POST</option>
                <option value="get" id="getOption">GET</option>
            </select>
            <br/>

            <a href="<c:url value="/register" />">Register</a>
        </span>

        <script>
            <jsp:directive.include file="/javascript/formValidation.js"/>
        </script>
    </body>
</html>
