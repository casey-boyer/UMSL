<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--The title contains the text "404 Error", which notifies
        the customer that an error occurred, and this text will be displayed
        in the tab of the web browser.--%>
        <title>404 Error</title>
        <%--The stylesheet for the error page(s).--%>
        <link rel="stylesheet" href="styles/errorStyles.css" type="text/css"/>
    </head>
    <body>
        <%--These p elements belong to the class "pageText" for styling.
        The first p element declares that a 404 ERROR occurred, and
        the second p element expands upon the error for the customer.--%>
        <p class="pageText">404 ERROR:</p>
        <p class="pageText">The server was unable to find the file you
        requested. To continue, hit the back button in the browser.</p>
    </body>
</html>