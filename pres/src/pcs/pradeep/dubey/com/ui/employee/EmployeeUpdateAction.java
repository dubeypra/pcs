/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.ui.facade.EmployeeFacade;
import pcs.pradeep.dubey.com.utility.DateTimeUtlity;

/**
 * Handle Create Request of the Employee
 * 
 * @author prdubey
 *
 */
public class EmployeeUpdateAction extends EmployeeAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> empIdList;
	private Employee employee;
	private String empIdToUpdated;

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public Date getDoj() {
		return doj;
	}

	public void setDoj(Date doj) {
		this.doj = doj;
	}

	private Date dob;
	private Date doj;

	public EmployeeUpdateAction() {
		empIdList = new ArrayList<String>();
		List<Employee> employeeList = getEmployeeDataForDB();

		for (Employee employee : employeeList) {
			empIdList.add(employee.getEmpId());
		}
		Collections.sort(empIdList);
	}

	public String getEmpIdToUpdated() {
		return empIdToUpdated;
	}

	public void setEmpIdToUpdated(String empIdToUpdated) {
		this.empIdToUpdated = empIdToUpdated;
	}

	public List<String> getEmpIdList() {
		return empIdList;
	}

	public void setEmpIdList(List<String> empIdList) {
		this.empIdList = empIdList;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employeeToUpdated) {
		employee = employeeToUpdated;
	}

	@SkipValidation
	public String createEmployeeUpdateForm() {
		return "employeeUpdate";
	}

	@SkipValidation
	public String fetchDetails() {
		EmployeeFacade facade = new EmployeeFacade();
		employee = facade.retreiveEmployee(empIdToUpdated);

		updateFormWithModel();

		return "fetchDetails";
	}

	private void updateFormWithModel() {
		// Date are persisted in XML format so they must be reverse mapped
		dob = DateTimeUtlity.convertXMLDateToDate(employee.getPersonalDetails().getDob());
		doj = DateTimeUtlity.convertXMLDateToDate(employee.getDateOfJoining());

		setFirstName(employee.getPersonalDetails().getFirstName());
		setMiddleName(employee.getPersonalDetails().getMiddleName());
		setLastName(employee.getPersonalDetails().getLastName());
		setDob(dob);

		empIdToUpdated = employee.getEmpId();

		setPrimaryAddress(employee.getAddressDetails().getPrimaryAddress());
		setCity(employee.getAddressDetails().getCity());
		setCountry(employee.getAddressDetails().getCountry());
		setState(employee.getAddressDetails().getState());
		setPinCode(employee.getAddressDetails().getPinCode());

		setMobileNo(employee.getCommunicationDetails().getMobileNo());
		setLandlineNo(employee.getCommunicationDetails().getLandlineNo());
		setEmail(employee.getCommunicationDetails().getEmailId());
		setDesignation(employee.getDesignation().name());
		setDoj(doj);

	}

	public String execute() {
		EmployeeFacade facade = new EmployeeFacade();
		Employee employeeChangedData = mapEmployeeData();
		employeeChangedData.setEmpId(empIdToUpdated);
		facade.updateEmployee(employeeChangedData);
		return "employeeDashboard";
	}

	public void validate() {
		additionalValidations();
	}

}
