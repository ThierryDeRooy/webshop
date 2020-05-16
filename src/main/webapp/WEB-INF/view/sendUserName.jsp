<%@include file="templates/header.jsp"%>


<form action="sendUsername" style="border:1px solid #ccc" method="POST" enctype="utf8">
<section id="loginform" class="outer-wrapper">
			<div class="inner-wrapper">
				<div class="container">
					<div class="row">
						<div class="col-sm-4 col-sm-offset-2">
							<h2 class="text-center"><spring:message code="password.reset" /></h2>


 <form:form action="sendUsername" method="POST" enctype="utf8" >
 							    <c:if test="${email != null}" >
     								<div class="alert alert-warning">
     								        <spring:message code="email.not.found" />
         							</div>
     							</c:if>
 							    <c:if test="${emailNotSent != null}" >
     								<div class="alert alert-warning">
     								        <spring:message code="email.not.sent" />
         							</div>
     							</c:if>
 							    <c:if test="${success != null}" >
     								<div class="alert alert-success">
     								       <spring:message code="username.sent.successfully" />
         							</div>
     							</c:if>
								<div class="form-group">
									<label>Email</label>
									<input type="email" name="myEmail" class="form-control" placeholder="enter your email" />
								</div>
								<button type="submit" class="signupbtn signup_btn"><spring:message code="send.username" /></button>
    							<p class="signin"><spring:message code="lang.alreadyAccount" /> <a href="/login"><spring:message code="lang.login" /></a>.</p>


 </form:form>



</div>