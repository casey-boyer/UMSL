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
                <form:form method="post" action="?editEducationHistory" modelAttribute="educationHistory"
                    onsubmit="return validateEducationHistoryForm()" id="educationHistoryForm"
                    name="educationHistory">

                    <input type="hidden" name="id" value="${educationHistory.id}"/>

                    <form:label path="degreeType">Degree Type: </form:label>
                    <p id="degreeTypeRequirements" class="inputErrorMsg hideElement">
                        The degree type must contain only alphabetic characters, and optional periods or
                        commas.
                    </p>
                    <form:input path="degreeType" value = "${educationHistory.degreeType}" type="text"
                                id="degreeType" onkeyup="validateDegreeType()"/>
                    <br/>

                    <form:label path="degreeDiscipline">Degree Discipline: </form:label>
                    <p id="degreeDisciplineRequirements" class="inputErrorMsg hideElement">
                        The degree discipline must contain only alphabetic characters, and optional periods
                        or commas.
                    </p>
                    <form:input path="degreeDiscipline" value="${educationHistory.degreeDiscipline}" type="text"
                                id="degreeDiscipline" onkeyup="validatedegreeDiscipline()"/>
                    <br/>

                    <form:label path="yearAchieved">Year Achieved: </form:label>
                    <p id="yearAchievedRequirements" class="inputErrorMsg hideElement">
                        The year achieved must be a single year, containing only digits, in the DDDD format.
                    </p>
                    <form:input path="yearAchieved" value="${educationHistory.yearAchieved}" type="text"
                            id="yearAchieved" onkeyup="validateDegreeYear()"/>
                    <br/>

                    <form:label path="universityName">University Name: </form:label>
                    <p id="universityNameRequirements" class="inputErrorMsg hideElement">
                        The university name must contain only alphabetic characters, with optional
                        commas or periods.
                    </p>
                    <form:input path="universityName" value="${educationHistory.universityName}" type="text"
                                id="universityName" onkeyup="validateUniversityName()"/>
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
