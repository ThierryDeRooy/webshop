<%@ include file="templates/header.jsp" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="org.apache.commons.fileupload.*"%>

<h1>Country Administration</h1>
<div class="customer-info-container">

<div class="container-fluid">
    <div class="row">
    <div class="tableDetails">
<form:form action="saveCountry" modelAttribute="country" method="POST">

<table class="twoColorTable" >
    <tr>
        <td>Country code</td>
        <td>
            <form:input path="code" size="20" />
            <form:errors path="code" cssClass="error" />
        </td>
    </tr>
    <c:forEach var="language" items="${Constants.LANGUAGES}">
        <tr>
            <td>name in ${language}</td>
            <td>
            <form:input path="names[${language}]" size="20" />
            <form:errors path="names[${language}]" cssClass="error" />
            </td>
        </tr>
    </c:forEach>
    <tr>
        <td colspan="2" align="center">
        <input type="submit" value="ADD" name="btnSubmit" id="btnSubmitId" />
        </td>
    </tr>
</table>
</form:form>
</div>
</div>
</div>


<h2>Country List</h2>
<table class="twoColorTable tablesorter" id="countryTable">
    <thead>
    <tr>
        <th>Code</th>
        <c:forEach var="language" items="${Constants.LANGUAGES}">
             <th>${language}</th>
        </c:forEach>
        <th>Modify</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="country" items="${countries}">
        <tr>
            <form:form action="saveCountry" modelAttribute="country" method="POST">
            <td><form:input path="code" size="20" value="${country.code}" /></td>
            <c:forEach var="language" items="${Constants.LANGUAGES}" varStatus="ctr">
                <td>
                    <form:input path="names[${language}]" size="20" value="${country.names[language]}"/>
                    <form:hidden path="ids[${language}]" value="${country.ids[language]}" />
                </td>
            </c:forEach>
            <td><input type="submit" value="Update" name="btnSubmit" id="btnSubmitId" />
            </td>
            </form:form>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
<input id="nbLang" type="hidden" value="${fn:length(Constants.LANGUAGES)}" />

<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>
<script src="js/adminCountry.js"></script>

<jsp:include page="templates/footer.jsp"/>

</body>
</html>
