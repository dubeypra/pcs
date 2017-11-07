<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="/pres/css/pcs.css">
</head>
<body>
	<div class="header">
		<tiles:insertAttribute name="header" />
	</div>
	<div class="row">
		<div class="column side">
			<tiles:insertAttribute name="menu" />
		</div>
		<div class="column middle">
			<tiles:insertAttribute name="body" />
		</div>

	</div>

	<div class="footer">
		<tiles:insertAttribute name="footer" />
	</div>
</body>
</html>
