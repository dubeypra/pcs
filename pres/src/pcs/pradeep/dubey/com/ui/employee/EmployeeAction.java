/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import pcs.pradeep.dubey.com.baseentity.AddressDetails;
import pcs.pradeep.dubey.com.baseentity.CommunicationDetails;
import pcs.pradeep.dubey.com.baseentity.PersonalDetails;
import pcs.pradeep.dubey.com.employee.Designation;
import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.ui.facade.EmployeeFacade;
import pcs.pradeep.dubey.com.utility.DateTimeUtlity;

/**
 * @author prdubey
 *
 */
public class EmployeeAction extends ActionSupport {

	private List<Employee> employeeList;
	private List<String> desginationList;

	public List<String> getDesginationList() {
		return desginationList;
	}

	public void setDesginationList(List<String> desginationList) {
		this.desginationList = desginationList;
	}

	private String firstName;
	private String middleName;
	private String lastName;
	private Date dob;

	private String primaryAddress;
	private String city;
	private String state;
	private String pinCode;

	private String country;
	
	private String mobileNo;
	private String landlineNo;
	private String email;
	private String designation;
	private Date doj;

	public EmployeeAction() {
		Designation[] designations = Designation.values();
		desginationList = new ArrayList();
		for (Designation designation : designations) {
			desginationList.add(designation.name());
		}
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPrimaryAddress() {
		return primaryAddress;
	}

	public void setPrimaryAddress(String primaryAddress) {
		this.primaryAddress = primaryAddress;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getLandlineNo() {
		return landlineNo;
	}

	public void setLandlineNo(String landlineNo) {
		this.landlineNo = landlineNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Date getDoj() {
		return doj;
	}

	public void setDoj(Date doj) {
		this.doj = doj;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String execute() {
		return "success";
	}

	public String populateDashBoard() {
		try {
			EmployeeFacade facade = new EmployeeFacade();
			employeeList = facade.retrieveEmployees();
		} catch (Exception ex) {
			return "failure";
		}
		return "employeeDashboard";
	}

	public String populateSideMenu() {
		return "employeeSideMenu";
	}

	public String updateEmployeeForm() {
		return "employeeUpdate";
	}

	public String createEmployeeForm() {
		return "employeeCreate";
	}

	public String deleteEmployeeForm() {
		return "employeeDelete";
	}

	public String createEmployee() {
		EmployeeFacade facade = new EmployeeFacade();
		facade.createEmployee(mapEmployeeData());
		populateDashBoard(); // Show the Update Employee List after creation
		return "employeeDashboard";
	}

	private Employee mapEmployeeData() {
		Employee employee = new Employee();

		PersonalDetails personalDetails = new PersonalDetails();
		personalDetails.setFirstName(getFirstName());
		personalDetails.setMiddleName(getMiddleName());
		personalDetails.setLastName(getLastName());
		personalDetails.setDob(DateTimeUtlity.convertDateToXMLDate(getDob()));
		employee.setPersonalDetails(personalDetails);

		CommunicationDetails communicationDetails = new CommunicationDetails();
		communicationDetails.setEmailId(getEmail());
		communicationDetails.setLandlineNo(getLandlineNo());
		communicationDetails.setMobileNo(getMobileNo());

		employee.setCommunicationDetails(communicationDetails);

		AddressDetails addressDetails = new AddressDetails();
		addressDetails.setCity(getCity());
		addressDetails.setCountry(getCountry());
		addressDetails.setPinCode(getPinCode());
		addressDetails.setPrimaryAddress(getPrimaryAddress());
		addressDetails.setState(getState());
		employee.setAddressDetails(addressDetails);

		employee.setDesignation(Designation.valueOf(getDesignation()));
		employee.setDateOfJoining(DateTimeUtlity.convertDateToXMLDate(getDoj()));
		return employee;
	}

	/**
	 * @return the employeeList
	 */
	public List<Employee> getEmployeeList() {
		return this.employeeList;
	}

	/**
	 * @param employeeList
	 *            the employeeList to set
	 */
	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
}
