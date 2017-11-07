/**
 * 
 */
package pcs.pradeep.dubey.com.ui.employee;

/**
 * Handle Create Request of the Employee
 * 
 * @author prdubey
 *
 */
public class EmployeeDashBoardAction extends EmployeeAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeDashBoardAction() {
	}

	public String populateDashBoard() {
		setEmployeeList(getEmployeeDataForDB());
		return "employeeDashboard";
	}

	public String populateSideMenu() {
		return "employeeSideMenu";
	}

}
