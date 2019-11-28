<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="com.webshop.kung.entity.Category"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<jsp:include page="templates/header.jsp"/>


    <div>
        <div>
            <h1>Spring Boot JSP Example</h1>
            <h2>Hello, these are the main categories</h2>
            <table>
            <c:forEach items="${categories}" var="category">
                <tr>
                    <td>cat ID = <c:out value="${category.id}"/></td>
                    <td>cat naam = <c:out value="${category.names['NL']}"/></td>
                    <td>beschrijving = <c:out value="${category.descriptions['EN']}"/></td>
                </tr>
            </c:forEach>
            </table>
            <h2>Hello, these are the main categories</h2>
            <table>
            <% Iterator<Category> catIt = ((List<Category>) request.getAttribute("categories")).iterator();
               while (catIt.hasNext()) {
                    Category category = catIt.next(); %>
                <tr>
                    <td>cat ID = <% out.println(category.getId()); %></td>
                    <td>cat name = <% out.println(category.getName("EN")); %></td>
                    <td>beschrijving = <% out.println(category.getDescription("NL")); %></td>
                </tr>
                <% } %>
            </table>
            Click on this <strong><a href="next">link</a></strong> to visit another page.
        </div>
    </div>
</body>
</html>