<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="templates/header.jsp"/>




<div class="page-title">Confirmation</div>
<div class="customer-info-container">
    <h3>Customer Information:</h3>
    <ul>
        <li>Name: <span>${myCart.customerInfo.name}</span></li>
        <li>Email: <span>${myCart.customerInfo.email}</span></li>
        <li>Address: <span>${myCart.customerInfo.address}</span></li>
        <li>Post code: <span>${myCart.customerInfo.postCode}</span></li>
        <li>City: <span>${myCart.customerInfo.city}</span></li>
        <li>Country: <span>${myCart.customerInfo.country.country}</span></li>
        <li>Phone: <span>${myCart.customerInfo.phone}</span></span></li>
    </ul>
    <h3>Cart Summary:</h3>
    <ul>
        <li>Quantity: <span>${myCart.numberOfProducts}</span></li>
        <li>Total:
            <span class="total">
                  ${myCart.totalPrice} EUR
               </span>
        </li>
    </ul>
</div>
<form method="POST" action="shoppingCartConfirmation">
    <!-- Edit Cart -->
    <a class="navi-item" href="shoppingCart">
        Edit Cart
    </a>
    <!-- Edit Customer Info -->
    <a class="navi-item" href="shoppingCartCustomer">
        Edit Customer Info
    </a>
    <!-- Send/Save -->
    <input type="submit" value="Send" class="button-send-sc" />
</form>
<div class="container">
    <c:forEach var="myCartLine" items="${myCart.cartLines}" varStatus="status">
    <div class="product-preview-container" >
        <ul>
            <li>
                <img class="product-image"
                      src="<c:url value='${myCartLine.product.productDetails.photoLoc}'/>" />
            </li>
            <li>
                Code: <span> ${myCartLine.product.productDetails.code}</span>
                <input
                        type="hidden" name="code" value="${myCartLine.product.productDetails.code}" />
            </li>
            <li>Name: <span>${myCartLine.product.name}</span></li>
            <li>Price:
                <span class="price">
                      ${myCartLine.product.productDetails.price}
                  </span>
            </li>
            <li>Quantity: <span>${myCartLine.quantity}</span></li>
            <li>Subtotal:
                <span class="subtotal">
                     ${myCartLine.totalPrice}
                  </span>
            </li>
        </ul>
    </div>
    </c:forEach>
</div>



</body>
</html>