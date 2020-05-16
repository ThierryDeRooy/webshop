<%@ include file="templates/header.jsp" %>
<%@ page import="java.math.BigDecimal"%>

<div class="page-title">Product List</div>

<c:if test="${(empty myShowOrder) || (empty myShowOrder.orderLines)}">
    <h2><spring:message code="lang.emptyCart" /></h2>
    <a href="/productList"><spring:message code="lang.showProductList" /></a>
</c:if>

<c:if test="${(not empty myShowOrder) && (not empty myShowOrder.orderLines)}">
<p><span><spring:message code="order.number" /> = ${myShowOrder.order.id}</span></p>

<table class="twoColorTable">
   <tr>
    <th><spring:message code="lang.code" /></th>
    <th><spring:message code="lang.productName" /></th>
    <th><spring:message code="lang.price" />(EUR)</th>
    <th><spring:message code="lang.quantity" /></th>
    <th><spring:message code="lang.BTW" />(EUR)</th>
    <th><spring:message code="lang.totalPrice" />(EUR)</th>
   </tr>
   <c:forEach var="orderLine" items="${myShowOrder.orderLines}" varStatus="status">
    <tr>
        <td>${orderLine.orderDetails.product.productDetails.code}</td>
        <td>${orderLine.orderDetails.productName}</td>
        <td>${orderLine.orderDetails.price}</td>
        <td>${orderLine.orderDetails.quantity}&nbsp;<spring:message code="lang.${Constants.PRODUCT_UNITS[orderLine.orderDetails.unit]}" /></td>
        <td>${orderLine.totalPriceInclBtw.subtract(orderLine.totalPrice)}</td>
        <td>${orderLine.totalPriceInclBtw}</td>
    </tr>
   </c:forEach>
   <c:forEach var="orderCost" items="${myShowOrder.order.orderCosts}" varStatus="status">
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
        <td>${myShowOrder.order.totalPriceInclBtwTransport}</td>
    </tr>
</table>


<br/>

<p>After payment this order will be send to you </p>
</c:if>



 </body>
</html>