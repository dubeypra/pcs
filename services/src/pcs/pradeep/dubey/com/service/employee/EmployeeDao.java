/**
 * 
 */
package pcs.pradeep.dubey.com.service.employee;

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

import pcs.pradeep.dubey.com.baseentity.AddressDetails;
import pcs.pradeep.dubey.com.baseentity.CommunicationDetails;
import pcs.pradeep.dubey.com.baseentity.PersonalDetails;
import pcs.pradeep.dubey.com.employee.Designation;
import pcs.pradeep.dubey.com.employee.Employee;
import pcs.pradeep.dubey.com.employee.EmployeeList;
import pcs.pradeep.dubey.com.service.utils.ApplicationConstants;
import pcs.pradeep.dubey.com.service.utils.DataFileFolderLocations;
import pcs.pradeep.dubey.com.service.utils.Utility;

/**
 * This class will deals with the file handling related to the Employees
 * 
 * @author prdubey
 *
 */
public class EmployeeDao {

	enum Service {
		INSTANCE;

		/**
		 * This map will populate when all the employess are loaded
		 */
		private HashMap<String, Employee> employeeMap;

		public static String UPDATE_EMPLOYEE_ID = "999999";
		public static String CREATE_EMPLOYEE_ID = "888888";
		public static String INTERMEDIATE_TEMP_FILE_IDENTIFIER = "TEMP_FILE";
		public static String INTERMEDIATE_EMPLOYEE_IDENTIFIER = "EMPLOYEE";

		/**
		 * This counter is used to track the employee Id of last used
		 */
		private static int lastUsedEmployeeId;

