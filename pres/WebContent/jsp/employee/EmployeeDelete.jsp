<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<sx:head />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/pres/css/pcs.css">
</head>
<body>
	<s:div class="overflowBodyDiv">
	
		<s:if test="%{employee==null}">
		
			<s:form action="EmployeeDelete" method="post">
				<s:select label="Employee Ids" headerKey="-1"
					headerValue="Select Employee " list="empIdList"
					key="empIdToUpdated" />

				<s:submit></s:submit>
			</s:form>
		</s:if>
	</s:div>
</body>
</html>