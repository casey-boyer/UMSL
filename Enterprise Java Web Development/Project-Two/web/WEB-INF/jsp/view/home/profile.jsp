<!DOCTYPE html>
<html>
    <head>
        <title>Profile</title>

        <style>
            <jsp:directive.include file="/styles/profile.css" />
            <jsp:directive.include file="/styles/formStyles.css"/>
        </style>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="card">
            <span class="description">
                <a href="<c:url value="/editDetails"/>">Edit Details</a><br/>

                <c:if test="${not empty user.profilePictureContents}">
                    <img src="/project2/img-response" width="100px" height="100px"/><br/>
                </c:if>

                <span class="item">Username:</span> ${user.username}<br/>
                <span class="item">First name:</span> ${user.firstName}<br/>
                <span class="item">Last name:</span> ${user.lastName}<br/>

                <c:if test="${not empty user.birthday}">
                    <span class="item">Birthday: </span>
                    ${user.birthday}<br/>
                </c:if>
                <c:if test="${not empty user.phoneNumber}">
                    <span class="item">Phone number: </span>
                    ${user.phoneNumber}<br/>
                </c:if>
                <c:if test="${not empty user.cellPhoneNumber}">
                    <span class="item">Cell phone number: </span>
                    ${user.cellPhoneNumber}<br/>
                </c:if>
                <c:if test="${not empty user.timezone}">
                    <span class="item">Timezone: </span>
                    ${user.timezone}<br/>
                </c:if>
                <c:if test="${not empty user.areasOfInterest}">
                    <span class="item">Areas of interest: </span>
                    ${user.areasOfInterest}<br/>
                </c:if>

                <span class="item">Address: </span>
                ${user.addressStreetOne}, ${user.city}, ${user.state}, ${user.zipcode}
                <br/>
            </span>
        </div>
        <br/>
        <div class="card">
            <p class="title">Work History:</p>

            <c:if test="${not empty workHistoryForm.workHistoryList}">

            <c:forEach items="${workHistoryForm.workHistoryList}" var="items" varStatus="status">

            <span class="description">
                    Job title: ${items.jobTitle}
                    <br/>

                    Company Name: ${items.companyName}
                    <br/>
                    Years of Service: ${items.yearsOfService}
                    <br/>

                    <a href="<c:url value="/workHistoryDelete/${items.id}"/>"
                       class="btn btn-info btn-lg">
                            <span class="glyphicon glyphicon-trash"></span> Delete
                    </a>
                    <a href="<c:url value="/editWorkHistory/${items.id}"/>"
                       class="btn btn-info btn-lg">
                            <span class="glyphicon glyphicon-trash"></span> Update
                    </a>

                    <p class="itemsList"></p>
                </c:forEach>
            </c:if>

            <a href="<c:url value="/workHistoryAdd"/>" class="btn btn-info btn-lg">
                <span class="glyphicon glyphicon-plus"></span> Add
            </a>
        </div>
        <br/>
        <div class="card">
            <p class="title">Education History:</p>

            <c:if test="${not empty educationHistoryForm.educationHistoryList}">

            <c:forEach items="${educationHistoryForm.educationHistoryList}" var="items" varStatus="status">

            <span class="description">
                Degree Type: ${items.degreeType}
                <br/>

                Degree Discipline: ${items.degreeDiscipline}
                <br/>
                
                Year Achieved: ${items.yearAchieved}
                <br/>

                University Name: ${items.universityName}
                <br/>

                <a href="<c:url value="/educationHistoryDelete/${items.id}"/>"
                   class="btn btn-info btn-lg">
                        <span class="glyphicon glyphicon-trash"></span> Delete
                </a>
                <a href="<c:url value="/editEducationHistory/${items.id}"/>"
                   class="btn btn-info btn-lg">
                        <span class="glyphicon glyphicon-trash"></span> Update
                </a>

                <p class="itemsList"></p>
            </c:forEach>
        </c:if>

            <a href="<c:url value="/educationHistoryAdd"/>" class="btn btn-info btn-lg">
                <span class="glyphicon glyphicon-plus"></span> Add
            </a>
        </div>
    </body>
</html>
