<html>
    <head>
        <title>404 ERROR</title>
        <%---This is the only way intellij will recognize ANY external static files.---%>
        <style>
            <jsp:directive.include file="/styles/formStyles.css"/>
        </style>
    </head>
    <body>
        <p id="errorMessage404" class="inputErrorMsg">
            YOU SHOULD NOT SUBMIT FORMS USING GET!
        </p>
    </body>
</html>
