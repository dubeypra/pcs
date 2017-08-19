/**
 * 
 */
package pcs.pradeep.dubey.com.service.customer;

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

import pcs.pradeep.dubey.com.customer.Customer;
import pcs.pradeep.dubey.com.customer.CustomerList;

/**
 * @author prdubey
 *
 */
@Path("/CustomerDataService")
public class CustomerDataService {

    private static final String SUCCESS_RESULT = "<result>success</result>";
    private static final String FAILURE_RESULT = "<result>failure</result>";

    @GET
    @Path("/customers")
    @Produces(MediaType.APPLICATION_XML)
    public CustomerList getCustomers() {
	try {
	    return CustomerDao.Service.INSTANCE.getAllCustomers();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @GET
    @Path("/customers/{customerId}")
    @Produces(MediaType.APPLICATION_XML)
    public Customer getCustomer(@PathParam("customerId") String customerId) {
	try {
	    return CustomerDao.Service.INSTANCE.getCustomer(customerId);
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @POST
    @Path("/customers/update")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String updateCustomer(String incomingXML) throws IOException {
	System.out.println("Incoming Customer  Details are : " + incomingXML);
	boolean isTransaction = CustomerDao.Service.INSTANCE.updateCustomer(incomingXML);

	if (isTransaction) {
	    return SUCCESS_RESULT;
	}
	return FAILURE_RESULT;
    }

    @POST
    @Path("/customers/create")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public String createCustomer(String incomingXML) throws IOException {
	System.out.println("Incoming Customer Details are : " + incomingXML);
	String customerId = CustomerDao.Service.INSTANCE.createCustomer(incomingXML);

	if (customerId != null) {
	    return "<result>" + customerId + "</result>";
	}
	return FAILURE_RESULT;
    }

    @DELETE
    @Path("/customers/{custId}")
    @Produces(MediaType.APPLICATION_XML)
    public String deleteUser(@PathParam("custId") String customerId) {
	boolean isTransctionSuccessFul = CustomerDao.Service.INSTANCE.deleteCustomer(customerId);
	if (isTransctionSuccessFul) {
	    return SUCCESS_RESULT;
	}
	return FAILURE_RESULT;
    }
}
