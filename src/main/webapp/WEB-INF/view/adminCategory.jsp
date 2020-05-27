<%@ include file="templates/header.jsp" %>

<h1>Category Administration</h1>
<div class="customer-info-container">

<div class="container-fluid">
    <div class="row">
    <div class="tableDetails">

    <form:form action="saveCategory" modelAttribute="category" method="POST">
<table class="twoColorTable" >
    <tr>
        <td>Category code</td>
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
            <td>UpperCategory code</td>
            <td>
                    <form:select path="upperCatCode" >
                        <form:option value=""></form:option>
                        <c:forEach var="cat" items="${upperCats}">
                            <form:option value="${cat.code}">${cat.code}</form:option>
                        </c:forEach>
                    </form:select>
                 <span class="error-message"><form:errors path="upperCatCode" cssClass="error" /></span>
            </td>
    </tr>
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



<h2>All categories</h2>
<table class="twoColorTable tablesorter" id="categoryTable">
    <thead>
    <tr>
        <th>Code</th>
        <c:forEach var="language" items="${Constants.LANGUAGES}">
             <th>${language}</th>
        </c:forEach>
        <th>code upper category</th>
        <th>Modify</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="category" items="${categories}">
        <tr>
            <form:form action="saveCategory" modelAttribute="category" method="POST">
            <td><form:input path="code" size="20" value="${category.code}" /></td>
            <c:forEach var="language" items="${Constants.LANGUAGES}" varStatus="ctr">
                <td>
                    <form:input path="names[${language}]" size="20" value="${category.names[language]}"/>
                    <form:hidden path="ids[${language}]" value="${category.ids[language]}" />
                </td>
            </c:forEach>
            <td><form:input path="upperCatCode" size="20" value="${category.upperCatCode}" /></td>
            <td><input type="submit" value="Update" name="btnSubmit" id="btnSubmitId" />
            </td>
            </form:form>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>


<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>
<script src="js/adminCategory.js"></script>

<jsp:include page="templates/footer.jsp"/>

</body>
</html>




