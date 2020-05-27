<%@ include file="templates/header.jsp" %>
<%@ page import="java.math.BigDecimal"%>

<div class="page-title"><spring:message code="lang.productlist" /></div>

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
    <th><spring:message code="lang.price" />(EUR)</th>
    <th><spring:message code="lang.quantity" /></th>
    <th><spring:message code="lang.BTW" />(EUR)</th>
    <th><spring:message code="lang.totalPrice" />(EUR)</th>
    <th></th>
   </tr>
   <c:forEach var="myCartLine" items="${myCart.cartLines}" varStatus="status">
    <tr>
        <td>${myCartLine.product.productDetails.code}
            <form:hidden path="cartLines[${status.index}].product" />
        </td>
        <td>${myCartLine.product.name}</td>
        <td>${myCartLine.product.productDetails.price}</td>
        <td>
            <form:input path="cartLines[${status.index}].quantity" value="${myCartLine.quantity}" type="number" min="0" max="${stock[myCartLine.product.productDetails.id]}"/>
            <spring:message code="lang.${Constants.PRODUCT_UNITS[myCartLine.product.productDetails.unit]}" />
            <form:errors path="cartLines[${status.index}]" class="error-message"></form:errors>
        </td>
        <td>${myCartLine.totalPriceInclBtw.subtract(myCartLine.totalPrice)}</td>
        <td>${myCartLine.totalPriceInclBtw}</td>
        <td><a href="shoppingCartRemoveProduct?code=${myCartLine.product.productDetails.code}">
                                   <spring:message code="lang.delete" />
                                </a></td>
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
            <td></td>
        </tr>
       </c:forEach>

    <tr>
        <td></td><td><spring:message code="lang.totalPrice" /></td><td></td><td></td><td></td>
        <td>${myCart.totalPriceInclBtw}</td><td></td>
    </tr>
</table>
       <div class="clearFields"></div>
        <input class="button-update-sc" type="submit" value="<spring:message code='update.quantity' />" />
        <a class="navi-item"
           href="/shoppingCartCustomer"><spring:message code="enter.customer.info" /></a>
        <a class="navi-item"
           href="/productList"><spring:message code="continue.buy" /></a>

</form:form>
<br/>
</c:if>


<jsp:include page="templates/footer.jsp"/>

 </body>
</html>