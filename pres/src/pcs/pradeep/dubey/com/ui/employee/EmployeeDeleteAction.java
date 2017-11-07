/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pcs.pradeep.dubey.com.baseentity.AddressDetails;
import pcs.pradeep.dubey.com.baseentity.CommunicationDetails;
import pcs.pradeep.dubey.com.baseentity.PersonalDetails;
import pcs.pradeep.dubey.com.employee.Designation;
import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.ui.facade.EmployeeFacade;
import pcs.pradeep.dubey.com.utility.DateTimeUtlity;

/**
 * Handle Create Request of the Employee
 * 
 * @author prdubey
 *
 */
public class EmployeeDeleteAction extends EmployeeAction {
	
	
	public String deleteEmployeeForm() {
		return "employeeDelete";
	}
}
