<%@ include file="templates/header.jsp" %>
<%@ page import="com.webshop.entity.Category"%>

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