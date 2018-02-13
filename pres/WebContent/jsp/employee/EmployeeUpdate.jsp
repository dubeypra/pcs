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
			<s:form action="EmployeeUpdate" validate="true" method="post">
				<!-- Personal Details Form -->
				<s:textfield label="Employee Id" key="empIdToUpdated"  disabled="true"></s:textfield>
				<s:textfield label="First Name (*)" key="firstName" ></s:textfield>
				<s:textfield label="Middle Name" key="middleName" ></s:textfield>
				<s:textfield label="Last Name (*)" key="lastName"></s:textfield>

				<sx:datetimepicker label="Date of Birth (*)"
					displayFormat="dd-MMM-yyyy" key="dob"/>

				<!-- Address Details form -->
				<s:textarea label="Address (*)" key="primaryAddress"></s:textarea>
				<s:textfield label="City(*)" key="city"></s:textfield>

				<s:select label="Country(*)" headerKey="-1"
					headerValue="Select Country " list="countryList" key="country" />

				<s:select label="Sate(*)" headerKey="-1" headerValue="Select State "
					list="stateList" key="state" />


				<s:textfield label="Pin Code(*)" key="pinCode"></s:textfield>
				<!-- Communication Details  -->

				<s:textfield label="Mobile No(*)" key="mobileNo"></s:textfield>
				<s:textfield label="landline No" key="landlineNo" >
				</s:textfield>
				<s:textfield label="email(*)" key="email" >
				</s:textfield>

				<s:select label="Designation(*)" headerKey="-1"
					headerValue="Select Designation " list="desginationList"
					key="designation" />

				<sx:datetimepicker label="Date of Joining(*)"
					displayFormat="dd-MMM-yyyy" key="doj" />

				
				<s:submit label="Update" id="update"></s:submit>
			</s:form>
		</s:if>
	</s:div>
</body>
</html>