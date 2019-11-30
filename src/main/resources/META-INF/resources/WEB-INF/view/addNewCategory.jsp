<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="templates/header.jsp"/>

<h1>Add new Category</h1>

<form:form action="saveCategory" modelAttribute="category">
<table>
    <tr>
        <td>Category</td>
        <td>
        <form:select path="upperCategory" >
             <form:option value="0"> - </form:option>
            <c:forEach var="cat" items="${categorien}">
                <form:option value="${cat.key}">${cat.value}</form:option>
            </c:forEach>
        </form:select>
        </td>
    </tr>
    <tr>
        <td>English name</td>
        <td><form:input path="name" size="20" /></td>
    </tr>
    <tr>
        <td colspan="2" align="center">
        <input type="submit" value="Add Category" name="btnSubmit" /> </td>
    </tr>
</table>

</form:form>

</body>
</html>