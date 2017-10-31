<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<s:div>
		<table border="1" cellpadding="2" cellspacing="2" align="center"
			width="100%" height="100%">
			<tr>
				<td colspan="2">
					<p><h1>Login Page</h1></p>
				</td>
			</tr>

			<s:form action="LoginAction">

				<tr>
					<td colspan="2"><s:textfield label="User ID" key="userId"></s:textfield></td>
				</tr>

				<tr>
					<td colspan="2"><s:password label="Password" key="password">
						</s:password></td>
				</tr>

				<tr>
					<td><s:submit></s:submit></td>
				</tr>



			</s:form>
		</table>
	</s:div>
</body>
</html>