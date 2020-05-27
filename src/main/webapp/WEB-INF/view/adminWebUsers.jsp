<%@ include file="templates/header.jsp" %>

<link rel="stylesheet" type="text/css" href="css/register.css">


<section id="loginform" class="outer-wrapper">
			<div class="inner-wrapper">
				<div class="container">
					<div class="row">
						<div class="col-sm-4 col-sm-offset-2">
							<h2 class="text-center"><spring:message code="lang.signup" /></h2>


 <form:form action="newAdmin" modelAttribute="newUser" method="POST" enctype="utf8" >
 							    <c:if test="${param.adminCreated != null}" >
     								<div class="alert alert-success">
     								    The new Admin user '${param.adminCreated}' has been created and a mail is sent for confirmation and password setting.
          							</div>
     							</c:if>
								<form:errors path = "*" cssClass = "errorblock alert alert-danger" element = "div" />
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
									<label>ROLE</label>
									<form:select path="role" class="form-control" >
									    <form:option value="ADMIN" label="ADMIN" />
									</form:select>
								</div>
								<button type="submit" class="signupbtn signup_btn">CREATE ADMIN</button>

 </form:form>



</div>
					</div>
				</div>
			</div>
		</section>

<br/>


<h2>all webusers</h2>
<table class="twoColorTable tablesorter" >
    <thead>
    <tr>
        <th>UserName</th>
        <th>Email</th>
        <th>Role</th>
        <th>Multifactor Authentication</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="webuser" items="${webUsers}">
        <tr>
            <td>${webuser.username}</td>
            <td>${webuser.email}</td>
            <td>${webuser.role}</td>
            <td>${webuser.totpEnabled}</td>
            <td><form:form action="removeWebUser" modelAttribute="webUser" method="POST">
                    <form:hidden path="username" value="${webuser.username}"/>
                    <button type="submit">REMOVE</button>
                </form:form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>





<link rel="stylesheet" href="css/ext/theme.default.css">
 <script src="js/ext/jquery.tablesorter.min.js"></script>


<jsp:include page="templates/footer.jsp"/>

</body>
</html>