<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib uri="/struts-tags" prefix="s"%>
<%@taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="overflowBodyDiv">
	<table class="blueTable">
		<thead>
			<tr>
				<th>ID</th>
				<th>NAME</th>
				<th>DESIGNATION</th>
				<th>Mobile</th>
				<th>email</th>
			</tr>
		</thead>

<!-- 		<tfoot> -->
<!-- 			<tr> -->
<!-- 				<td colspan="4"> -->
<!-- 					<div class="links"> -->
<!-- 						<a href="#">&laquo;</a> <a class="active" href="#">1</a> <a -->
<!-- 							href="#">2</a> <a href="#">3</a> <a href="#">4</a> <a href="#">&raquo;</a> -->
<!-- 					</div> -->
<!-- 				</td> -->
<!-- 			</tr> -->
<!-- 		</tfoot> -->
		<tbody>
			<s:iterator status="stat" value="employeeList" var="employeeDetail">
				<tr>
					<td><s:property value="empId" /></td>
					<td><s:property
							value="#employeeDetail.personalDetails.firstName" />&nbsp; <s:property
							value="#employeeDetail.personalDetails.lastName" /></td>
					<td><s:property value="designation" /></td>
					<td><s:property
							value="#employeeDetail.communicationDetails.mobileNo" /></td>
					<td><s:property
							value="#employeeDetail.communicationDetails.emailId" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>
