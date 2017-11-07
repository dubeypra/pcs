/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

import pcs.pradeep.dubey.com.baseentity.AddressDetails;
import pcs.pradeep.dubey.com.baseentity.CommunicationDetails;
import pcs.pradeep.dubey.com.baseentity.PersonalDetails;
import pcs.pradeep.dubey.com.employee.Designation;
import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.ui.facade.EmployeeFacade;
import pcs.pradeep.dubey.com.ui.facade.ExternalServiceFacade;
import pcs.pradeep.dubey.com.utility.AddressUtility;
import pcs.pradeep.dubey.com.utility.DateTimeUtlity;

/**
 * @author prdubey
 *
 */
public abstract class EmployeeAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Employee> employeeList;

	private List<String> desginationList;

	private List<String> stateList;
	private List<String> countryList;

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
		desginationList = new ArrayList<String>();
		Designation[] desginations = Designation.values();
		for (Designation designation : desginations) {
			desginationList.add(designation.name());
		}

		stateList = Arrays.asList(AddressUtility.getStates("India"));
		countryList = Arrays.asList(AddressUtility.getCountries());
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

	public List<String> getDesginationList() {
		return desginationList;
	}

	public void setDesginationList(List<String> desginationList) {
		this.desginationList = desginationList;
	}

	public List<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}

	public List<String> getStateList() {
		return stateList;
	}

	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}

	public List<String> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<String> countryList) {
		this.countryList = countryList;
	}

	/**
	 * @return : List of All employees
	 */
	public List<Employee> getEmployeeDataForDB() {
		List<Employee> employeeList = null;
		try {
			EmployeeFacade facade = new EmployeeFacade();
			employeeList = facade.retrieveEmployees();
		} catch (Exception ex) {

		}
		return employeeList;
	}

	/**
	 * Transform Form Input Data in to business entity
	 * 
	 * @return Employee
	 */
	public Employee mapEmployeeData() {
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
	 * Validations Required for Employee Create/Update
	 */
	public void additionalValidations() {
		// Pin Code Validation
		AddressDetails addressDetails = new AddressDetails();
		addressDetails.setPinCode(getPinCode());
		addressDetails.setCountry(getCountry());
		addressDetails.setState(getState());

		if (!ExternalServiceFacade.getAddressBasedOnPin(addressDetails)) {
			addFieldError("pinCode", "Pin Code and Provided Address details did not match");
		}

		// Designation Block
		if (!getDesginationList().contains(getDesignation())) {
			addFieldError("designation", "Designation Mandatory for Employee Creation");
		}

		// Country Block
		if (!getCountryList().contains(getCountry())) {
			addFieldError("country", "Please select Country");
		}
		// State Block

		if (!getStateList().contains(getState())) {
			addFieldError("state", "Please select State");
		}

	}

}
