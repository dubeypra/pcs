<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Employee Dashboard</title>
<link type = "text/css" rel="stylesheet" href="/css/pcs.css" />
</head>
<body>
	<s:div class="header" >
		<table>
			<tr>
				<td>Id</td>
				<td>Name</td>
				<td>Designation</td>
			</tr>
			<s:iterator status="stat" value="employeeList" var="employeeDetail">
				<tr>
				<td>
				<s:property value="empId" />
				</td>
					<td><s:property
							value="#employeeDetail.personalDetails.firstName" />&nbsp; <s:property
							value="#employeeDetail.personalDetails.lastName" /></td>
				<td><s:property value="designation"/></td>
				</tr>
			</s:iterator>
		</table>


	</s:div>

</body>
</html>