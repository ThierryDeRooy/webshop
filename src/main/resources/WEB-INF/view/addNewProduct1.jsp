<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="templates/header.jsp"/>

<h1>Add new Product</h1>
<div class="customer-info-container">
<form:form action="saveProduct" modelAttribute="product" enctype="multipart/form-data">
<table>
    <tr>
        <td>Category</td>
        <td>
        <form:select path="category.id" >
            <c:forEach var="cat" items="${categorien}">
                <form:option value="${cat.key}">${cat.value}</form:option>
            </c:forEach>
        </form:select>
        <form:errors path="category.id" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>language</td>
        <td>${lang}
            <form:hidden path="lang" value="${lang}" />
        </td>
    </tr>
    <tr>
        <td>name</td>
        <td>
            <form:input path="name" size="20" />
            <form:errors path="name" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>Product code</td>
        <td>
            <form:input path="productDetails.code" size="10" />
            <form:errors path="productDetails.code" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>description </td>
        <td>
            <form:textarea path="description" rows="10" cols="30" />
            <form:errors path="description" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>Price Unit</td>
        <td>
        <form:select path="productDetails.Unit" >
            <c:forEach var="pu" items="${PriceUnits}">
                <form:option value="${pu}">${pu}</form:option>
            </c:forEach>
        </form:select>
        <form:errors path="productDetails.Unit" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>price</td>
        <td>
            <form:input path="productDetails.price" size="10" />
            <form:errors path="productDetails.price" cssClass="error" />
        </td>
    </tr>
    <tr>
        <td>photo</td>
        <td>
            <input type="file" name="file" />
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
<table>
    <tr>
        <th>CatId</th>
        <th>Name</th>
        <th>Code</th>
        <th>PriceUnit</th>
        <th>Price</th>
        <th>description</th>
        <th>language</th>
        <th>modify</th>
    </tr>
    <c:forEach var="prod" items="${products}">
        <tr>
            <c:if test="${pageContext.response.locale.language == prod.lang}">
            <td class="ClickCategory" value="${prod.category.id}" >${prod.category.name}</td>
            <td class="ClickName">${prod.name}</td>
            <td class="ClickCode">${prod.productDetails.code}</td>
            <td class="ClickUnit">${prod.productDetails.unit}</td>
            <td class="ClickPrice">${prod.productDetails.price}</td>
            <td class="ClickDescription">${prod.description}</td>
            <td>${prod.lang}</td>
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
            <td><a href="#" >---</a></td>
            </c:if>
        </tr>
    </c:forEach>
</table>


<h2>Products missing in ${lang}</h2>
<table>
    <tr>
        <th>CatId</th>
        <th>Name</th>
        <th>Code</th>
        <th>PriceUnit</th>
        <th>Price</th>
        <th>description</th>
        <th>language</th>
        <th>modify</th>
    </tr>
    <c:forEach var="prod" items="${missingProducts}">
        <tr>
            <td class="ClickCategory" value="${prod.category.id}" >${prod.category.name}</td>
            <td class="ClickName">${prod.name}</td>
            <td class="ClickCode">${prod.productDetails.code}</td>
            <td class="ClickUnit">${prod.productDetails.unit}</td>
            <td class="ClickPrice">${prod.productDetails.price}</td>
            <td class="ClickDescription">${prod.description}</td>
            <td>${prod.lang}</td>
            <td><a class="Clickable" href="#" >ADD</a></td>
        </tr>
    </c:forEach>
    </table>
</div>

<script>
    var links = document.getElementsByClassName("Clickable");
    var prodCodes = document.getElementsByClassName("ClickCode");
    var prices = document.getElementsByClassName("ClickPrice");
    var units = document.getElementsByClassName("ClickUnit");
    var names = document.getElementsByClassName("ClickName");
    var descriptions = document.getElementsByClassName("ClickDescription");
    var categories = document.getElementsByClassName("ClickCategory");
    // For each class "Clickable" inside document
    for (var i = 0; i < links.length; i++) {
      var link = links[i];
      link.id = "modify-"+i;
      prodCodes[i].id = "prodCodeID-"+i;
      prices[i].id = "priceID"+i;
      units[i].id = "unitID"+i;
      names[i].id = "nameID"+i;
      descriptions[i].id = "descriptionID"+i;
      categories[i].id = "categoryID"+i;
//      link.innerHTML = "MODIFY";
      link.addEventListener("click", modifyProduct);
      //link.onclick = modifyProduct(i);
    }

    function modifyProduct(){
        var id = this.id;
        var ctr = id.split("-")[1];
        document.getElementById('productDetails.code').value = document.getElementById("prodCodeID-"+ctr).innerHTML;
        document.getElementById('productDetails.price').value = document.getElementById("priceID"+ctr).innerHTML;
        document.getElementById('productDetails.Unit').value = document.getElementById("unitID"+ctr).innerHTML;
        document.getElementById('name').value = document.getElementById("nameID"+ctr).innerHTML;
        document.getElementById('description').value = document.getElementById("descriptionID"+ctr).innerHTML;
        document.getElementById('category.id').value = document.getElementById("categoryID"+ctr).getAttribute("value");
        document.getElementById('btnSubmitId').value = document.getElementById("modify-"+ctr).innerHTML;
//        selectElement('category.id', document.getElementById("categoryID"+ctr).getAttribute("value"));
    }

</script>

</body>
</html>

