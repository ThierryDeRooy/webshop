<%@ include file="templates/header.jsp" %>

	<div class="container">
		<h3>Account</h3>
		<form action="confirm-totp" method="POST" enctype="utf8">
		<table class="table table-borderless">
		    <c:if test="${qrUrl != null}">
			<tbody>
				<tr>
					<td>Use your Google Authenticator App, to scan the QR Code<br>
						<img alt="QR Code" src="${qrUrl}">
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td>
					<c:if test="${not empty confirmError}">
						<p class="error">Wrong code, try again</p>
					</c:if>
						<input type="text" name="code" placeholder="Verification Code"/><br>
						<button type="submit" class="signupbtn signup_btn">Enable 2fa</button>
					</td>
					<td>

					</td>
				</tr>
			</tbody>
			</c:if>
			<c:if test="${not empty totpEnabled && totpEnabled}">
			<tbody>
				<tr >
					<td class="alert alert-success">2fa Enabled</td>
					<td>
					</td>
				</tr>
			</tbody>
			</c:if>
			<c:if test="${qrUrl == null && !totpEnabled}">
			<tbody>
				<tr >
					<td><a href="/setup-totp">Enable 2FA with Google Authenticator</a> </td>
					<td>
					</td>
				</tr>
			</tbody>
			</c:if>
		</table>
		</form>
	</div>
</body>
</html>