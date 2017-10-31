<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link type="text/css" rel="stylesheet" href="/pres/css/pcs.css" />
</head>
<body>
	<div>
		<table cellspacing="5" cellpadding="5" border="0" width="325">
			<tr>
				<td>
					<table cellspacing="0" cellpadding="1" border="1" width="300">
						<tr>
							<th>ID</th>
							<th>NAME</th>
							<th>DESIGNATION</th>
							<th>Mobile</th>
							<th>email</th>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<div class="divWithScrolableTable">
						<table cellspacing="0" cellpadding="1" border="1" width="300">
							<s:iterator status="stat" value="employeeList"
								var="employeeDetail">
								<tr>
									<td><s:property value="empId" /></td>
									<td><s:property
											value="#employeeDetail.personalDetails.firstName" />&nbsp; <s:property
											value="#employeeDetail.personalDetails.lastName" /></td>
									<td><s:property value="designation" /></td>
									<td><s:property value="#employeeDetail.communicationDetails.mobileNo" /></td>
									<td><s:property value="#employeeDetail.communicationDetails.emailId" /></td>
								</tr>
							</s:iterator>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>