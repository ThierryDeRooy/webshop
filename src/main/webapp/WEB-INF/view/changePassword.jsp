<%@ include file="templates/header.jsp" %>
<link rel="stylesheet" type="text/css" href="css/register.css">


<form:form action="changePassword" class="signup" method="POST" enctype="utf8">
<section id="loginform" class="outer-wrapper">
			<div class="inner-wrapper">
				<div class="container">
					<div class="row">
						<div class="col-sm-4 col-sm-offset-2">
							<h2 class="text-center"><spring:message code="password.change" /></h2>

 							    <c:if test="${success != null}" >
     								<div class="alert alert-success">password.successfully.changed
        									<spring:message code="password.change" />
         							</div>
     							</c:if>
     						    <c:if test="${error != null}" >
                                     <div class="alert alert-danger">
                                     	<c:forEach var="errorItem" items="${error}" >
                                           <spring:message code="${errorItem}" /><br/>
                                        </c:forEach>
                                     </div>
                                </c:if>

<c:if test="${success == null}" >
 <form:form action="changePasswordNoOld" modelAttribute="newPassword" method="POST" enctype="utf8" >
                                <form:errors path = "*" cssClass = "errorblock alert alert-danger" element = "div" />
								<div class="form-group">
									<label><spring:message code="lang.old.password" /></label>
									<form:input type="password" path="oldPassword" class="form-control" placeholder="Password"/>
									<form:errors path="oldPassword" cssClass="error" />
								</div>
								<div class="form-group">
									<label><spring:message code="lang.new.password" /></label>
									<form:input type="password" path="password" class="form-control" placeholder="New Password"/>
									<form:errors path="password" cssClass="error" />
								</div>
								<div class="form-group">
									<label><spring:message code="lang.confirmPassword" /></label>
									<form:input type="password" path="confirmPassword" class="form-control" placeholder="Confirm Password"/>
									<form:errors path="confirmPassword" cssClass="error" />
								</div>
								<button type="submit" class="signupbtn signup_btn"><spring:message code="password.change" /></button>


 </form:form>

</c:if>

</div>
					</div>
				</div>
			</div>
		</section>
		</form:form>
<br/>

</body>
</html>