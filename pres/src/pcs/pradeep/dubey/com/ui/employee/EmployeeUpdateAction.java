/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;

import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.ui.facade.EmployeeFacade;

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

	public EmployeeUpdateAction() {
		empIdList = new ArrayList<String>();
		List<Employee> employeeList = getEmployeeDataForDB();

		for (Employee employee : employeeList) {
			empIdList.add(employee.getEmpId());
		}
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
		return "fetchDetails";
	}

	public String execute() {
		EmployeeFacade facade = new EmployeeFacade();
		facade.updateEmployee(mapEmployeeData());
		return "employeeDashboard";
	}

	public void validate() {
		additionalValidations();
	}

}
