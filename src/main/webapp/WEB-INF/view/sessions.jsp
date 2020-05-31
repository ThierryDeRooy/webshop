<%@ include file="templates/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="creationDate" class="java.util.Date" />
<jsp:useBean id="lastAccessDate" class="java.util.Date" />



<h2>SESSIONS  (${fn:length(sessions)})</h2>
<div class="container-fluid">
    <div class="row">
    <div class="tableDetails">

<table class="twoColorTable tablesorter" id="itemsTable">
    <thead>
    <tr>
        <th>ID</th>
        <th>USER</th>
        <th>ROLE</th>
        <th>BASKET</th>
        <th>START TIME</th>
        <th>TIME Last ACTION</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
        <c:forEach var="sess" items="${sessions}" >
            <jsp:setProperty name="creationDate" property="time" value="${sess.creationTime}" />
            <jsp:setProperty name="lastAccessDate" property="time" value="${sess.lastAccessedTime}" />
            <tr>
                <td>${sess.id}</td>
                <td>${sess.getAttribute("username")}</td>
                <td>${sess.getAttribute("role")}</td>
                <td>${sess.getAttribute("myOrders").totalPriceInclBtw}</td>
                <td><fmt:formatDate value="${creationDate}" pattern="dd/MM/yyyy hh:mm:ss a" /></td>
                <td><fmt:formatDate value="${lastAccessDate}" pattern="dd/MM/yyyy hh:mm:ss a" /></td>
                <td><a href="/removeSession?sessionId=${sess.id}">REMOVE</a></td>
             </tr>
        </c:forEach>
    </tbody>

 </table>
</div>
</div>
</div>
<script src="js/orderTable.js"></script>

<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>
<jsp:include page="templates/footer.jsp"/>
</body>
</html>
