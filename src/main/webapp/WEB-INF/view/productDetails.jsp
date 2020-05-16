<%@ include file="templates/header.jsp" %>

<link rel="stylesheet" type="text/css" href="css/productDetails.css">
<div class="page-title">Product Details</div>

<div class="container-fluid">
    <div class="row">
    <c:if test="${cartLine != null}">
<div class="prodDetailsImg">
    <img src="<c:url value='${cartLine.product.productDetails.photoLoc}'/>" class="prodDetailsImgIn"/>
</div>
<div class="prodDetailsdesc">
    <p><spring:message code="lang.code" />: <span>${cartLine.product.productDetails.code}</span></p>
    <h1>${cartLine.product.name}</h1>
      <p class="price">${cartLine.product.productDetails.priceInclBtw} EUR</p>
      <p><spring:message code="lang.per${Constants.PRODUCT_UNITS[cartLine.product.productDetails.unit]}" /></p>
      <c:if test="${stock > 0}" >
        <p><spring:message code="lang.stock" />: ${stock}</p>
        <form:form modelAttribute="cartLine" action="orderProduct" method="post">
            <form:hidden path="product" />
            <p><spring:message code="lang.quantity" />: <form:input path="quantity" type="number" size="10" min="0" max="${stock}" />
                        <form:errors path="quantity" class="error-message" ><span class="error-message">Enter valid quantity</span></form:errors></p>
                        <form:errors path = "*" cssClass = "errorblock alert alert-danger" element = "div" />
            <p> <input type="submit" value="<spring:message code='lang.addCart' />" name="btnSubmit" /></p>
        </form:form>
       </c:if>
       <c:if test="${stock <= 0}" >
           <div><button value="<spring:message code='lang.soldout' />"><spring:message code="lang.soldout" /></button></div>
       </c:if>
        <c:if test="${not empty cartLine.product.description}">
            <textarea readonly class="detailsDescription" id="description">${cartLine.product.description}</textarea>
        </c:if>
</div>


</c:if>
    </div>
</div>

<br/>

<script src="js/productDetails.js"></script>
</body>
</html>