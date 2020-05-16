<%@include file="templates/header.jsp"%>
<script src='https://www.google.com/recaptcha/api.js'></script>

<link rel="stylesheet" type="text/css" href="css/register.css">

<form action="#" class="signup" action="register" method="POST" enctype="utf8">
<section id="loginform" class="outer-wrapper">
			<div class="inner-wrapper">
				<div class="container">
					<div class="row">
						<div class="col-sm-4 col-sm-offset-2">
							<h2 class="text-center"><spring:message code="lang.signup" /></h2>


 <form:form action="register" modelAttribute="newUser" method="POST" enctype="utf8" >
 							    <c:if test="${param.success != null}" >
     								<div class="alert alert-success">
     								    <spring:message code="lang.signup.succes" />
          							</div>
     							</c:if>
								<form:errors path = "*" cssClass = "errorblock alert alert-danger" element = "div" />
								<div class="form-group">
									<label><spring:message code="lang.firstName" /></label>
									<form:input type="text" path="firstname" class="form-control" placeholder="enter First name" />
									<form:errors path="firstname" cssClass="error" />
								</div>
								<div class="form-group">
									<label><spring:message code="lang.familyName" /></label>
									<form:input type="text" path="lastname" class="form-control" placeholder="Enter family name"/>
									<form:errors path="lastname" cssClass="error" />
								</div>
								<div class="form-group">
									<label><spring:message code="lang.username" /></label>
									<form:input type="text" path="username" class="form-control" placeholder="Enter username"/>
									<form:errors path="username" cssClass="error" />
								</div>
								<div class="form-group">
									<label><spring:message code="lang.emailAddress" /></label>
									<form:input type="email" path="email" class="form-control" placeholder="Enter email"/>
									<form:errors path="email" cssClass="error" />
								</div>
								<div class="form-group">
									<label><spring:message code="lang.password" /></label>
									<form:password path="password" class="form-control" placeholder="Password"/>
									<form:errors path="password" cssClass="error" />
								</div>
								<div class="form-group">
									<label><spring:message code="lang.confirmPassword" /></label>
									<form:password path="confirmPassword" class="form-control" placeholder="Confirm Password"/>
									<form:errors path="confirmPassword" cssClass="error" />
								</div>

								 <div class="g-recaptcha" data-sitekey="${siteKey}">
 								 </div>
								<button type="submit" class="signupbtn signup_btn"><spring:message code="lang.signup" /></button>

    							<p class="signin"><spring:message code="lang.alreadyAccount" /> <a href="/login"><spring:message code="lang.login" /></a>.</p>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
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