		private Service() {

			employeeMap = new HashMap<String, Employee>();
			try {
				loadEmployeeData();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Used to retrieve all the employees stored in the system
		 * 
		 * @return list of the Employee Object
		 */
		private List<Employee> loadEmployeeData() throws JAXBException {
			List<Employee> employeeList = new ArrayList<Employee>();
			List<String> employeePathList = Utility
					.listFilesForFolder(new File(DataFileFolderLocations.getEmployeeDataPath()));
			JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			for (String fileLocation : employeePathList) {
				Employee employee = (Employee) (jaxbUnmarshaller.unmarshal(new File(fileLocation)));
				if (employee != null) {
					int fetchedEmployeeId = Integer.parseInt(employee.getEmpId());
					lastUsedEmployeeId = lastUsedEmployeeId < fetchedEmployeeId ? fetchedEmployeeId
							: lastUsedEmployeeId;
					employeeList.add(employee);
					employeeMap.put(employee.getEmpId(), employee);
				}
			}
			return employeeList;
		}

		/**
		 * Get all the Employees data
		 * 
		 * @return
		 * @throws JAXBException
		 */
		public EmployeeList getAllEmployee() throws JAXBException {
			EmployeeList employeeList = new EmployeeList();
			Collection<Employee> emplist = employeeMap.values();
			for (Employee employee : emplist) {
				employeeList.getEmployeeList().add(employee);
			}
			return employeeList;
		}

		/**
		 * Used to retrieve all the employees stored in the system
		 * 
		 * @return Employee Value Can be Null
		 */
		public Employee getEmployee(String employeeId) throws JAXBException {
			Employee employee = null;
			employee = employeeMap.get(employeeId);
			if (employee == null) {
				try {

					JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
					Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
					String fileLocation = DataFileFolderLocations.getEmployeeDataPath() + employeeId
							+ DataFileFolderLocations.FILE_EXTENSION;
					employee = (Employee) (jaxbUnmarshaller.unmarshal(new File(fileLocation)));

				} catch (JAXBException e) {
					throw e;
				}
			}
			return employee;
		}

		/**
		 * This method used to create the new Employee in the System Employee Id
		 * Generarted by the system
		 * 
		 * @param employee
		 *            Employee
		 * @return EmployeeID Created by System
		 */
		public String createEmployee(String employeeData) {
			if (lastUsedEmployeeId == 0) {
				lastUsedEmployeeId = ApplicationConstants.EMPLOYEE_ID_OFFSET;
			}
			String employeeId = String.valueOf(++lastUsedEmployeeId);
			Employee employee = null;

			try {
				HashMap intermediateProcessMap = handleEmployeeStream(employeeData, CREATE_EMPLOYEE_ID);
				employee = (Employee) intermediateProcessMap.get(INTERMEDIATE_EMPLOYEE_IDENTIFIER);
				File tempFile = (File) intermediateProcessMap.get(INTERMEDIATE_TEMP_FILE_IDENTIFIER);
				employee.setEmpId(employeeId);

				String fileLocation = DataFileFolderLocations.getEmployeeDataPath() + employeeId
						+ DataFileFolderLocations.FILE_EXTENSION;

				JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
				Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				jaxbMarshaller.marshal(employee, new FileOutputStream(fileLocation));
				tempFile.delete();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			if (employee != null)
				employeeMap.put(employeeId, employee);

			return employeeId;

		}

		/**
		 * This method update the existing employee details in the system
		 * 
		 * @param employee
		 *            {@link Employee}
		 * @return True for Success
		 */
		public boolean updateEmployee(String employeeData) {
			boolean isTransactionSuccess = true;

			Employee employee = null;
			try {

				HashMap intermediateProcessMap = handleEmployeeStream(employeeData, UPDATE_EMPLOYEE_ID);
				employee = (Employee) intermediateProcessMap.get(INTERMEDIATE_EMPLOYEE_IDENTIFIER);
				File tempFile = (File) intermediateProcessMap.get(INTERMEDIATE_TEMP_FILE_IDENTIFIER);

				String fileLocation = DataFileFolderLocations.getEmployeeDataPath() + employee.getEmpId()
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
			if (employee != null)
				employeeMap.put(employee.getEmpId(), employee);

			return isTransactionSuccess;
		}

		/**
		 * Delete the data of the particular employee from the system
		 * 
		 * @param empId
		 * @return
		 */
		public boolean deleteEmployee(String empId) {
			String fileLocation = DataFileFolderLocations.getEmployeeDataPath() + empId
					+ DataFileFolderLocations.FILE_EXTENSION;
			File file = new File(fileLocation);
			boolean isTransactionSuccesfull = file.delete();

			if (isTransactionSuccesfull) {
				employeeMap.remove(empId);
			}
			return isTransactionSuccesfull;
		}

		/**
		 * @param employeeData
		 * @return
		 * @throws IOException
		 * @throws JAXBException
		 */
		private HashMap handleEmployeeStream(String employeeData, String employeeId) throws IOException, JAXBException {
			HashMap intermediateMap = new HashMap<>();
			Employee employee;
			String tempFileLocation = DataFileFolderLocations.getEmployeeDataPath() + employeeId
					+ DataFileFolderLocations.FILE_EXTENSION;
			Utility.stringToDom(employeeData, tempFileLocation);

			JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			File tempFile = new File(tempFileLocation);
			employee = (Employee) (jaxbUnmarshaller.unmarshal(tempFile));

			intermediateMap.put(INTERMEDIATE_TEMP_FILE_IDENTIFIER, tempFile);
			intermediateMap.put(INTERMEDIATE_EMPLOYEE_IDENTIFIER, employee);
			return intermediateMap;
		}
	}

	/*    *//**
			 * Devloped for Testing purpose only
			 * 
			 * @param args
			 *//*
				 * public static void main(String args[]) { for (int i = 0; i < 3; i++) {
				 * Service.INSTANCE.createEmployee(createDummyEmployeeData()); }
				 * 
				 * 
				 * try {
				 * 
				 * List<Employee> employeeList = dao.getAllEmployees();
				 * System.out.println("Test Ok"); } catch (JAXBException e) {
				 * e.printStackTrace(); }
				 * 
				 * 
				 * }
				 */

	/**
	 * @return dummy test Data
	 */
	private static Employee createDummyEmployeeData() {
		Employee employee = new Employee();
		AddressDetails addressDetails = new AddressDetails();
		addressDetails.setCity("Delhi");
		addressDetails.setCountry("India");
		addressDetails.setPinCode("11001");
		addressDetails.setPrimaryAddress("HN 2388");
		addressDetails.setState("Delhi");

		employee.setAddressDetails(addressDetails);

		CommunicationDetails communicationDetails = new CommunicationDetails();
		communicationDetails.setEmailId("xyz@gmil.com");
		communicationDetails.setLandlineNo("123445");
		communicationDetails.setMobileNo("3523464");
		employee.setCommunicationDetails(communicationDetails);
		employee.setDesignation(Designation.ADMIN);
		employee.setEmpId("97512");

		PersonalDetails personalDetails = new PersonalDetails();
		// personalDetails.setDob(GregorianCalendar.getInstance().getTime());
		personalDetails.setFirstName("Pradeep");
		personalDetails.setLastName("Dubey");
		personalDetails.setMiddleName("");
		employee.setPersonalDetails(personalDetails);

		return employee;

	}

}
