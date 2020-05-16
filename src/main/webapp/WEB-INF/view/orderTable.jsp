<%@ include file="templates/header.jsp" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>

<link rel="stylesheet" type="text/css" href="css/orderTable.css">

<c:if test="${not empty resentStatus}">
    <br/>
    <p>STATUS RESENT EMAIL:  ${resentStatus}</p>
</c:if>
<c:if test="${not empty chosenOrder}">
<br/>
<h2>Order details</h2>
<form:form action="updateOrder" modelAttribute="chosenOrder" >
<div class="container-fluid">
    <div class="row">
    <div class="tableDetails">

<table class="twoColorTable">
    <tr>
        <th colspan="2" >Order summary</th>
    </tr>
    <tr>
        <td>ORDER ID</td>
        <td>${chosenOrder.id}<form:hidden path="id" value="${chosenOrder.id}"/></td>
    </tr>
    <tr>
        <td>STATUS</td>
        <td>
                <form:select path="status" >
                    <c:forEach var="state" items="${Constants.ORDER_STATES}">
                        <form:option value="${state.key}">${state.value}</form:option>
                    </c:forEach>
                </form:select>
        </td>
    </tr>
    <tr>
        <td>DATE</td>
        <td><fmt:formatDate pattern="dd-MM-yyyy" value="${chosenOrder.date}" /></td>
    </tr>
    <tr>
        <td>Total items</td>
        <td>${chosenOrder.quantity}</td>
    </tr>
    <tr>
        <td>TOTAL PRICE</td>
        <td>${chosenOrder.totalPrice}</td>
    </tr>
    <tr>
        <td>TOTAL VAT</td>
        <td>${chosenOrder.totalPriceInclBtw.subtract(chosenOrder.totalPrice)}</td>
    </tr>
    <tr>
        <td>TOTAL COSTS</td>
        <td>${chosenOrder.totalPriceInclTransport.subtract(chosenOrder.totalPrice)}</td>
    </tr>
    <tr>
        <td>TOTAL PRICE (+VAT)</td>
        <td>${chosenOrder.totalPriceInclBtwTransport}</td>
    </tr>
    <tr>
        <td>Registered user</td>
        <td>
            <c:if test="${chosenOrder.customer != null}">
                ${chosenOrder.customer.webUser.username}
            </c:if>
            <c:if test="${chosenOrder.customer == null}">
                not registered
            </c:if>
    </tr>
    <c:if test="${chosenOrder.customer != null}">
    <tr>
        <td>Email webuser</td>
        <td>${chosenOrder.customer.webUser.email}</td>
    </tr>
    </c:if>
    <tr>
        <td colspan="2" align="center">
        <input type="submit" value="Update" name="btnSubmit" id="btnSubmitId" />
        </td>
    </tr>
</table>
</div>
<div class="tableDetails">

<table class="twoColorTable" style="tr:nth-child(even){background-color: #ffffff;">
    <tr>
        <th colspan="2" >Customer info</th>
    </tr>
    <tr>
        <td>NAME</td>
        <td>${chosenOrder.receiverName}</td>
    </tr>
    <tr>
        <td>ADDRESS</td>
        <td>${chosenOrder.receiverAddress}</td>
    </tr>
    <tr>
        <td>POSTCODE</td>
        <td>${chosenOrder.receiverPostNr}</td>
    </tr>
     <tr>
         <td>CITY</td>
         <td>${chosenOrder.receiverCity}</td>
     </tr>
    <tr>
        <td>COUNTRY</td>
        <td>${chosenOrder.receiverCountry.country}</td>
    </tr>
    <tr>
        <td>LANGUAGE</td>
        <td>${chosenOrder.receiverCountry.lang}</td>
    </tr>
    <tr>
        <td>EMAIL</td>
        <td>${chosenOrder.receiverEmail}</td>
    </tr>
    <tr>
        <td>PHONE NR</td>
        <td>${chosenOrder.receiverTel}</td>
    </tr>
    <tr>
        <td>VAT NR</td>
        <td>${chosenOrder.receiverBtw}</td>
    </tr>
</table>
</div>
</div>
</div>

<br/>
</form:form>

<c:if test="${chosenOrder != null}">
<div style="display: inline-block;">
    <form:form action="showInvoice" modelAttribute="chosenOrder" id="showInvoiceId" >
        <form:hidden path="id" value="${id}" />
        <input type="submit" value="Show Invoice" name="btnSubmit" id="showInvoiceSubmitId" />
    </form:form>
