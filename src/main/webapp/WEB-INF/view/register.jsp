<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.*"%>
<%@ page import="com.webshop.kung.constants.*"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<%@include file="templates/header.jsp"%>


<form action="#" style="border:1px solid #ccc" action="register" method="POST" enctype="utf8">
<section id="loginform" class="outer-wrapper">
			<div class="inner-wrapper">
				<div class="container">
					<div class="row">
						<div class="col-sm-4 col-sm-offset-2">
							<h2 class="text-center">Register</h2>


 <form:form action="register" modelAttribute="newUser" method="POST" enctype="utf8" >
 							    <c:if test="${param.success != null}" >
     								<div class="alert alert-success">
        									Registration complete! , you can now <a href="/login" href="/login">login</a>
         							</div>
     							</c:if>
								<form:errors path = "*" cssClass = "errorblock" element = "div" />
								<div class="form-group">
									<label>First name</label>
									<form:input type="text" path="firstname" class="form-control" placeholder="enter First name" />
									<form:errors path="firstname" cssClass="error" />
								</div>
								<div class="form-group">
									<label>Family name</label>
									<form:input type="text" path="lastname" class="form-control" placeholder="Enter family name"/>
									<form:errors path="lastname" cssClass="error" />
								</div>
								<div class="form-group">
									<label>Username</label>
									<form:input type="text" path="username" class="form-control" placeholder="Enter username"/>
									<form:errors path="username" cssClass="error" />
								</div>
								<div class="form-group">
									<label>Email address</label>
									<form:input type="email" path="email" class="form-control" placeholder="Enter email"/>
									<form:errors path="email" cssClass="error" />
								</div>
								<div class="form-group">
									<label>Password</label>
									<form:password path="password" class="form-control" placeholder="Password"/>
									<form:errors path="password" cssClass="error" />
								</div>
								<div class="form-group">
									<label>Confirm Password</label>
									<form:password path="confirmPassword" class="form-control" placeholder="Confirm Password"/>
									<form:errors path="password" cssClass="error" />
								</div>
								<button type="submit" class="signupbtn signup_btn">Sign Up</button>
    							<p class="signin">Already have an account? <a href="/login">Sign in</a>.</p>


 </form:form>



</div>
					</div>
				</div>
			</div>
		</section>
		</form>
<br/>

</body>
</html>