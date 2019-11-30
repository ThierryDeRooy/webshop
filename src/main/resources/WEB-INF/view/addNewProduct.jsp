<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.commons.fileupload.*"%>
<%@ page import="com.webshop.kung.constants.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
  <link rel="stylesheet" type="text/css" href="css/table.css">
<jsp:include page="templates/header.jsp"/>

<h1>Add new Product</h1>
<div class="customer-info-container">
<c:if test="${not empty generalError}" >
<p><span class="error">${generalError}</span></p>
</c:if>
<form:form action="saveProduct" modelAttribute="productInfo" enctype="multipart/form-data">
<table class="twoColorTable">
    <tr>
        <td>Category</td>
        <td>
        <form:select path="product.category.id" >
            <c:forEach var="cat" items="${categorien}">
                <form:option value="${cat.key}">${cat.value}</form:option>
            </c:forEach>
        </form:select>
        <form:errors path="product.category.id" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>language</td>
        <td>${lang}
            <form:hidden path="product.lang" value="${lang}" />
        </td>
    </tr>
    <tr>
        <td>name</td>
        <td>
            <form:input path="product.name" size="20" />
            <form:errors path="product.name" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>Product code</td>
        <td>
            <form:input path="product.productDetails.code" size="10" />
            <form:errors path="product.productDetails.code" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>description </td>
        <td>
            <form:textarea path="product.description" rows="10" cols="30" />
            <form:errors path="product.description" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>Price Unit</td>
        <td>
        <form:select path="product.productDetails.unit" >
            <c:forEach var="pu" items="${Constants.PRODUCT_UNITS}">
                <form:option value="${pu.key}">${pu.value}</form:option>
            </c:forEach>
        </form:select>
        <form:errors path="product.productDetails.unit" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>price</td>
        <td>
            <form:input path="product.productDetails.price" pattern="^\d*(\.\d{0,2})?$" size="10" />
            <form:errors path="product.productDetails.price" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>photo</td>
        <td id="productImage">
            <form:input path="file" type="file" />
            <form:errors path="file" cssClass="error" />
         </td>
    </tr>
    <tr>
        <td>status</td>
        <td>
                <form:select path="product.productDetails.status" >
                    <c:forEach var="status" items="${Constants.PRODUCT_STATES}">
                        <form:option value="${status.key}">${status.value}</form:option>
                    </c:forEach>
                </form:select>
            <form:errors path="product.productDetails.status" cssClass="error" />
         </td>
    </tr>
    <tr>
        <td colspan="2" align="center">
        <input type="submit" value="Add Product" name="btnSubmit" id="btnSubmitId" />
</td>
    </tr>
</table>

</form:form>

<br/>
<br/>
<h2>Products available in ${lang}</h2>
<table class="twoColorTable">
    <tr>
        <th>CatId</th>
        <th>Name</th>
        <th>Code</th>
        <th>PriceUnit</th>
        <th>Price</th>
        <th>description</th>
        <th>language</th>
        <th>status</th>
        <th>modify</th>
    </tr>
    <c:forEach var="prod" items="${products}">
        <tr>
            <c:if test="${pageContext.response.locale.language == prod.lang}">
            <td class="ClickCategory" value="${prod.category.id}" >${prod.category.name}</td>
            <td class="ClickName">${prod.name}</td>
            <td class="ClickCode">${prod.productDetails.code}</td>
            <td class="ClickUnit" value="${prod.productDetails.unit}" >${Constants.PRODUCT_UNITS[prod.productDetails.unit]}</td>
            <td class="ClickPrice">${prod.productDetails.price}</td>
            <td class="ClickDescription">${prod.description}</td>
            <td>${prod.lang}<hidden class="ClickPhotoLoc" value="${prod.productDetails.photoLoc}"/></td>
            <td class="ClickStatus" value="${prod.productDetails.status}" >${Constants.PRODUCT_STATES[prod.productDetails.status]}</td>
            <td><a class="Clickable" href="#" >MODIFY</a></td>
            </c:if>
            <c:if test="${pageContext.response.locale.language != prod.lang}">
            <td>${prod.category.name}</td>
            <td>${prod.name}</td>
            <td>${prod.productDetails.code}</td>
            <td>${prod.productDetails.unit}</td>
            <td>${prod.productDetails.price}</td>
            <td>${prod.description}</td>
            <td>${prod.lang}</td>
            <td>${PRODUCT_STATES[prod.productDetails.status]}</td>
            <td><a href="#" >---</a></td>
            </c:if>
        </tr>
    </c:forEach>
</table>


<h2>Products missing in ${lang}</h2>
<table class="twoColorTable">
    <tr>
        <th>CatId</th>
        <th>Name</th>
        <th>Code</th>
        <th>PriceUnit</th>
        <th>Price</th>
        <th>description</th>
        <th>language</th>
        <th>status</th>
        <th>modify</th>
    </tr>
    <c:forEach var="prod" items="${missingProducts}">
        <tr>
            <td class="ClickCategory" value="${prod.category.id}" >${prod.category.name}</td>
            <td class="ClickName">${prod.name}</td>
            <td class="ClickCode">${prod.productDetails.code}</td>
            <td class="ClickUnit" value="${prod.productDetails.unit}">${Constants.PRODUCT_UNITS[prod.productDetails.unit]}</td>
            <td class="ClickPrice">${prod.productDetails.price}</td>
            <td class="ClickDescription">${prod.description}</td>
            <td>${prod.lang}<hidden class="ClickPhotoLoc" value="${prod.productDetails.photoLoc}"/></td>
            <td class="ClickStatus" value="${prod.productDetails.status}" >${Constants.PRODUCT_STATES[prod.productDetails.status]}</td>
            <td><a class="Clickable" href="#" >ADD</a></td>
        </tr>
    </c:forEach>
    </table>
</div>

<script src="js/addProduct.js"></script>

</body>
</html>

