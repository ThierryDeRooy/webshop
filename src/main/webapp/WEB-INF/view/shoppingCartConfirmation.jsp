<%@ include file="templates/header.jsp" %>

<%@ page import="java.math.BigDecimal"%>
<%@ page import="com.webshop.entity.Category"%>

<link rel="stylesheet" type="text/css" href="css/productList.css">

<div class="page-title"><spring:message code="confirmation" /></div>
<div class="customer-info-container">
    <h3><spring:message code="customer.info" />:</h3>
    <ul>
        <li><spring:message code="lang.name" />: <span>${myCart.customerInfo.name}</span></li>
        <li><spring:message code="lang.emailAddress" />: <span>${myCart.customerInfo.email}</span></li>
        <li><spring:message code="lang.address" />: <span>${myCart.customerInfo.address}</span></li>
        <li><spring:message code="lang.postcode" />: <span>${myCart.customerInfo.postCode}</span></li>
        <li><spring:message code="lang.city" />: <span>${myCart.customerInfo.city}</span></li>
        <li><spring:message code="lang.country" />: <span>${myCart.customerInfo.country.country}</span></li>
        <li><spring:message code="lang.phone" />: <span>${myCart.customerInfo.phone}</span></li>
        <li><spring:message code="lang.btwNr" />: <span>${myCart.customerInfo.btwNr}</span></li>
    </ul>
    <h3><spring:message code="cart.summary" />:</h3>
    <table class="twoColorTable">
       <tr>
        <th><spring:message code="lang.code" /></th>
        <th><spring:message code="lang.productName" /></th>
        <th><spring:message code="lang.price" />(EUR)</th>
        <th><spring:message code="lang.quantity" /></th>
        <th><spring:message code="lang.BTW" />(EUR)</th>
        <th><spring:message code="lang.totalPrice" />(EUR)</th>
       </tr>
       <c:forEach var="cartLine" items="${myCart.cartLines}" varStatus="status">
        <tr>
            <td>${cartLine.product.productDetails.code}</td>
            <td>${cartLine.product.name}</td>
            <td>${cartLine.product.productDetails.price}</td>
            <td>${cartLine.quantity}&nbsp;<spring:message code="lang.${Constants.PRODUCT_UNITS[cartLine.product.productDetails.unit]}" /></td>
            <td>${cartLine.totalPriceInclBtw.subtract(cartLine.totalPrice)}</td>
            <td>${cartLine.totalPriceInclBtw}</td>
        </tr>
       </c:forEach>
       <c:forEach var="orderCost" items="${myCart.orderCosts}" varStatus="status">
        <tr>
            <td></td>
            <td>${orderCost.description}</td>
            <td>${orderCost.price}</td>
            <td>${orderCost.quantity}&nbsp;<spring:message code="lang.${Constants.PRODUCT_UNITS[Constants.PER_UNIT]}" /></td>
            <td>${orderCost.vat}</td>
            <td>${orderCost.totalPriceInclVat}</td>
        </tr>
       </c:forEach>

        <tr>
            <td></td><td><spring:message code="lang.totalPrice" /></td><td></td><td></td><td></td>
            <td>${myCart.totalPriceInclBtw}</td>
        </tr>
    </table>




    <ul>
        <li><spring:message code="lang.total.quantity" />: <span>${myCart.numberOfProducts}</span></li>
        <li><spring:message code="lang.totalPrice" />:
            <span class="total">
                  ${myCart.totalPriceInclBtw} EUR
               </span>
        </li>
    </ul>
</div>
<form:form method="POST" action="shoppingCartConfirmation">
    <!-- Edit Cart -->
    <a class="navi-item" href="shoppingCart">
        <spring:message code="edit.cart" />
    </a>
    <!-- Edit Customer Info -->
    <a class="navi-item" href="shoppingCartCustomer">
        <spring:message code="edit.customer.info" />
    </a>
    <!-- Send/Save -->
    <input type="submit" value="<spring:message code='confirm.purchase' />" class="button-send-sc" />
</form:form>
<div class="container">
    <c:forEach var="myCartLine" items="${myCart.cartLines}" varStatus="status">
        <div class="card">
          <img src="<c:url value='${myCartLine.product.productDetails.photoLoc}'/>" alt="${myCartLine.product.productDetails.code}" class="cardImg" />
          <p>Code: <span> ${myCartLine.product.productDetails.code}</span></p>
            <div class="cardProdNameOuterbox">
                <div class="cardProdNameInnerbox">
                    <h1>${myCartLine.product.name}</h1>
                </div>
             </div>

          <h3>--${myCartLine.product.category.name}--</h3><br/>
          <p><span class="price">${myCartLine.product.productDetails.priceInclBtw} EUR</span>
          <br/><spring:message code="lang.per${Constants.PRODUCT_UNITS[myCartLine.product.productDetails.unit]}" /></p>
          <p><spring:message code="lang.quantity" />: <span>${myCartLine.quantity}</span></p>
          <p><spring:message code="lang.totalPrice" />: <span class="subtotal">${myCartLine.totalPriceInclBtw}</span></p>
        </div>

    </c:forEach>
</div>


<jsp:include page="templates/footer.jsp"/>

</body>
</html>