/**
 * 
 */
package pcs.pradeep.dubey.com.service.employee;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.employee.EmployeeList;;

/**
 * @author prdubey
 *
 */
@Path("/EmployeeDataService")
public class EmployeeDataService {

    private static final String SUCCESS_RESULT = "<result>success</result>";
    private static final String FAILURE_RESULT = "<result>failure</result>";

    @GET
    @Path("/employees")
    @Produces(MediaType.APPLICATION_XML)
    public EmployeeList getEmployees() {
	try {
	    return EmployeeDao.Service.INSTANCE.getAllEmployee();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @GET
    @Path("/employees/{empId}")
    @Produces(MediaType.APPLICATION_XML)
    public Employee getEmployee(@PathParam("empId") String employeeId) {
	try {
	    return EmployeeDao.Service.INSTANCE.getEmployee(employeeId);
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @POST
    @Path("/employees/update")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String updateEmployee(String incomingXML) throws IOException {
	System.out.println("Incoming Employee Details are : " + incomingXML);
	boolean isTransaction = EmployeeDao.Service.INSTANCE.updateEmployee(incomingXML);

	if (isTransaction) {
	    return SUCCESS_RESULT;
	}
	return FAILURE_RESULT;
    }

    @POST
    @Path("/employees/create")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String createEmployee(String incomingXML) throws IOException {
	System.out.println("Incoming Employee Details are : " + incomingXML);
	String empId = EmployeeDao.Service.INSTANCE.createEmployee(incomingXML);

	if (empId != null) {
	    return "<result>" + empId + "</result>";
	}
	return FAILURE_RESULT;
    }

    @DELETE
    @Path("/employees/{empId}")
    @Produces(MediaType.APPLICATION_XML)
    public String deleteUser(@PathParam("empId") String empId) {
	boolean isTransctionSuccessFul = EmployeeDao.Service.INSTANCE.deleteEmployee(empId);
	if (isTransctionSuccessFul) {
	    return SUCCESS_RESULT;
	}
	return FAILURE_RESULT;
    }
}
