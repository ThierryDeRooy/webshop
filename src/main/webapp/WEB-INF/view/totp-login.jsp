<%@ include file="templates/header.jsp" %>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center login-title">Submit your verification code</h1>
            <c:if test="${not empty loginError}">
            <p class="error">Wrong user, password or security pin</p>
            </c:if>
            <div class="account-wall">
                <form:form class="form-signin" action="/login" method="post">
                <input type="password" name="totp_code" class="form-control" placeholder="Verification code" required autofocus>
                <button class="btn btn-lg btn-primary btn-block signup_btn" type="submit">Sign in</button>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>