</div>
</c:if>
<c:if test="${chosenOrder.status == Constants.ORDER_ARCHIVED}">
<div style="display: inline-block;">
    <form:form action="deleteOrder" modelAttribute="chosenOrder" id="deleteFormId" >
        <form:hidden path="id" value="${id}" />
        <input type="submit" value="Delete" name="btnSubmit" id="deleteSubmitId" />
    </form:form>
</div>
</c:if>
<c:if test="${chosenOrder.status == Constants.ORDER_CREATED}">
<div style="display: inline-block;">
    <form:form action="resendEmail" modelAttribute="chosenOrder" id="resendEmailFormId" >
        <form:hidden path="id" value="${id}" />
        <input type="submit" value="Resend Email" name="btnSubmit" id="resendSubmitId" />
    </form:form>
</div>
</c:if>
<br/>
<br/>
<h3>items:</h3>
<table class="twoColorTable tablesorter" id="itemsTable">
    <thead>
    <tr>
        <th>ProductCode</th>
        <th>ProductName</th>
        <th>Price</th>
        <th>Unit</th>
        <th>quantity</th>
        <th>VAT</th>
        <th>subtotal</th>
    </tr>
    </thead>
    <tbody>
        <c:forEach var="orderLine" items="${chosenOrder.orderDetails}" >
         <tr>
             <td>${orderLine.product.productDetails.code}</td>
             <td>${orderLine.productName}</td>
             <td style="text-align:right;">${orderLine.price}</td>
             <td style="text-align:right;">${Constants.PRODUCT_UNITS[orderLine.unit]}</td>
             <td style="text-align:right;">${orderLine.quantity}</td>
             <td style="text-align:right;">${orderLine.totalPriceInclBtw.subtract(orderLine.totalPrice)}</td>
             <td style="text-align:right;">${orderLine.totalPriceInclBtw}</td>
         </tr>

        </c:forEach>
        <c:forEach var="orderCost" items="${chosenOrder.orderCosts}" >
         <tr>
             <td></td>
             <td>${orderCost.description}</td>
             <td style="text-align:right;">${orderCost.price}</td>
             <td style="text-align:right;">${Constants.PRODUCT_UNITS[Constants.PER_UNIT]}</td>
             <td style="text-align:right;">${orderCost.quantity}</td>
             <td style="text-align:right;">${orderCost.totalVat}</td>
             <td style="text-align:right;">${orderCost.totalPriceInclVat}</td>
         </tr>

        </c:forEach>
    </tbody>
</table>
</c:if>
<br/>
<h2>Orders</h2>
<div>
    <form id="pageSizeForm" action="showOrders">
        <button><i class="fa fa-search" ></i></button><input name="search" type=text />
        <select id="statusSelect" name="status" >
                    <option value=""></option>
                    <c:forEach var="state" items="${Constants.ORDER_STATES}">
                        <option value="${state.key}">${state.value}</option>
                    </c:forEach>
        </select>
    </form>
</div>
<br/>
<table class="twoColorTable tablesorter" id="ordersTable">
    <thead>
    <tr>
        <th>OrderId</th>
        <th>Date</th>
        <th>Name</th>
        <th>City</th>
        <th>Country</th>
        <th>items</th>
        <th>price</th>
        <th>VAT</th>
        <th>costs</th>
        <th>Total price</th>
        <th>lang</th>
        <th>status</th>
        <th>update</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="order" items="${orders}" varStatus="status">
        <tr>
            <td>${order.id}</td>
            <td><fmt:formatDate pattern="yyyy-MM-dd" value="${order.date}" /></td>
            <td>${order.receiverName}</td>
            <td>${order.receiverCity}</td>
            <td>${order.receiverCountry.country}</td>
            <td style="text-align:right;">${order.quantity}</td>
            <td style="text-align:right;">${order.totalPrice}</td>
            <td style="text-align:right;">${order.totalPriceInclBtwTransport.subtract(order.totalPriceInclTransport)}</td>
            <td style="text-align:right;">${order.totalPriceInclTransport.subtract(order.totalPrice)}</td>
            <td style="text-align:right;">${order.totalPriceInclBtwTransport}</td>
            <td style="text-align:right;">${order.receiverCountry.lang}</td>
            <td style="text-align:right;">${Constants.ORDER_STATES[order.status]}</td>
            <td style="text-align:right;"><a href="../showOrders?id=${order.id}">Details</a></td>
        </tr>
    </c:forEach>
    </tbody>
</div>

<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>
<script src="js/orderTable.js"></script>

</body>
</html>

