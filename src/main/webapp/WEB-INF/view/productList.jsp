<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="com.webshop.kung.constants.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="templates/header.jsp"/>

<div class="page-title">Product List</div>


   <c:forEach var="product" items="${products}">
    <div class="product-preview-container" >
    <ul>
        <li><img class="product-image" src="<c:url value='${product.productDetails.photoLoc}'/>"/></li>
        <li><spring:message code="lang.code" />: <span>${product.productDetails.code}</span></li>
        <li><spring:message code="lang.productName" />: <span>${product.name}</span></li>
        <li><spring:message code="lang.price" />: <span>${product.productDetails.price}</span> euro  <spring:message code="lang.per${Constants.PRODUCT_UNITS[product.productDetails.unit]}" /></li>
        <li>
            <a href="buyProduct?code=${product.productDetails.code}">Buy Now</a>
        </li>
    </ul>
   </div>
    </c:forEach>


<br/>

</body>
</html>