<%@taglib uri="/struts-tags" prefix="s"%>
<body>
	<div class="sideMenuDimension">
		<a href="<s:url action="EmployeeAction"/>">Retrieve Employees</a><br>
		<a href="<s:url action="EmployeeAction!createEmployeeForm"/>">Create
			Employee</a><br> <a
			href="<s:url action="EmployeeAction!updateEmployeeForm"/>">Update
			Employee</a><br> <a
			href="<s:url action="EmployeeAction!deleteEmployeeForm"/>">Delete
			Employee</a><br>
	</div>
</body>