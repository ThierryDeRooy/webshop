<%@ include file="templates/header.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<c:if test="${not empty chosenOrder}">
<br/>
<h2><spring:message code="lang.reference" />: ${chosenOrder.id} - <spring:message code="lang.date" />: <fmt:formatDate pattern="dd-MM-yyyy" value="${chosenOrder.date}" /></h2>
<p>order status: ${Constants.ORDER_STATES[chosenOrder.status]}</p>
<p>${chosenOrder.receiverName}<br/>
    ${chosenOrder.receiverAddress}<br/>
    ${chosenOrder.receiverPostNr} ${chosenOrder.receiverCity}<br/>
    ${chosenOrder.receiverCountry.country}<br/>
    ${chosenOrder.receiverEmail}<br/>
    ${chosenOrder.receiverBtw}</p>


<h3><spring:message code="lang.order" /> - <spring:message code="lang.reference" />: ${chosenOrder.id}</h3>
<table class="twoColorTable tablesorter" id="itemsTable">
    <thead>
    <tr>
        <th><spring:message code="lang.code" /></th>
        <th><spring:message code="lang.productName" /></th>
        <th><spring:message code="lang.price" />(EUR)</th>
        <th><spring:message code="lang.quantity" /></th>
        <th><spring:message code="lang.BTW" />(EUR)</th>
        <th><spring:message code="lang.totalPrice" />(EUR)</th>
    </tr>
    </thead>
    <tbody>
<c:forEach var="orderLine" items="${chosenOrder.orderDetails}" >
         <tr>
            <td>${orderLine.product.productDetails.code}</td>
            <td>${orderLine.productName}</td>
            <td>${orderLine.price}</td>
            <td>${orderLine.quantity}&nbsp;<spring:message code="lang.${Constants.PRODUCT_UNITS[orderLine.unit]}" /></td>
            <td>${orderLine.totalPriceInclBtw.subtract(orderLine.totalPrice)}</td>
            <td>${orderLine.totalPriceInclBtw}</td>
         </tr>

</c:forEach>
   <c:forEach var="orderCost" items="${chosenOrder.orderCosts}" varStatus="status">
    <tr>
        <td></td>
        <td>${orderCost.description}</td>
        <td>${orderCost.price}</td>
        <td>${orderCost.quantity}&nbsp;<spring:message code="lang.${Constants.PRODUCT_UNITS[Constants.PER_UNIT]}" /></td>
        <td>${orderCost.vat}</td>
        <td>${orderCost.totalPriceInclVat}</td>
    </tr>
   </c:forEach>
    </tbody>
           <tr>
               <td colspan="5"></td>
               <td>${chosenOrder.totalPriceInclBtwTransport}</td>
           </tr>

</table>

<br/>

<div style="display: inline-block;">
        <form:form modelAttribute="chosenOrder" action="orderSame" method="post">
             <input type="submit" value="<spring:message code="lang.add.same"/>" name="btnSubmit" />
        </form:form>
</div>
<div style="display: inline-block;">
        <form:form modelAttribute="chosenOrder" action="orderIdentically" method="post">
             <input type="submit" value="<spring:message code="lang.order.same"/>" name="btnSubmit" />
        </form:form>
</div>

<br/>
</c:if>
<br/>
<h2><spring:message code="lang.myOrders" /></h2>

<table class="twoColorTable tablesorter" id="ordersTable">
    <thead>
    <tr>
        <th><spring:message code="lang.reference" /></th>
        <th><spring:message code="lang.date" /></th>
        <th><spring:message code="lang.quantity" /></th>
        <th><spring:message code="lang.price" /></th>
        <th><spring:message code="lang.state" /></th>
        <th><spring:message code="lang.moreDetails" /></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="order" items="${orders}" varStatus="status">
        <tr>
            <td>${order.id}</td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${order.date}" /></td>
            <td>${order.quantity}</td>
            <td>${order.totalPriceInclBtwTransport}</td>
            <td>${Constants.ORDER_STATES[order.status]}</td>
            <td><a href="?id=${order.id}">Details</a></td>
        </tr>
    </c:forEach>
    </tbody>

</div>
<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>
<script src="js/orderTable.js"></script>


</body>
</html>
