<%@include file="templates/header.jsp"%>


<div class="container">
    <div class="row justify-content-center">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center login-title"><spring:message code="lang.login" /></h1>
            <c:if test="${loginError == true}" >
                <div class="alert alert-danger"><spring:message code="wrong.username.password" /></div>
            </c:if>
            <c:if test="${param.logout != null}" >
                <div class="alert alert-success"><spring:message code="lang.logout" /></div>
            </c:if>
             <div class="account-wall">
                 <form class="form-signin" action="login" method="post">
                 <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                 <input type="text" name = "username" class="form-control" placeholder="<spring:message code='lang.username' />" required autofocus>
                 <input type="password" name = "password" class="form-control" placeholder="<spring:message code='lang.password' />" required>
                 <button class="btn btn-lg btn-primary btn-block signup_btn" type="submit">
                     <spring:message code="lang.login" /></button>
                  <label class="checkbox pull-left">
                      <input type="checkbox" name="remember-me">
                      <spring:message code="lang.rememberMe" />
                  </label>
                 </form>
             </div>
             <br/><br/>
             <a href="/register" class="text-center new-account"><spring:message code="lang.createAccount" /></a>
             <br/>
             <a href="/pwReset" class="text-center new-account"><spring:message code="lang.forgotPassword" /> </a>
             <br/>
             <a href="/sendUsername" class="text-center new-account"><spring:message code="forget.username" /> </a>

        </div>
    </div>
</div>

</body>
</html>

