/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import org.apache.struts2.interceptor.validation.SkipValidation;

import pcs.pradeep.dubey.com.ui.facade.EmployeeFacade;

/**
 * Handle Create Request of the Employee Handle two actions 1. Creation of the
 * Form of the Employee Details 2. Submit Action of this form
 * 
 * @author prdubey
 *
 */
public class EmployeeCreateAction extends EmployeeAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeCreateAction() {
	}

	@SkipValidation
	public String createEmployeeForm() {
		return "employeeCreate";
	}

	public String execute() {
		EmployeeFacade facade = new EmployeeFacade();
		facade.createEmployee(mapEmployeeData());
		return "employeeDashboard";
	}

	public void validate() {
		additionalValidations();
	}
}
