<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="com.webshop.kung.constants.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="templates/header.jsp"/>

<div class="page-title">Product Details</div>


    <div>
    <form:form modelAttribute="cartLine" action="orderProduct" method="post">
        <form:hidden path="product" />
     <ul>
         <li><img class="product-image" src="<c:url value='${cartLine.product.productDetails.photoLoc}'/>"/></li>
         <li><spring:message code="lang.code" />: <span>${cartLine.product.productDetails.code}</span></li>
         <li><span>${cartLine.product.name}</span></li>
         <li><spring:message code="lang.price" />: <span>${cartLine.product.productDetails.price}</span> euro  <spring:message code="lang.per${Constants.PRODUCT_UNITS[cartLine.product.productDetails.unit]}" /></li>
         <li><span>${cartLine.product.description}</span></li>
         <li><spring:message code="lang.quantity" />: <form:input path="quantity" type="number" size="10" />
         <form:errors path="quantity" ><span class="error-message">Enter valid quantity</span></form:errors></li>
         <li>
             <input type="submit" value="Add to cart" name="btnSubmit" />
         </li>
     </ul>

    </form:form>

   </div>


<br/>

</body>
</html>