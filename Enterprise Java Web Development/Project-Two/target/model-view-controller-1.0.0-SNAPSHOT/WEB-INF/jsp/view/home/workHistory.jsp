<html>
    <head>
        <%---This is the only way intellij will recognize ANY external static files.---%>
        <style>
            <jsp:directive.include file="/styles/profile.css" />
            <jsp:directive.include file="/styles/formStyles.css"/>
        </style>
    </head>
    <body>
    <div class="card">
        <span class="description">
            <form:form method="post" action="?editWorkHistory" modelAttribute="workHistory"
                onsubmit="return validateWorkHistoryForm()" id="workHistoryForm" name="workHistory">
                <input type="hidden" name="id" value="${workHistory.id}"/>

                <form:label path="jobTitle">Job Title: </form:label>
                <p id="jobTitleRequirements" class="inputErrorMsg hideElement">
                    The job title must contain only alphabetic characters, followed by an optional space
                    or period.
                </p>
                <form:input path="jobTitle" value = "${workHistory.jobTitle}" type="text"
                            id="jobTitle" onkeyup="validateJobTitle()"/>
                <br/>

                <form:label path="companyName">Company Name: </form:label>
                <p id="companyNameRequirements" class="inputErrorMsg hideElement">
                    The company name must contain only alphabetic characters, followed by an optional space
                    or period.
                </p>
                <form:input path="companyName" value="${workHistory.companyName}" type="text"
                            id="companyName" onkeyup="validateCompanyName()"/>
                <br/>

                <form:label path="yearsOfService">Years of Service: </form:label>
                <p id="yearsOfServiceRequirements" class="inputErrorMsg hideElement">
                    Please enter a range of years in the format DDDD-DDDD.
                </p>
                <form:input path="yearsOfService" value="${workHistory.yearsOfService}" type="text"
                            id="yearsOfService" onkeyup="validateYearsOfService()"/>
                <br/>

                <input type="submit" value="Update"/>
            </form:form>
        </span>

        <select id="formSubmissionOption">
            <option value="post" id="postOption">POST</option>
            <option value="get" id="getOption">GET</option>
        </select>

        <script>
            <jsp:directive.include file="/javascript/formValidation.js"/>
        </script>
    </div>
    </body>
</html>
