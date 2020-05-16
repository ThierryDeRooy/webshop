<%@ include file="templates/header.jsp" %>
<%@ page import="org.apache.commons.fileupload.*"%>

<h1>Transport Cost</h1>
<div class="customer-info-container">
<c:if test="${not empty generalError}" >
<p><span class="error">${generalError}</span></p>
</c:if>



<div class="container-fluid">
<form:form action="saveTransportCost" modelAttribute="transportCost" method="POST">
    <div class="row">
    <div class="tableDetails">
<table class="twoColorTable" >
    <tr>
        <td>Country</td>
        <td>
            <form:select path="country" >
                <c:forEach var="land" items="${countries}">
                    <form:option value="${land.id}">${land.country}</form:option>
                </c:forEach>
            </form:select>
        </td>
    </tr>
    <tr>
        <td>Max Transport points for 1 cost unit</td>
        <td>
            <form:input path="points" size="20" />
            <form:errors path="points" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>costPrice for 1 cost unit</td>
        <td>
            <form:input path="costPrice" size="20" />
            <form:errors path="costPrice" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>VAT costPrice (%)</td>
        <td>
            <form:input path="vat" size="20" />
            <form:errors path="vat" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td colspan="2" align="center">
        <input type="submit" value="Update" name="btnSubmit" id="btnSubmitId" />
        </td>
    </tr>
</table>
</div>
</div>
<form:hidden path="id" />
</form:form>

</div>


<br/>
<br/>
<h2>Transport cost per country</h2>
<table class="twoColorTable tablesorter" id="transportCostTable">
    <thead>
    <tr>
        <th>Country</th>
        <th>Max. Transport points for 1 unit</th>
        <th>price for 1 unit</th>
        <th>VAT (%)</th>
        <th>Modify</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="transportCost" items="${transportCosts}">
        <tr>
            <td class="ClickCountry" value="${transportCost.country.id}" >${transportCost.country.country}</td>
            <td class="ClickPoints">${transportCost.points}</td>
            <td class="ClickCostPrice">${transportCost.costPrice}</td>
            <td class="ClickVat">${transportCost.vat}</td>
            <td><a class="Clickable" href="#" >MODIFY</a>
                <hidden class="ClickId" value="${transportCost.id}"/>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>


</div>


<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>
<script src="js/transportCost.js"></script>


</body>
</html>
