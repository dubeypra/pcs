<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<sx:head/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="/pres/css/pcs.css">
</head>
<body>
	<s:div>
		<s:form>
			<!-- Personal Details Form -->
			<s:textfield label="First Name" key="firstName"></s:textfield>
			<s:textfield label="Middle Name" key="middleName"></s:textfield>
			<s:textfield label="Last Name" key="lastName"></s:textfield>

			<sx:datetimepicker  label="Date of Birth" displayFormat="dd-MMM-yyyy"
				 key="dob"/>

			<!-- Address Details form -->
			<s:textarea label="Address" key="primaryAddress"></s:textarea>
			<s:textfield label="City" key="city"></s:textfield>
			<s:textfield label="Sate" key="state"></s:textfield>
			<s:textfield label="Pin Code" key="pinCode"></s:textfield>
			<s:textfield label="Country" key="country"></s:textfield>

			<!-- Communication Details  -->

			<s:textfield label="Mobile No" key="mobileNo"></s:textfield>
			<s:textfield label="landline No" key="landlineNo">
			</s:textfield>
			<s:textfield label="email" key="email">
			</s:textfield>

			<!-- Specific o employee -->
			<s:combobox label="Designation" key="designation"
				list="desginationList">
			</s:combobox>

			<sx:datetimepicker label="Date of Joining"
				displayFormat="dd-MMM-yyyy" key="doj" />


			<s:submit action="EmployeeAction!createEmployee"></s:submit>

		</s:form>
	</s:div>
</body>
</html>