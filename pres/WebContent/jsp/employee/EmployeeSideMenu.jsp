<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/pres/css/pcs.css">
</head>
<body>
	<div>
		<table>
			<tr>
				<th><a href="EmployeeAction" title="retrieve Employees">Retrieve Employees </a></th>
			</tr>

			<tr>
				<th><a href="EmployeeAction!updateEmployeeForm" title="update Employee">Update Employee</a></th>
			</tr>

			<tr>
				<th><a href="EmployeeAction!createEmployeeForm" title="create Employee">Create Employee</a></th>
			</tr>

			<tr>
				<th><a href="EmployeeAction!deleteEmployeeForm" title="delete Employee">Delete Employee</a></th>
			</tr>
		</table>

	</div>
</body>
</html>