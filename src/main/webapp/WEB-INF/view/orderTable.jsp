<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.webshop.kung.constants.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="templates/header.jsp"/>

<c:if test="${not empty chosenOrder}">
<br/>
<h2>Order details</h2>
<form:form action="updateOrder" modelAttribute="chosenOrder" >

<table class="twoColorTable">
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
        <td>Total items</td>
        <td>${chosenOrder.quantity}</td>
    </tr>
    <tr>
        <td>TOTAL PRICE</td>
        <td>${chosenOrder.totalPrice}</td>
    </tr>
    <tr>
        <td colspan="2" align="center">
        <input type="submit" value="Update" name="btnSubmit" id="btnSubmitId" />
        </td>
    </tr>
</table>

<br/>
</form:form>

<h3>items:</h3>
<table class="twoColorTable">
    <tr>
        <th>ProductCode</th>
        <th>ProductName</th>
        <th>Price</th>
        <th>Unit</th>
        <th>quantity</th>
        <th>subtotal</th>
    </tr>
<c:forEach var="orderLine" items="${chosenOrder.orderDetails}" >
         <tr>
             <td>${orderLine.product.productDetails.code}</td>
             <td>${orderLine.productName}</td>
             <td>${orderLine.price}</td>
             <td>${Constants.PRODUCT_UNITS[orderLine.unit]}</td>
             <td>${orderLine.quantity}</td>
             <td>${orderLine.totalPrice}</td>
         </tr>

</c:forEach>
</table>
</c:if>
<br/>
<h2>Orders</h2>

<table class="twoColorTable">
    <tr>
        <th>OrderId</th>
        <th>Date</th>
        <th>Name</th>
        <th>City</th>
        <th>Country</th>
        <th>items</th>
        <th>price</th>
        <th>language</th>
        <th>status</th>
        <th>update</th>
    </tr>
    <c:forEach var="order" items="${orders}" varStatus="status">
        <tr>
            <td>${order.id}</td>
            <td><fmt:formatDate pattern="dd-MM-yyyy" value="${order.date}" /></td>
            <td>${order.receiverName}</td>
            <td>${order.receiverCity}</td>
            <td>${order.receiverCountry.country}</td>
            <td>${order.quantity}</td>
            <td>${order.totalPrice}</td>
            <td>${order.receiverCountry.lang}</td>
            <td>${Constants.ORDER_STATES[order.status]}</td>
            <td><a href="?id=${order.id}">Details</a></td>
        </tr>
    </c:forEach>

</div>


</body>
</html>

