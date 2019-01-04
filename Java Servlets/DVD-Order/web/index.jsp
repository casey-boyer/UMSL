<%--Add the taglib for the JSTL core library.--%>
<%@page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%--The link to the stylesheet for the page.--%>
        <link rel="stylesheet" href="styles/index.css" type="text/css"/>
        <%--The title of the page, which will be displayed
        on the tab in the customer's browser, clearly
        indicates the purpose of the page.--%>
        <title>Order DVDs</title>
    </head>
    <body>
        <%--This div element is used as the page title. It
        has its own id by convention purposes but also
        for styling, and belongs to the classes 'centerText' and 'pageHeader'.--%>
        <div id="pageTitle" class="centerText pageHeader">DVD Order Form</div><br/>
        
        <%--This p element describes the basic purpose of the page for the
        customer. It has its own id by convention purpose,
        and belongs to the class pageText for styling.--%>
        <p id="pageDescription" class="pageText centerText">Please select the DVD(s) you 
            wish to order.</p>
        <%--The table which displays the column headers
        Cover, Title, Price, and Add to Cart for each DVD.
        The table belongs to the class pageText for styling.
        For the four table rows, in each "Add to Cart" column,
        there is a form that processes the user's request
        when they click the "Add to Cart" button.
        Each form element has the action attribute equal to "order"
        so the servlet can process the DVD request, and the
        method is post.
        Within each form, the "Add to Cart" button is an
        input element with the value "submit," so when clicked
        the servlet can process the user request. Additionally,
        there is a hidden input field with the name "productCode",
        and the value attribute for the hidden input field
        corresponds to each DVD's product code in the ProductList table (in
        the DvdDB database). Thus, this product code is retrieved by the servlet,
        and the servlet uses the product code with the ProductDB class to 
        select the DVD's title, price, and cover.--%>
        <table class="pageText tableStyle">
            <tr>
                <th id="columnOne" class="tableHeaders">Cover</th>
                <th id="columnTwo" class="tableHeaders">Title</th>
                <th id="columnThree" class="tableHeaders">Price</th>
                <th id="columnFour" class="tableHeaders">Add to Cart</th>
            </tr>
            <tr>
                <td class="imageColumn"><img src="http://img.moviepostershop.com/death-proof-movie-poster-2007-1020403304.jpg" class="movieImages"/></td>
                <td class="titleColumn">Death Proof</td>
                <td class="priceColumn">$19.99</td>
                <td class="submitColumn">
                    <%--The form for the first DVD, which displays an "Add to Cart" button and
                    uses a hidden input field for the DVD's product code.--%>
                    <form action="order" method="post">
                        <input type="hidden" name="productCode" value="1"/>
                        <input type="submit" value="Add to Cart" class="buttonStyles">
                    </form>
                </td>
            </tr>
            <tr>
                <td class="imageColumn"><img src="https://images-na.ssl-images-amazon.com/images/I/51GXZ-j2x0L._SY450_.jpg"class="movieImages"/></td>
                <td class="titleColumn">2001: <br/> A Space Odyssey</td>
                <td class="priceColumn">$19.99</td>
                <td class="submitColumn">
                    <%--The form for the second DVD, which displays an "Add to Cart" button
                    and uses a hidden input field for the DVD's product code.--%>
                    <form action="order" method="post">
                        <input type="hidden" name="productCode" value="2"/>
                        <input type="submit" value="Add to Cart" class="buttonStyles">
                    </form>
                </td>
            </tr>
            <tr>
                <td class="imageColumn"><img src="https://static.zerochan.net/Heisei.Tanuki.Gassen.Pom.Poko.full.69629.jpg" class="movieImages"/></td>
                <td class="titleColumn">Pom Poko</td>
                <td class="priceColumn">$25.99</td>
                <td class="submitColumn">
                    <%--The form for the third DVD, which displays an "Add to Cart" button
                    and uses a hidden input field for the DVD's product code.--%>
                    <form action="order" method="post">
                        <input type="hidden" name="productCode" value="3"/>
                        <input type="submit" value="Add to Cart" class="buttonStyles">
                    </form>
                </td>
            </tr>
            <tr>
                <td class="imageColumn"><img src="https://img00.deviantart.net/04a0/i/2007/156/e/4/eagle_vs_shark_2_by_drawie.jpg" class="movieImages"/></td>
                <td class="titleColumn">Eagle Vs. Shark</td>
                <td class="priceColumn">$16.99</td>
                <td class="submitColumn">
                    <%--The form for the fourth DVD, which displays an "Add to Cart" button
                    and uses a hidden input field for the DVD's product code.--%>
                    <form action="order" method="post">
                        <input type="hidden" name="productCode" value="4"/>
                        <input type="submit" value="Add to Cart" class="buttonStyles">
                    </form>
                </td>
            </tr>
        </table>
    </body>
</html>