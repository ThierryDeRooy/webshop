<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="templates/header.jsp"/>


<div class="page-title">Enter Customer Information</div>

<form:form method="POST" modelAttribute="customerInfo" action="shoppingCartCustomer">

    <table>
        <tr>
            <td>Name *</td>
            <td><form:input path="name" /></td>
            <td>
                <span class="error-message"><form:errors path="name" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td>Email *</td>
            <td><form:input path="email" /></td>
            <td>
                  <span class="error-message"><form:errors path="email" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td>Address *</td>
            <td><form:input path="address" /></td>
            <td>
                 <span class="error-message"><form:errors path="address" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td>post code *</td>
            <td><form:input path="postCode" /></td>
            <td>
                 <span class="error-message"><form:errors path="postCode" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td>city *</td>
            <td><form:input path="city" /></td>
            <td>
                 <span class="error-message"><form:errors path="city" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td>country *</td>
            <td>
                    <form:select path="country" >
                        <c:forEach var="land" items="${countries}">
                            <form:option value="${land}">${land.country}</form:option>
                        </c:forEach>
                    </form:select>
            </td>
            <td>
                 <span class="error-message"><form:errors path="country" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td>Phone</td>
            <td><form:input path="phone" /></td>
            <td>
                  <span class="error-message"><form:errors path="phone" cssClass="error" /></span>
            </td>
        </tr>
        <c:if test="${not empty customerInfo.email}">
        <tr>
            <td colspan="2">Save address data for next time
            <input type="checkbox" name="saveAddress"/>
            </td>
        </tr>
        </c:if>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Submit" /> <input type="reset"
                                                              value="Reset" /></td>
        </tr>
    </table>

</form:form>

</body>
</html>