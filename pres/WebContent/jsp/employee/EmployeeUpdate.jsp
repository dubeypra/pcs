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
		
			<s:form action="EmployeeUpdateFormfetchDetails" method="post">
				<s:select label="Employee Ids" headerKey="-1"
					headerValue="Select Employee " list="empIdList"
					key="empIdToUpdated" />

				<s:submit></s:submit>
			</s:form>
		</s:if>
		
		<s:if test="%{employee!=null}">
			<s:form action="EmployeeUpdate" validate="true">
				<!-- Personal Details Form -->
				<s:textfield label="First Name (*)" key="firstName" name="employee.personalDetails.firstName"></s:textfield>
				<s:textfield label="Middle Name" key="middleName" name ="employee.personalDetails.middleName"></s:textfield>
				<s:textfield label="Last Name (*)" key="lastName" name="employee.personalDetails.lastName"></s:textfield>

				<sx:datetimepicker label="Date of Birth (*)"
					displayFormat="dd-MMM-yyyy" key="dob" name="employee.personalDetails.dob"/>

				<!-- Address Details form -->
				<s:textarea label="Address (*)" key="primaryAddress" name="employee.addressDetails.primaryAddress"></s:textarea>
				<s:textfield label="City(*)" key="city" name="employee.addressDetails.city"></s:textfield>

				<s:select label="Country(*)" headerKey="-1"
					headerValue="Select Country " list="countryList" key="country" name="employee.addressDetails.country"/>

				<s:select label="Sate(*)" headerKey="-1" headerValue="Select State "
					list="stateList" key="state" name="employee.addressDetails.country.state"/>


				<s:textfield label="Pin Code(*)" key="pinCode" name="employee.addressDetails.country.pinCode"></s:textfield>
				<!-- Communication Details  -->

				<s:textfield label="Mobile No(*)" key="mobileNo" name="employee.communicationDetails.mobileNo"></s:textfield>
				<s:textfield label="landline No" key="landlineNo" name="employee.communicationDetails.landlineNo">
				</s:textfield>
				<s:textfield label="email(*)" key="email" name="employee.communicationDetails.emailId">
				</s:textfield>

				<s:select label="Designation(*)" headerKey="-1"
					headerValue="Select Designation " list="desginationList"
					key="designation" name="employee.designation"/>

				<sx:datetimepicker label="Date of Joining(*)"
					displayFormat="dd-MMM-yyyy" key="doj" name="employee.dateOfJoining"/>

				<s:submit label="Update" id="update"></s:submit>
			</s:form>
		</s:if>
	</s:div>
</body>
</html>