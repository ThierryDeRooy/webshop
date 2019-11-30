<!DOCTYPE html>
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.*"%>
<%@ page import="com.webshop.kung.entity.Category"%>

<head>
  <title>WEBSHOP</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="http://cdn.jsdelivr.net/webjars/bootstrap/4.1.0/css/bootstrap.min.css" />
  <link rel="stylesheet" type="text/css" href="css/header.css">
  <link rel="stylesheet" type="text/css" href="css/styles.css">
  <link rel="stylesheet" type="text/css" href="css/register.css">
<link rel="stylesheet" type="text/css" href="css/table.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/js/bootstrap.min.js"></script>
  <script src="js/header.js"></script>
<style>
	.error{
		color:red
	}
</style>

</head>
<body>


<%!

public void printSubCategories(JspWriter out, List<Category> categories) throws java.io.IOException {
	Iterator<Category> catIt = categories.iterator();
	while (catIt.hasNext()) {
        Category category = catIt.next();
		if (category.getSubCategories().isEmpty()) {
			out.println("<li><a href='#'>" + category.getName() + "</a></li>");
		} else {
			out.println("<li class=\"dropdown-submenu\">");
			out.println("<a class=\"dropdownsub\" tabindex=\"-1\" href=\"#\">" + category.getName() + "<span class=\"caret\"></span></a>");
			out.println("<ul class=\"dropdown-menu\">");
			printSubCategories(out, category.getSubCategories());
			out.println("</ul>");
			out.println("</li>");
		}
	}
}
%>

<nav class="navbar navbar-inverse">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">WebSiteName</a>
    </div>
    <div class="collapse navbar-collapse" id="myNavbar">
      <ul class="nav navbar-nav">
        <li class="active"><a href="home">Home</a></li>
        <li><a href="productList"><spring:message code="lang.shop" /></a></li>
        <c:if test="${pageContext.request.isUserInRole('ADMIN')}">
        <li class="dropdown">
          <a class="dropdown-toggle" data-toggle="dropdown" href="#">ADMIN<span class="caret"></span></a>
          <ul class="dropdown-menu">
              <li><a href="addNewProduct">Add Product</a></li>
              <li><a href="addNewCategory">Add Category</a></li>
              <li><a href="showOrders">Show Orders</a></li>
          </ul>
        </li>
        </c:if>
        <li><a href="contact">Contact</a></li>
        <li><a href="shoppingCart"><spring:message code="lang.shoppingbasket" /></a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle text-uppercase" data-toggle="dropdown" href="#">${pageContext.response.locale.language}
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="?language=nl">NL</a></li>
                        <li><a href="?language=en">EN</a></li>
                    </ul>
                </li>
        <c:if test="${not empty pageContext.request.userPrincipal}">
            <li><a href="/logout"><span class="glyphicon glyphicon-user"></span> Logout</a></li>
             <li><a href="#"><c:out value="${pageContext.request.userPrincipal.name}" /></a></li>
        </c:if>
        <c:if test="${empty pageContext.request.userPrincipal}">
            <li><a href="/register"><span class="glyphicon glyphicon-user"></span> Sign Up</a></li>
            <li><a href="/login"><span class="glyphicon glyphicon-log-in"></span> Login</a></li>
        </c:if>
      </ul>
    </div>
  </div>
</nav>
