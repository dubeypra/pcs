package pcs.pradeep.dubey.com.fileHandler;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import pcs.pradeep.dubey.com.customer.Customer;
import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.project.Project;

/**
 * Utility class Mapped XML info into Java Entity
 * 
 * @author prdubey
 *
 */
public class XmlToObject {

    private static final String FILE_EXTENSION = ".xml";

    /**
     * This method will load the customer infomation from the file system.
     * Customer Snapshot is saved at the Server in the user data
     * 
     * @param relativePath
     *            : Relative Path to load customer data
     * @param customerId:
     *            Id of the customer whose information has to be loaded
     * @return : Customer Snapshot
     * @throws JAXBException
     */
    public static Customer loadCustomer(String relativePath, String customerId) throws JAXBException {
	Customer customer = null;
	try {

	    JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    customer = (Customer) (jaxbUnmarshaller.unmarshal(new File(relativePath + customerId + FILE_EXTENSION)));

	} catch (JAXBException e) {
	    throw e;
	}
	return customer;
    }

    /**
     * This method will load the Employee infomation from the file system.
     * Employee Snapshot is saved at the Server in the user data
     * 
     * @param relativePath
     *            : Relative Path to load Employee data
     * @param employeeId:
     *            Id of the employee whose information has to be loaded
     * @return : {@link Employee} Snapshot
     * @throws JAXBException
     */
    public static Employee loadEmployee(String relativePath, String employeeId) throws JAXBException {
	Employee employee = null;
	try {

	    JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    employee = (Employee) (jaxbUnmarshaller.unmarshal(new File(relativePath + employeeId + FILE_EXTENSION)));

	} catch (JAXBException e) {
	    throw e;
	}
	return employee;
    }

    /**
     * This method will load the Project infomation from the file system.
     * Project Snapshot is saved at the Server in the user data
     * 
     * @param relativePath
     *            : Relative Path to load Project data
     * @param projectId:
     *            Id of the project whose information has to be loaded
     * @return : Project Snapshot
     * @throws JAXBException
     */
    public static Project loadPoject(String relativePath, String projectId) throws JAXBException {
	Project project = null;
	try {

	    JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    project = (Project) (jaxbUnmarshaller.unmarshal(new File(relativePath + projectId + FILE_EXTENSION)));

	} catch (JAXBException e) {
	    throw e;
	}
	return project;
    }
}
