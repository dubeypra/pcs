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

import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.employee.EmployeeList;

/**
 * This class will do all the communication for the Employee related information
 * for UI Facade has one to one mapping with the Web Service
 * 
 * @author prdubey
 *
 */
public class EmployeeFacade extends Facade {

    public static String BASE_URL = "http://localhost:8080/services/rest/EmployeeDataService/";

    public static String GET_ALL_EMPLOYEE = "employees";
    public static String UPDATE_EMPLOYEE = "employees/update";
    public static String CREATE_EMPLOYEE = "employees/create";
    public static String DELETE_EMPLOYEE = "employees";

    public static String OPERATION_GET = "GET";
    public static String OPERATION_POST = "POST";
    public static String OPERATION_DELETE = "DELETE";

    /**
     * @return List of all the Employees
     */
    public List<Employee> retrieveEmployees() {
	List<Employee> employeeList = new ArrayList<Employee>();
	try {
	    HttpURLConnection conn = connectEmployeeDataService(BASE_URL + GET_ALL_EMPLOYEE, OPERATION_GET);

	    JAXBContext jaxbContext = JAXBContext.newInstance(EmployeeList.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    EmployeeList employeeList1 = (EmployeeList) (jaxbUnmarshaller.unmarshal(conn.getInputStream()));

	    isServiceCallSuccessful(conn);

	    employeeList = employeeList1.getEmployeeList();
	    System.out.println(employeeList.size());

	    conn.disconnect();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return employeeList;
    }

    /**
     * Fetch the specific employee Id
     * 
     * @param empId
     *            : Employee Id
     * @return: Employee
     * 
     */
    public Employee retreiveEmployee(String empId) {
	Employee employee = null;
	try {
	    String urlPath = BASE_URL + GET_ALL_EMPLOYEE + "/" + empId;
	    HttpURLConnection conn = connectEmployeeDataService(urlPath, OPERATION_GET);

	    JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

	    employee = (Employee) (jaxbUnmarshaller.unmarshal(conn.getInputStream()));

	    isServiceCallSuccessful(conn);

	    System.out.println("Employee is : " + employee.getDesignation());

	    conn.disconnect();

	} catch (IOException | RuntimeException e) {
	    e.printStackTrace();
	} catch (JAXBException e) {
	    e.printStackTrace();
	}
	return employee;
    }

    /**
     * This method will update the employee data in the file system As per make
     * is more generic we have passes the complete changed Employee from UI
     * layer to Bussiness Layer so that all the data will be in sync
     * 
     * @param employee
     * @return
     */
    public String createEmployee(Employee employee) {
	String empId = "-1";

	JAXBContext jaxbContext;
	try {
	    jaxbContext = JAXBContext.newInstance(Employee.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    String urlPath = BASE_URL + CREATE_EMPLOYEE;

	    HttpURLConnection conn = connectEmployeeDataService(urlPath, OPERATION_POST);
	    conn.setRequestProperty("Content-Type", "application/xml");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    jaxbMarshaller.marshal(employee, conn.getOutputStream());

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
	return empId;
    }

    /**
     * This method will update the employee data in the file system As per make
     * is more generic we have passes the complete changed Employee from UI
     * layer to Bussiness Layer so that all the data will be in sync
     * 
     * @param employee
     * @return
     */
    public boolean updateEmployee(Employee employee) {
	boolean isTransactionSuccessFul = true;

	JAXBContext jaxbContext;
	try {
	    jaxbContext = JAXBContext.newInstance(Employee.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    String urlPath = BASE_URL + UPDATE_EMPLOYEE;

	    HttpURLConnection conn = connectEmployeeDataService(urlPath, OPERATION_POST);
	    conn.setRequestProperty("Content-Type", "application/xml");
	    conn.setDoOutput(true);
	    conn.setDoInput(true);
	    jaxbMarshaller.marshal(employee, conn.getOutputStream());

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
     * This method deals with delete of the Employee from the System. Clean up
     * of all the data linked with this employee Id is not implemented. Moreover
     * inspite of hard delete we can move this data to obsolete folder which
     * must have the same structure as current data structure had.
     * 
     * Orchestration of the Delete related views will be implemented from here
     * by invoking respective methods from other facades
     * 
     * @param empId
     * @return
     */
    public boolean deleteEmployee(String empId) {
	boolean isTransactionSuccesfull = true;
	String urlPath = BASE_URL + DELETE_EMPLOYEE + "/" + empId;
	try {
	    HttpURLConnection conn = connectEmployeeDataService(urlPath, OPERATION_DELETE);

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
	EmployeeFacade employeeFacade = new EmployeeFacade();
	// employeeFacade.deleteEmployee("1005");
	employeeFacade.updateEmployee(createDummyEmployeeData());
    }
}
// https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily