/**
 * 
 */
package pcs.pradeep.dubey.com.service.employee;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import pcs.pradeep.dubey.com.employee.Employee;

/**
 * @author prdubey
 *
 */
@Path("/EmployeeDataService")
public class EmployeeDataService {

    @GET
    @Path("/allEmployee")
    @Produces(MediaType.APPLICATION_XML)
    public List<Employee> getUsers() {
	try {
	    return EmployeeDao.Service.INSTANCE.getAllEmployee();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Devloped for Testing purpose only
     * 
     * @param args
     */
    public static void main(String args[]) {
	EmployeeDataService dataService = new EmployeeDataService();
	System.out.println(dataService.getUsers().size());
    }
}
