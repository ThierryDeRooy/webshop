<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page import="java.util.*"%>
<%@ page import="com.webshop.constants.*"%>
<%@ page import="com.webshop.entity.Category"%>
<%@ page import="com.webshop.model.Cart"%>
<%@ page session="true" %>

<head>
  <title>WEBSHOP</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
<!--  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"> -->
  <link rel="stylesheet" href="css/ext/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="css/styles.css">
<link rel="stylesheet" type="text/css" href="css/table.css">
<!--<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">-->
<link rel="stylesheet" href="css/ext/font-awesome.min.css">
<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script> -->
   <script src="js/ext/jquery.min.js"></script>
<!--  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"></script> -->
  <script src="js/ext/bootstrap.min.js"></script>
  <script src="js/header.js"></script>

</head>
<body style="background: #DEFDD1;">

	<c:url value="/logout" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" />
	</form>


<nav class="navbar navbar-expand-md navbar-dark bg-yellow">
    <a class="navbar-brand" href="#"><img src="img/Kung-Thaise-Groente-rechthoek-Logo-250px.png" class="logo"/></a>
      <button type="button" class="navbar-toggler" data-toggle="collapse" data-target="#myNavbar">
        <span class="navbar-toggler-icon"></span>
      </button>
    <div class="collapse navbar-collapse" id="myNavbar">
    <div class="container-fluid">
      <ul class="navbar-nav">
        <li class="nav-item"><a class="nav-link" href="home">Home</a></li>
        <li class="nav-item"><a class="nav-link" href="productList"><spring:message code="lang.shop" /></a></li>
        <c:if test="${pageContext.request.isUserInRole('ADMIN')}">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">ADMIN</a>
          <div class="dropdown-menu">
              <a class="dropdown-item" href="showCategories">Category</a>
              <a class="dropdown-item" href="addNewProduct">Product</a>
              <a class="dropdown-item" href="showTransportCosts">TransportCost</a>
              <a class="dropdown-item" href="showCountries">Countries</a>
              <a class="dropdown-item" href="showOrders">Show Orders</a>
              <a class="dropdown-item" href="newAdmin">new Admin user</a>
          </div>
        </li>
        <li class="nav-item"><a class="nav-link" href="account">MFA</a></li>
        </c:if>
        <li class="nav-item"><a class="nav-link" href="contact">Contact</a></li>
        <c:if test="${pageContext.request.isUserInRole('CUSTOMER')}">
            <li class="nav-item"><a class="nav-link" href="showOrderList"><spring:message code="lang.myOrders" /></a></li>
        </c:if>
        <li class="nav-item"><a class="nav-link" href="shoppingCart"><i class="fa shoppingcart">&#xf07a;</i>
               <span>
            <c:if test="${not empty sessionScope.myOrders}">
                ${sessionScope.myOrders.totalPriceInclBtw} eur
            </c:if>
            <c:if test="${empty sessionScope.myOrders}">
                0 eur
            </c:if>
            </span>
        </a></li>
      </ul>
            <ul class="nav navbar-nav navbar-right">
                      <li class="nav-item dropdown">
                          <a class="nav-link dropdown-toggle text-uppercase" data-toggle="dropdown" href="#">${pageContext.response.locale.language}
                              <span class="caret"></span></a>
                          <div class="dropdown-menu">
                            <c:forEach var="language" items="${Constants.LANGUAGES}">
                              <a class="dropdown-item" href="?language=${language}">${language}</a>
                             </c:forEach>
                          </div>
                      </li>
              <c:if test="${not empty pageContext.request.userPrincipal}">
                      <li class="nav-item dropdown">
                          <a class="nav-link dropdown-toggle" data-toggle="dropdown" href="#">${pageContext.request.userPrincipal.name}
                              <span class="caret"></span></a>
                          <div class="dropdown-menu dropdown-menu-right">
                              <a class="dropdown-item" href="/changePassword">Change Password</a>
<!--                              <a class="dropdown-item" href="<c:url value="/logout" />">Logout</a>-->
                              <a class="dropdown-item" id="logoutID">Logout</a>
                          </div>
                      </li>
              </c:if>
              <c:if test="${empty pageContext.request.userPrincipal}">
                  <li class="nav-item"><a class="nav-link" href="/register"><span class="glyphicon glyphicon-user"></span><spring:message code="lang.signup" /></a></li>
                  <li class="nav-item"><a class="nav-link" href="/login"><span class="glyphicon glyphicon-log-in"></span><spring:message code="lang.login" /></a></li>
              </c:if>
            </ul>

    </div>
 </div>
</nav>

