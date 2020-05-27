<%@ include file="templates/header.jsp" %>
<%@ page import="java.math.BigDecimal"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="page-title"><spring:message code="customer.info" /></div>

<form:form method="POST" modelAttribute="customerInfo" action="shoppingCartCustomer">

    <table>
        <tr>
            <td><spring:message code="lang.name" /> *</td>
            <td><form:input path="name" /></td>
            <td>
                <span class="error-message"><form:errors path="name" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td><spring:message code="lang.emailAddress" /> *</td>
            <td><form:input path="email" /></td>
            <td>
                  <span class="error-message"><form:errors path="email" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td><spring:message code="lang.address" /> *</td>
            <td><form:input path="address" /></td>
            <td>
                 <span class="error-message"><form:errors path="address" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td><spring:message code="lang.postcode" /> *</td>
            <td><form:input path="postCode" /></td>
            <td>
                 <span class="error-message"><form:errors path="postCode" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td><spring:message code="lang.city" /> *</td>
            <td><form:input path="city" /></td>
            <td>
                 <span class="error-message"><form:errors path="city" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td><spring:message code="lang.country" /> *</td>
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
            <td><spring:message code="lang.phone" /></td>
            <td><form:input path="phone" /></td>
            <td>
                  <span class="error-message"><form:errors path="phone" cssClass="error" /></span>
            </td>
        </tr>
        <tr>
            <td><spring:message code="lang.btwNr" /></td>
            <td><form:input path="btwNr" /></td>
            <td>
                  <span class="error-message"><form:errors path="btwNr" cssClass="error" /></span>
            </td>
        </tr>
        <sec:authorize access="hasRole('CUSTOMER')">
        <tr>
            <td colspan="2"><spring:message code="save.data.next.time" />
            <input type="checkbox" name="saveAddress"/>
            </td>
        </tr>
        </sec:authorize>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Submit" /> <input type="reset"
                                                              value="Reset" /></td>
        </tr>
    </table>

</form:form>

<jsp:include page="templates/footer.jsp"/>
</body>
</html>