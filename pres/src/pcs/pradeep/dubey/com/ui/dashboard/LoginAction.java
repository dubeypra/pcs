/**
 * 
 */
package pcs.pradeep.dubey.com.ui.dashboard;

import org.apache.struts2.components.Password;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author prdubey
 *
 */
public class LoginAction extends ActionSupport{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3035181492598854817L;
	
	private String userId;
	private Password password;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Password getPassword() {
		return password;
	}
	public void setPassword(Password password) {
		this.password = password;
	}

	
	public String execute() {
		//TODO : Authentication Logic has to be implemented here
		return "success";
	}
	
	public String cancel() {
		return "cancel";
	}
	
}
