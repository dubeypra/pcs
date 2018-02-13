/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import java.util.ArrayList;
import java.util.Collections;
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
public class EmployeeDeleteAction extends EmployeeAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Employee employee;
	private String empIdToUpdated;

	private List<String> empIdList;

	public List<String> getEmpIdList() {
		return empIdList;
	}

	public void setEmpIdList(List<String> empIdList) {
		this.empIdList = empIdList;
	}

	public EmployeeDeleteAction() {

		empIdList = new ArrayList<String>();
		List<Employee> employeeList = getEmployeeDataForDB();

		for (Employee employee : employeeList) {
			empIdList.add(employee.getEmpId());
		}
		Collections.sort(empIdList);

	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getEmpIdToUpdated() {
		return empIdToUpdated;
	}

	public void setEmpIdToUpdated(String empIdToUpdated) {
		this.empIdToUpdated = empIdToUpdated;
	}

	@SkipValidation
	public String createEmployeeDeleteForm() {
		return "employeeDelete";
	}

	public String execute() {
		EmployeeFacade facade = new EmployeeFacade();
		facade.deleteEmployee(empIdToUpdated);
		return "employeeDashboard";
	}
}
