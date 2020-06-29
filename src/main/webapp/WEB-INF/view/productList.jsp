<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="templates/header.jsp" %>

<!--   <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script> -->
 <script src="js/ext/popper.min.js"></script>


<link rel="stylesheet" type="text/css" href="css/productList.css">



<%!

public void printSubCategories(JspWriter out, List<Category> categories) throws java.io.IOException {
	Iterator<Category> catIt = categories.iterator();
	while (catIt.hasNext()) {
                Category category = catIt.next();
		if (category.getSubCategories().isEmpty()) {
			out.println("<li><a href='?category="+ category.getName() +"'>" + category.getName() + "</a></li>");
		} else {
			out.println("<li class=\"dropdown-submenu\">");
			out.println("<a class=\"dropdownsub\" href=\"?category="+ category.getName() +"\">" + category.getName() + "<span class=\"caret\"></span></a>");
			out.println("<ul class=\"dropdown-menu\">");
			printSubCategories(out, category.getSubCategories());
			out.println("</ul>");
			out.println("</li>");
		}
	}
}
public void printSubCategoriesList(JspWriter out, List<Category> categories) throws java.io.IOException {
	Iterator<Category> catIt = categories.iterator();
	while (catIt.hasNext()) {
                Category category = catIt.next();
		if (category.getSubCategories().isEmpty()) {
			out.println("<li><a href='?category="+ category.getName() +"'>" + category.getName() + "</a></li>");
		} else {
			out.println("<li>");
			out.println("<a href=\"?category="+ category.getName() +"\">" + category.getName()+"</a>");
			out.println("<ul class=\"cat-list\">");
			printSubCategoriesList(out, category.getSubCategories());
			out.println("</ul>");
			out.println("</li>");
		}
	}
}
%>
<div class="row">
    <div class="col-sm-9">
<div class="breadcrumbPlace" >
<nav>
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="?category=all">ALL</a></li>
        <c:forEach var="cat" items="${cats}">
            <li class="breadcrumb-item"><a href="?category=${cat.name}">${cat.name}</a></li>
        </c:forEach>
        <li class="breadcrumb-item">
            <div class="dropdown-menu" aria-labelledby="dropdownmenu">
                 <c:forEach var="cat" items="${sessionScope.selectedCategory.subCategories}">
                    <a class="dropdown-item" href="?category=${cat.name}">${cat.name}</a>
                  </c:forEach>
            </div>
        </li>
        <li>
            <form id="searchForm" action="productList">
                <button><i class="fa fa-search" ></i></button><input name="search" type=text />
            </form>
        </li>
    </ol>
</nav>
</div>

</div>
<div class="col-sm-3">
    <br/>
    <div class="sort"><spring:message code="lang.price" />
        <button id="PriceSortUp"><div class="arrow-up"></div></button><button id="PriceSortDown"><div class="arrow-down"></div></button>
    </div>
    <div class="sort"><spring:message code="lang.productName" />
        <button id="NameSortUp"><div class="arrow-up"></div></button><button id="NameSortDown"><div class="arrow-down"></div></button>
    </div>

    <br/>

</div>

</div>

<div class="row">
    <div class="col-sm-3 col-lg-2">


        <div class="catDiv" style="">
            <ul class="cat-list" style="width: 100%;">
                <li><a href="?category=all">* CATEGORIES *</a></li>
                <%printSubCategoriesList(out, (List<Category>) request.getAttribute("categories"));%>
            </ul>
        </div>

        <div class="pageCtl">
            <div >
            <form id="pageSizeForm" action="productList">
                <p><spring:message code="lang.pageSize" />
                    <select id="pageSizeSelect" name="pageSize" >
                        <option value="${pageSizeMax}" selected="selected">${pageSizeMax}</option>
                        <option value="10">10</option>
                        <option value="25">25</option>
                        <option value="50">50</option>
                        <option value="100">100</option>
                    </select>
                </p>
            </form>
            </div>
            <div  style="text-align: right;padding: 5px;"><spring:message code="lang.price" />
                <form action="productList" style="display: inline-block;">
                    <input type="hidden" name="sortBy" value="productDetails.price" />
                    <input type="hidden" name="direction" value="ASC" />
                    <button><i class="fa fa-sort-amount-asc" ></i></button>
                </form>
                <form action="productList" style="display: inline-block;">
                    <input type="hidden" name="sortBy" value="productDetails.price" />
                    <input type="hidden" name="direction" value="DESC" />
                    <button><i class="fa fa-sort-amount-desc" ></i></button>
                </form>
            </div>
            <div  style="text-align: right;padding: 5px;"><spring:message code="lang.productName" />
                <form action="productList" style="display: inline-block;">
                    <input type="hidden" name="sortBy" value="name" />
                    <input type="hidden" name="direction" value="ASC" />
                    <button><i class="fa fa-sort-alpha-asc" ></i></button>
                </form>
                <form action="productList" style="display: inline-block;">
                    <input type="hidden" name="sortBy" value="name" />
                    <input type="hidden" name="direction" value="DESC" />
                    <button><i class="fa fa-sort-alpha-desc" ></i></button>
                </form>
            </div>

        </div>

    </div>
    <div class="col-sm-9 col-lg-10">
<div class="weergave">
    <a href="productList?weergave=grid"><i class="fa fa-th-large"></i></a><span> - </span>
    <a href="productList?weergave=lijst"><i class="fa fa-bars"></i></a>
 </div>

<div class="paging">
<div style="display: inline-block;">
 <c:if test="${pageNr > 0}" >
    <form action="productList">
        <input type="hidden" name="pageNo" value="${pageNr - 1}" />
        <button class="pageButton"> << </button>
    </form>
 </c:if>
 </div>
 &nbsp;&nbsp;
