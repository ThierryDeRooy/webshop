<%@ include file="templates/header.jsp" %>
<%@ page import="org.apache.commons.fileupload.*"%>

<h1>Add new Product</h1>
<div class="customer-info-container">
<c:if test="${not empty generalError}" >
<p><span class="error">${generalError}</span></p>
</c:if>
<form:form action="saveProduct" modelAttribute="productInfo" enctype="multipart/form-data">

<link rel="stylesheet" type="text/css" href="css/newProduct.css">


<div class="container-fluid">
    <div class="row">
    <div class="tableDetails">
<table class="twoColorTable">
    <tr>
        <td>language</td>
        <td><h2>${lang}</h2>
            <form:hidden path="product.lang" value="${lang}" />
        </td>
    </tr>
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
        <td>name</td>
        <td>
            <form:input path="product.name" size="20" />
            <form:errors path="product.name" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>description </td>
        <td>
            <form:textarea path="product.description" rows="10" cols="30" />
            <form:errors path="product.description" cssClass="error" />
        </td>
    </tr>
</table>
</div>
    <div class="tableDetails">

<table class="twoColorTable">
    <tr>
        <td>Product code</td>
        <td>
            <form:input path="product.productDetails.code" size="10" />
            <form:errors path="product.productDetails.code" cssClass="error" />
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
        <td>VAT(BTW)</td>
        <td>
            <form:input path="product.productDetails.btw" pattern="^\d*(\.\d{0,2})?$" size="10" />
            <form:errors path="product.productDetails.btw" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>Transport cost (points)</td>
        <td>
            <form:input path="product.productDetails.transportPoints" pattern="^\d*(\.\d{0,2})?$" size="10" />
            <form:errors path="product.productDetails.transportPoints" cssClass="error" />
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
        <td>baskets</td>
        <td><input id="baskets" disabled="true" /><button form="basketResetFromId">RESET BASKETS</button>
         </td>
    </tr>
    <tr>
        <td>stock</td>
        <td>
            <form:input path="product.productDetails.stock" readonly="true" type="number" size="10" />
            <form:errors path="product.productDetails.stock" cssClass="error" />
            <span id="addStockProduct"></span>
            ADD: <input id="updateStock" name="updateStock" type="number" size="5" value="0"/>
         </td>
    </tr>
    <tr>
        <td colspan="2" align="center">
        <input type="submit" value="Add Product" name="btnSubmit" id="btnSubmitId" />
        </td>
    </tr>
</table>
<form:hidden path="product.productDetails.id" />
</div>
</div>
</form:form>
<form id="basketResetFromId" action="removeProductFromBaskets" method="POST" >
    <input type="hidden" name="prodDetailsId" id="prodDetailsId" />
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

</form>
</div>


<br/>
<br/>
<h2>Products available in ${lang}</h2>
<table class="twoColorTable tablesorter" id="productTable">
    <thead>
    <tr>
        <th>CatId</th>
        <th>Name</th>
        <th>Code</th>
        <th>PriceUnit</th>
        <th>Price</th>
        <th>VAT(%)</th>
        <th>Transport(pts)</th>
        <th>language</th>
        <th>stock</th>
        <th>baskets</th>
        <th>modify</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="prod" items="${products}">
        <tr>
            <c:if test="${pageContext.response.locale.language == prod.lang}">
            <td class="ClickCategory" value="${prod.category.id}" >${prod.category.name}</td>
            <td class="ClickName">${prod.name}</td>
            <td class="ClickCode">${prod.productDetails.code}</td>
            <td class="ClickUnit" value="${prod.productDetails.unit}" >${Constants.PRODUCT_UNITS[prod.productDetails.unit]}</td>
            <td class="ClickPrice">${prod.productDetails.price}</td>
            <td class="ClickBtw">${prod.productDetails.btw}</td>
            <td class="ClickTransportPoints">${prod.productDetails.transportPoints}</td>
            <td>${prod.lang}</td>
            <td class="ClickStock" value="${prod.productDetails.stock}" >${prod.productDetails.stock}</td>
            <td class="ClickReserved" value="${sessionsStock[prod.productDetails.id]}" >${sessionsStock[prod.productDetails.id]}</td>
            <td><a class="Clickable" href="#" >MODIFY</a>
                <hidden class="ClickDescription" value="${prod.description}"/>
                <hidden class="ClickProdDetails" value="${prod.productDetails.id}"/>
                <hidden class="ClickPhotoLoc" value="${prod.productDetails.photoLoc}"/>
            </td>
            </c:if>
        </tr>
    </c:forEach>
    </tbody>
</table>


<h2>Products missing in ${lang}</h2>
<table class="twoColorTable tablesorter" id="missingProductsTable">
    <thead>
    <tr>
        <th>CatId</th>
        <th>Name</th>
        <th>Code</th>
        <th>PriceUnit</th>
        <th>Price</th>
        <th>VAT(%)</th>
        <th>Transport(pts)</th>
        <th>language</th>
        <th>stock</th>
        <th>baskets</th>
        <th>modify</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="prod" items="${missingProducts}">
        <tr>
            <td class="ClickCategory" value="${langCat[prod.category.code]}" >${prod.category.name}</td>
            <td class="ClickName">${prod.name}</td>
            <td class="ClickCode">${prod.productDetails.code}</td>
            <td class="ClickUnit" value="${prod.productDetails.unit}">${Constants.PRODUCT_UNITS[prod.productDetails.unit]}</td>
            <td class="ClickPrice">${prod.productDetails.price}</td>
            <td class="ClickBtw">${prod.productDetails.btw}</td>
            <td class="ClickTransportPoints">${prod.productDetails.transportPoints}</td>
            <td>${prod.lang}</td>
            <td class="ClickStock" value="${prod.productDetails.stock}" >${prod.productDetails.stock}</td>
            <td class="ClickReserved" value="${sessionsStock[prod.productDetails.id]}" >${sessionsStock[prod.productDetails.id]}</td>
            <td><a class="Clickable" href="#" >ADD</a>
                <hidden class="ClickDescription" value="${prod.description}"/>
                <hidden class="ClickProdDetails" value="${prod.productDetails.id}"/>
                <hidden class="ClickPhotoLoc" value="${prod.productDetails.photoLoc}"/>
            </td>
        </tr>
    </c:forEach>
    </tbody>
    </table>
</div>


<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>
<script src="js/addProduct.js"></script>

<jsp:include page="templates/footer.jsp"/>

</body>
</html>

