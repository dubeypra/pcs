/**
 * 
 */
package pcs.pradeep.dubey.com.ui.facade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import pcs.pradeep.dubey.com.baseentity.AddressDetails;
import pcs.pradeep.dubey.com.baseentity.CommunicationDetails;
import pcs.pradeep.dubey.com.baseentity.PersonalDetails;
import pcs.pradeep.dubey.com.employee.Designation;
import pcs.pradeep.dubey.com.employee.Employee;

/**
 * @author prdubey
 *
 */
public abstract class Facade {

    /**
     * Common method to establish the connection
     * 
     * @param urlPath
     *            : Web Service path
     * @return: Connection
     * @throws MalformedURLException
     * @throws IOException
     * @throws ProtocolException
     * @throws RuntimeException
     */
    public HttpURLConnection connectEmployeeDataService(String urlPath, String requestMethod)
	    throws MalformedURLException, IOException, ProtocolException, RuntimeException {
	URL url = new URL(urlPath);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod(requestMethod);
	conn.setRequestProperty("Accept", "application/xml");
	return conn;
    }

    /**
     * @param conn
     * @return
     * @throws IOException
     */
    public String readResponse(HttpURLConnection conn) throws IOException {
	BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	String inputLine;
	StringBuffer response = new StringBuffer();

	while ((inputLine = in.readLine()) != null) {
	    response.append(inputLine);
	}
	in.close();

	int startIndex = response.indexOf(">");
	int endIndex = response.indexOf("</result>");
	String effectiveResult = response.substring(startIndex + 1, endIndex);
	return effectiveResult;
    }

    /**
     * @param conn
     * @throws IOException
     * @throws RuntimeException
     */
    public void isServiceCallSuccessful(HttpURLConnection conn) throws IOException, RuntimeException {
	if (conn.getResponseCode() != 200) {
	    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
	}
    }

    /**
     * @return dummy test Data
     */
    public static Employee createDummyEmployeeData() {
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
	employee.setEmpId("1011");

	PersonalDetails personalDetails = new PersonalDetails();
	// personalDetails.setDob(GregorianCalendar.getInstance().getTime());
	personalDetails.setFirstName("Pradeep");
	personalDetails.setLastName("Dubey");
	personalDetails.setMiddleName("");
	employee.setPersonalDetails(personalDetails);

	return employee;

    }

}