<div style="display: inline-block;">
    <form id="pageNrForm" action="productList">
        <select id="pageNr" name="pageNo" style="background: transparent;">
        <c:forEach var="i" begin="1" end="${totalPages}">
            <c:if test="${pageNr+1 == i}" >
                <option value="${i-1}" selected="selected">${i}</option>
            </c:if>
            <c:if test="${pageNr+1 != i}" >
                <option value="${i-1}">${i}</option>
            </c:if>
        </c:forEach>
        </select>
    </form>
 </div>
 &nbsp;&nbsp;
<div style="display: inline-block; padding=50px;">
 <c:if test="${pageNr < totalPages-1}" >
    <form action="productList">
        <input type="hidden" name="pageNo" value="${pageNr + 1}" />
        <button class="pageButton"> >> </button>
    </form>
 </c:if>
 </div>
 &nbsp;&nbsp;
 <div style="display: inline-block;">
        ${pageNr*pageSizeMax+1}-${pageNr*pageSizeMax+elemsOnPage} | ${totalElems}
 </div>
</div>

<br/>
<br/>
<div class="prodList">
<c:if test="${sessionScope.weergave == 'grid'}" >
   <c:forEach var="product" items="${products}">
    <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] <= 0}">
<div class="card sortItem" style="opacity: 0.6; filter: alpha(opacity = 60);">
    </c:if>
    <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] > 0}">
<div class="card sortItem" >
    </c:if>
  <img class="cardImg" src="<c:url value='${imageFolder}${product.productDetails.code}/${product.productDetails.photoLoc}'/>" alt="${product.productDetails.code}" />
  <div class="cardProdNameOuterbox">
    <div class="cardProdNameInnerbox">
        <h1 class="sortName">${product.name}</h1>
    </div>
  </div>
  <h3>--${product.category.name}--</h3><br/>
  <p><span class="price">${product.productDetails.priceInclBtw} EUR</span>
  <br/><spring:message code="lang.per${Constants.PRODUCT_UNITS[product.productDetails.unit]}" /></p>
    <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] <= 0}">
  <p><a href="#" class="buttonCard btn btn-info" role="button"><spring:message code="lang.soldout" /></a></p>
    </c:if>
    <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] > 0}">
  <p><a href="buyProduct?code=${product.productDetails.code}" class="buttonCard btn btn-info" role="button"><spring:message code="lang.addCart" /></a></p>
    </c:if>

</div>
 </c:forEach>
</c:if>

<c:if test="${sessionScope.weergave == 'lijst'}" >
   <c:forEach var="product" items="${products}">
    <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] <= 0}">
        <div class="cardList sortItem" style="opacity: 0.6; filter: alpha(opacity = 60);">
    </c:if>
    <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] > 0}">
        <div class="cardList sortItem">
    </c:if>
        <div class="cardListImg" >
        <img class="responsive cardListImgIn" src="<c:url value='${imageFolder}${product.productDetails.code}/${product.productDetails.photoLoc}'/>" alt="${product.productDetails.code}" />
        </div>
        <div class="cardListName" >
          <p><span class="listName sortName">${product.name}</span><br/>
          <span>--${product.category.name}--</span></p>
        </div>
        <div class="cardListPrice">
          <p><span class="price">${product.productDetails.priceInclBtw}</span> EUR
          <br/><span><spring:message code="lang.per${Constants.PRODUCT_UNITS[product.productDetails.unit]}" />   </span></p>
        </div>
        <div class="cardListButton">
            <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] <= 0}">
                <a href="#" class="buttonCardList btn btn-info" role="button"><spring:message code="lang.soldout" /></a>
            </c:if>
            <c:if test="${product.productDetails.stock - sessionsStock[product.productDetails.id] > 0}">
                <a href="buyProduct?code=${product.productDetails.code}" class="buttonCardList btn btn-info" role="button"><spring:message code="lang.addCart" /></a>
            </c:if>

        </diV>
    </div>
    </c:forEach>
</c:if>

</div>


<div class="paging">
<div style="display: inline-block;">
 <c:if test="${pageNr > 0}" >
    <form action="productList">
        <input type="hidden" name="pageNo" value="${pageNr - 1}" />
        <button class="pageButton"> << </button>
    </form>
 </c:if>
 </div>
 &nbsp;&nbsp;
<div style="display: inline-block;">
    <form id="pageNrFormDown" action="productList">
        <select id="pageNrDown" name="pageNo" style="background: transparent;">
        <c:forEach var="i" begin="1" end="${totalPages}">
            <c:if test="${pageNr+1 == i}" >
                <option value="${i-1}" selected="selected">${i}</option>
            </c:if>
            <c:if test="${pageNr+1 != i}" >
                <option value="${i-1}">${i}</option>
            </c:if>
        </c:forEach>
        </select>
    </form>
 </div>
 &nbsp;&nbsp;
<div style="display: inline-block;">
 <c:if test="${pageNr < totalPages-1}" >
    <form action="productList">
        <input type="hidden" name="pageNo" value="${pageNr + 1}" />
        <button class="pageButton"> >> </button>
    </form>
 </c:if>
 </div>
 &nbsp;&nbsp;
 <div style="display: inline-block;">
    ${pageNr*pageSizeMax+1}-${pageNr*pageSizeMax+elemsOnPage} | ${totalElems}
 </div>
</div>



</div>
</div>


<script src="js/productList.js"></script>
<jsp:include page="templates/footer.jsp"/>

</body>
</html>