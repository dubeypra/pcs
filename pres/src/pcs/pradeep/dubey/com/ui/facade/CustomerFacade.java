/**
 * 
 */
package pcs.pradeep.dubey.com.ui.facade;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import pcs.pradeep.dubey.com.baseentity.AddressDetails;
import pcs.pradeep.dubey.com.baseentity.CommunicationDetails;
import pcs.pradeep.dubey.com.baseentity.PersonalDetails;
import pcs.pradeep.dubey.com.customer.Customer;
import pcs.pradeep.dubey.com.customer.CustomerList;

/**
 * This class will do all the communication for the Customer related information
 * for UI Facade has one to one mapping with the Web Service
 * 
 * @author prdubey
 *
 */
public class CustomerFacade extends Facade {

    public static String BASE_URL = "http://localhost:8080/services/rest/CustomerDataService/";

    public static String GET_ALL_CUSTOMER = "customers";
    public static String UPDATE_CUSTOMER = "customers/update";
    public static String CREATE_CUSTOMER = "customers/create";
    public static String DELETE_CUSTOMER = "customers";

    public static String OPERATION_GET = "GET";
    public static String OPERATION_POST = "POST";
    public static String OPERATION_DELETE = "DELETE";

    /**
     * @return List of all the Customers
     */
    public List<Customer> retrieveCustomers() {
	List<Customer> customerList = new ArrayList<Customer>();
	try {
	    HttpURLConnection conn = connectDataService(BASE_URL + GET_ALL_CUSTOMER, OPERATION_GET);

	    JAXBContext jaxbContext = JAXBContext.newInstance(CustomerList.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    CustomerList customerList1 = (CustomerList) (jaxbUnmarshaller.unmarshal(conn.getInputStream()));

	    isServiceCallSuccessful(conn);

	    customerList = customerList1.getCustomerList();
	    System.out.println(customerList.size());

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return customerList;
    }

    /**
     * Fetch the specific Cusomer Id
     * 
     * @param customerId
     *            : Id
     * @return: Customer
     * 
     */
    public Customer retreiveCustomer(String customerId) {
	Customer customer = null;
	try {
	    String urlPath = BASE_URL + GET_ALL_CUSTOMER + "/" + customerId;
	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_GET);

	    JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    customer = (Customer) (jaxbUnmarshaller.unmarshal(conn.getInputStream()));

	    isServiceCallSuccessful(conn);

	    System.out.println("Customer is : " + customer.getCustomerId());

	    conn.disconnect();

	} catch (IOException | RuntimeException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return customer;
    }

    /**
     * This method will update the customer data in the file system As per make
     * is more generic we have passes the complete changed Customer from UI
     * layer to Bussiness Layer so that all the data will be in sync
     * 
     * @param customer
     * @return
     */
    public String createCustomer(Customer customer) {
	String customerId = "-1";

	JAXBContext jaxbContext;
	try {
	    jaxbContext = JAXBContext.newInstance(Customer.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    String urlPath = BASE_URL + CREATE_CUSTOMER;

	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_POST);
	    conn.setRequestProperty("Content-Type", "application/xml");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    jaxbMarshaller.marshal(customer, conn.getOutputStream());

	    String response = readResponse(conn);
	    isServiceCallSuccessful(conn);

	    System.out.println(response.toString());

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (RuntimeException e) {
	    e.printStackTrace();
	} catch (JAXBException e1) {
	    e1.printStackTrace();
	}
	return customerId;
    }

    /**
     * This method will update the Customer data in the file system As per make
     * is more generic we have passes the complete changed Customer from UI
     * layer to Bussiness Layer so that all the data will be in sync
     * 
     * @param customer
     * @return
     */
    public boolean updateCustomer(Customer customer) {
	boolean isTransactionSuccessFul = true;

	JAXBContext jaxbContext;
	try {
	    jaxbContext = JAXBContext.newInstance(Customer.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    String urlPath = BASE_URL + UPDATE_CUSTOMER;

	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_POST);
	    conn.setRequestProperty("Content-Type", "application/xml");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    jaxbMarshaller.marshal(customer, conn.getOutputStream());

	    String response = readResponse(conn);
	    isServiceCallSuccessful(conn);

	    System.out.println(response.toString());

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (RuntimeException e) {
	    e.printStackTrace();
	} catch (JAXBException e1) {
	    e1.printStackTrace();
	}
	return isTransactionSuccessFul;
    }

    /**
     * This method deals with delete of the Customer from the System. Clean up
     * of all the data linked with this Customer Id is not implemented. Moreover
     * inspite of hard delete we can move this data to obsolete folder which
     * must have the same structure as current data structure had.
     * 
     * Orchestration of the Delete related views will be implemented from here
     * by invoking respective methods from other facades
     * 
     * @param customerId
     * @return
     */
    public boolean deleteCustomer(String customerId) {
	boolean isTransactionSuccesfull = true;
	String urlPath = BASE_URL + DELETE_CUSTOMER + "/" + customerId;
	try {
	    HttpURLConnection conn = connectDataService(urlPath, OPERATION_DELETE);

	    String response = readResponse(conn);
	    isServiceCallSuccessful(conn);
	    System.out.println(response.toString());

	    conn.disconnect();

	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ProtocolException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (RuntimeException e) {
	    e.printStackTrace();
	}
	return isTransactionSuccesfull;
    }

    public static void main(String args[]) {
	CustomerFacade customerFacade = new CustomerFacade();
	// customerFacade.retrieveCustomers();
	customerFacade.updateCustomer(createDummyCustomerData());

    }

    /**
     * @return dummy test Data
     */
    private static Customer createDummyCustomerData() {
	Customer customer = new Customer();
	AddressDetails addressDetails = new AddressDetails();
	addressDetails.setCity("Delhi");
	addressDetails.setCountry("India");
	addressDetails.setPinCode("11001");
	addressDetails.setPrimaryAddress("HN 2388");
	addressDetails.setState("Delhi");

	customer.setAddressDetails(addressDetails);

	CommunicationDetails communicationDetails = new CommunicationDetails();
	communicationDetails.setEmailId("xyz@gmil.com");
	communicationDetails.setLandlineNo("123445");
	communicationDetails.setMobileNo("3523464");
	customer.setCommunicationDetails(communicationDetails);

	customer.setCustomerId("5001");

	PersonalDetails personalDetails = new PersonalDetails();
	// personalDetails.setDob(GregorianCalendar.getInstance().getTime());
	personalDetails.setFirstName("Dafasf");
	personalDetails.setLastName("Adaakha");
	personalDetails.setMiddleName("");
	customer.setPersonalDetails(personalDetails);

	return customer;

    }

}
