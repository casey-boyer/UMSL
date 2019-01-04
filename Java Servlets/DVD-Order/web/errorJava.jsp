<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--The title, which contains the text "ERROR" that will be displayed
        in the tab of the web browser, indicates to the customer that an error
        has ocurred.--%>
        <title>ERROR</title>
        <%--The stylesheet for the error page(s).--%>
        <link rel="stylesheet" href="styles/errorStyles.css" type="text/css"/>
    </head>
    <body>
        <%--These first two p tags notify the customer that an
        error has ocurred.--%>
        <p class="pageText">Sorry, an error has occurred.</p>
        <p class="pageText">To continue, please hit the back button
            in your browser.</p>
        <br/>
        
        <%--Now, the type of error that occurred is detailed.
        The exception class is displayed and the details
        of that exception are displayed.--%>
        <p class="pageText">Details:</p>
        <p class="pageText">${pageContext.exception["class"]}</p>
        <p class="pageText">${pageContext.exception.message}</p>
    </body>
</html>
