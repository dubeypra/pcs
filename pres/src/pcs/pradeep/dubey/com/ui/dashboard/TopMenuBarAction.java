/**
 * 
 */
package pcs.pradeep.dubey.com.ui.dashboard;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author prdubey
 *
 */
public class TopMenuBarAction extends ActionSupport{

	private static final long serialVersionUID = 835632715171710917L;
	
	public String employee() {
		return "employeeSideMenu";
	}

	public String customer() {
		return "customerSideMenu";
	}

	public String admin() {
		return "adminSideMenu";
	}
	
	public String project() {
		return "projectSideMenu";
	}
	
	public String help() {
		return "helpSideMenu";
	}

}
