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
	<table border="1" cellpadding="2" cellspacing="2" align="center" class="dashboardTable">
		<tr>
			<td>
				<div class="topMenuBar">
					<tiles:insertAttribute name="header" />
				</div>
			</td>
		</tr>
		<tr>
			<td>
				<table border="1" cellpadding="2" cellspacing="2" align="center">
					<tr>
						<td valign="top">
							<div class="dashBoardMenuDiv">
								<tiles:insertAttribute name="menu" />
							</div>
						</td>
						<td align="left">
							<div class="dashBoardBodyDiv">
								<tiles:insertAttribute name="body" />
							</div>
						</td>
					</tr>
				</table>

			</td>

		</tr>
		<tr>
			<td valign="baseline">
				<div class="dashBoardFooter">
					<tiles:insertAttribute name="footer" />
				</div>
			</td>
		</tr>
	</table>
</body>
</html>
