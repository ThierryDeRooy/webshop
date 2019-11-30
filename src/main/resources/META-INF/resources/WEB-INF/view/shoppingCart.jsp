<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.webshop.kung.constants.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<link rel="stylesheet" type="text/css" href="css/table.css">
<jsp:include page="templates/header.jsp"/>

<div class="page-title">Product List</div>

<c:if test="${(empty myCart) || (empty myCart.cartLines)}">
    <h2><spring:message code="lang.emptyCart" /></h2>
    <a href="/productList"><spring:message code="lang.showProductList" /></a>
</c:if>

<c:if test="${(not empty myCart) && (not empty myCart.cartLines)}">

<form:form method="POST" modelAttribute="myCart" action="shoppingCart">

<table class="twoColorTable">
   <tr>
    <th><spring:message code="lang.code" /></th>
    <th><spring:message code="lang.productName" /></th>
    <th><spring:message code="lang.price" /></th>
    <th><spring:message code="lang.unit" /></th>
    <th><spring:message code="lang.quantity" /></th>
    <th><spring:message code="lang.totalPrice" /></th>
    <th></th>
   </tr>
   <c:forEach var="myCartLine" items="${myCart.cartLines}" varStatus="status">
    <tr>
        <td>${myCartLine.product.productDetails.code}
            <form:hidden path="cartLines[${status.index}].product" />
        </td>
        <td>${myCartLine.product.name}</td>
        <td>${myCartLine.product.productDetails.price}</td>
        <td><spring:message code="lang.per${Constants.PRODUCT_UNITS[myCartLine.product.productDetails.unit]}" /></td>
        <td>
            <form:input path="cartLines[${status.index}].quantity" value="${myCartLine.quantity}" type="number" />
            <form:errors path="cartLines[${status.index}].quantity" ><span class="error-message">Enter valid quantity</span></form:errors></li></td>
        <td>${myCartLine.totalPrice}</td>
        <td><a href="shoppingCartRemoveProduct?code=${myCartLine.product.productDetails.code}">
                                   <spring:message code="lang.delete" />
                                </a></td>
    </tr>
       </c:forEach>

    <tr>
        <td/><td/><td/><td/><td/>
        <td>${myCart.totalPrice}</td><td/>
    </tr>
</table>
       <div style="clear: both"></div>
        <input class="button-update-sc" type="submit" value="Update Quantity" />
        <a class="navi-item"
           href="/shoppingCartCustomer">Enter Customer Info</a>
        <a class="navi-item"
           href="/productList">Continue Buy</a>

</form:form>
<br/>
</c:if>



 </body>
</html>