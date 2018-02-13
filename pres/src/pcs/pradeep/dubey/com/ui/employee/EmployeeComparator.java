/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

import java.util.Comparator;

import pcs.pradeep.dubey.com.employee.Employee;

/**
 * @author prdubey
 *
 */
public class EmployeeComparator implements Comparator<Employee> {

	@Override
	public int compare(Employee arg0, Employee arg1) {

		return arg0.getEmpId().compareTo(arg1.getEmpId());
	}

}
