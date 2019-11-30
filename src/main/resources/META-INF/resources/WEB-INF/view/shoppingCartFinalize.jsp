<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.webshop.kung.constants.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="templates/header.jsp"/>

<div class="page-title">Product List</div>

<c:if test="${(empty myShowOrder) || (empty myShowOrder.orderLines)}">
    <h2><spring:message code="lang.emptyCart" /></h2>
    <a href="/productList"><spring:message code="lang.showProductList" /></a>
</c:if>

<c:if test="${(not empty myShowOrder) && (not empty myShowOrder.orderLines)}">
<p><span>Order Number = ${myShowOrder.order.id}</span></p>

<table class="twoColorTable">
   <tr>
    <th><spring:message code="lang.code" /></th>
    <th><spring:message code="lang.productName" /></th>
    <th><spring:message code="lang.price" /></th>
    <th><spring:message code="lang.unit" /></th>
    <th><spring:message code="lang.quantity" /></th>
    <th><spring:message code="lang.totalPrice" /></th>
   </tr>
   <c:forEach var="orderLine" items="${myShowOrder.orderLines}" varStatus="status">
    <tr>
        <td>${orderLine.orderDetails.product.productDetails.code}</td>
        <td>${orderLine.orderDetails.productName}</td>
        <td>${orderLine.orderDetails.price}</td>
        <td><spring:message code="lang.per${Constants.PRODUCT_UNITS[orderLine.orderDetails.unit]}" /></td>
        <td>${orderLine.orderDetails.quantity}</td>
        <td>${orderLine.totalPrice}</td>
    </tr>
       </c:forEach>

    <tr>
        <td/><td/><td/><td/><td/>
        <td>${myShowOrder.totalPrice}</td><td/>
    </tr>
</table>


<br/>

<p>After payment this order will be send to you </p>
</c:if>



 </body>
</html>