package pcs.pradeep.dubey.com.service.customer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import pcs.pradeep.dubey.com.customer.Customer;
import pcs.pradeep.dubey.com.customer.CustomerList;
import pcs.pradeep.dubey.com.service.utils.ApplicationConstants;
import pcs.pradeep.dubey.com.service.utils.DataFileFolderLocations;
import pcs.pradeep.dubey.com.service.utils.Utility;

/**
 * This class will deals with the file handling related to the Customer
 * 
 * @author prdubey
 *
 */
public class CustomerDao {

    enum Service {
	INSTANCE;

	/**
	 * This map will populate when all the Customer are loaded
	 */
	private HashMap<String, Customer> customerMap;

	public static String UPDATE_CUSTOMER_ID = "999999";
	public static String CREATE_CUSTOMER_ID = "888888";
	public static String INTERMEDIATE_TEMP_FILE_IDENTIFIER = "TEMP_FILE";
	public static String INTERMEDIATE_CUSTOMER_IDENTIFIER = "CUSTOMER";

	/**
	 * This counter is used to track the employee Id of last used
	 */
	private static int lastUsedCustomerId;

	private Service() {

	    customerMap = new HashMap<String, Customer>();
	    try {
		loadCustomerData();
	    } catch (JAXBException e) {
		e.printStackTrace();
	    }
	}

	/**
	 * Used to retrieve all the customers stored in the system
	 * 
	 * @return list of the Customer Object
	 */
	private List<Customer> loadCustomerData() throws JAXBException {
	    List<Customer> customerList = new ArrayList<Customer>();
	    List<String> customerPathList = Utility
		    .listFilesForFolder(new File(DataFileFolderLocations.CUSTOMER_RELATIVE_PATH));
	    JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    for (String fileLocation : customerPathList) {
		Customer customer = (Customer) (jaxbUnmarshaller.unmarshal(new File(fileLocation)));
		if (customer != null) {
		    int fetchedCustomerId = Integer.parseInt(customer.getCustomerId());
		    lastUsedCustomerId = lastUsedCustomerId < fetchedCustomerId ? fetchedCustomerId
			    : lastUsedCustomerId;
		    customerList.add(customer);
		    customerMap.put(customer.getCustomerId(), customer);
		}
	    }
	    return customerList;
	}

	/**
	 * Get all the Customer data
	 * 
	 * @return
	 * @throws JAXBException
	 */
	public CustomerList getAllCustomers() throws JAXBException {
	    CustomerList customerList = new CustomerList();
	    Collection<Customer> custList = customerMap.values();
	    for (Customer customer : custList) {
		customerList.getCustomerList().add(customer);
	    }
	    return customerList;
	}

	/**
	 * Used to retrieve all the customer stored in the system
	 * 
	 * @return Customer Value Can be Null
	 */
	public Customer getCustomer(String customerId) throws JAXBException {
	    Customer customer = null;
	    customer = customerMap.get(customerId);
	    if (customer == null) {
		try {

		    JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    String fileLocation = DataFileFolderLocations.CUSTOMER_RELATIVE_PATH + customerId
			    + DataFileFolderLocations.FILE_EXTENSION;
		    customer = (Customer) (jaxbUnmarshaller.unmarshal(new File(fileLocation)));

		} catch (JAXBException e) {
		    throw e;
		}
	    }
	    return customer;
	}

	/**
	 * This method used to create the new Customer in the System Customer Id
	 * Generarted by the system
	 * 
	 * @param customerDataXML
	 *            Stream of the Customer
	 * @return CustomerID Created by System
	 */
	public String createCustomer(String customerDataXML) {
	    if (lastUsedCustomerId == 0) {
		lastUsedCustomerId = ApplicationConstants.CUSTOMER_ID_OFFSET;
	    }
	    String customerId = String.valueOf(++lastUsedCustomerId);
	    Customer customer = null;

	    try {
		HashMap intermediateProcessMap = handleCustomerStream(customerDataXML, CREATE_CUSTOMER_ID);
		customer = (Customer) intermediateProcessMap.get(INTERMEDIATE_CUSTOMER_IDENTIFIER);
		File tempFile = (File) intermediateProcessMap.get(INTERMEDIATE_TEMP_FILE_IDENTIFIER);
		customer.setCustomerId(customerId);

		String fileLocation = DataFileFolderLocations.CUSTOMER_RELATIVE_PATH + customerId
			+ DataFileFolderLocations.FILE_EXTENSION;

		JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(customer, new FileOutputStream(fileLocation));
		tempFile.delete();

	    } catch (FileNotFoundException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    } catch (JAXBException e) {
		e.printStackTrace();
	    }
	    if (customer != null)
		customerMap.put(customerId, customer);

	    return customerId;

	}

	/**
	 * This method update the existing Customer details in the system
	 * 
	 * @param customerData
	 *            : Stream of the customer Information }
	 * @return True for Success
	 */
	public boolean updateCustomer(String customerData) {
	    boolean isTransactionSuccess = true;
	    Customer customer = null;
	    try {

		HashMap intermediateProcessMap = handleCustomerStream(customerData, UPDATE_CUSTOMER_ID);
		customer = (Customer) intermediateProcessMap.get(INTERMEDIATE_CUSTOMER_IDENTIFIER);
		File tempFile = (File) intermediateProcessMap.get(INTERMEDIATE_TEMP_FILE_IDENTIFIER);

		String fileLocation = DataFileFolderLocations.CUSTOMER_RELATIVE_PATH + customer.getCustomerId()
			+ DataFileFolderLocations.FILE_EXTENSION;

		File existingFile = new File(fileLocation);
		isTransactionSuccess = existingFile.delete();

		if (isTransactionSuccess)
		    isTransactionSuccess = tempFile.renameTo(new File(fileLocation));

	    } catch (FileNotFoundException e) {
		isTransactionSuccess = false;
		e.printStackTrace();
	    } catch (IOException e) {
		isTransactionSuccess = false;
		e.printStackTrace();
	    } catch (JAXBException e) {
		isTransactionSuccess = false;
		e.printStackTrace();
	    }
	    if (customer != null)
		customerMap.put(customer.getCustomerId(), customer);

	    return isTransactionSuccess;
	}

	/**
	 * Delete the data of the particular customer from the system
	 * 
	 * @param custId
	 * @return
	 */
	public boolean deleteCustomer(String custId) {
	    String fileLocation = DataFileFolderLocations.CUSTOMER_RELATIVE_PATH + custId
		    + DataFileFolderLocations.FILE_EXTENSION;
	    File file = new File(fileLocation);
	    return file.delete();
	}

	/**
	 * @param customerData
	 * @return
	 * @throws IOException
	 * @throws JAXBException
	 */
	private HashMap handleCustomerStream(String customerData, String customerId) throws IOException, JAXBException {
	    HashMap intermediateMap = new HashMap<>();
	    Customer customer;
	    String tempFileLocation = DataFileFolderLocations.CUSTOMER_RELATIVE_PATH + customerId
		    + DataFileFolderLocations.FILE_EXTENSION;
	    Utility.stringToDom(customerData, tempFileLocation);

	    JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    File tempFile = new File(tempFileLocation);
	    customer = (Customer) (jaxbUnmarshaller.unmarshal(tempFile));

	    intermediateMap.put(INTERMEDIATE_TEMP_FILE_IDENTIFIER, tempFile);
	    intermediateMap.put(INTERMEDIATE_CUSTOMER_IDENTIFIER, customer);
	    return intermediateMap;
	}
    }

}
