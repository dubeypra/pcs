/**
 * 
 */
package pcs.pradeep.dubey.com.utility;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * This class contains generic methods which can be used for handling 
 * for Date conversion from one form to other form
 * 
 * ALl the method must be created static and no class level variable has to be defined in this
 * @author prdubey
 *
 */
public class DateTimeUtlity {

	/**
	 * @param inputDate Java Util Date
	 * @return XML Format Date
	 */
	public static XMLGregorianCalendar convertDateToXMLDate(Date inputDate) {
		System.out.println("Input Date is : "+inputDate);
		
		XMLGregorianCalendar xmlGregorianCalendar = null;
		GregorianCalendar gregorainCalendar = new GregorianCalendar();
		gregorainCalendar.setTime(inputDate);
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorainCalendar);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Converted Date is : "+xmlGregorianCalendar);
		
		return xmlGregorianCalendar;
	}
	
	/**
	 * @param inputDate XML Date
	 * @return XML Format Date
	 */
	public static Date convertXMLDateToDate(XMLGregorianCalendar inputDate) {
		System.out.println("Input Date is : "+inputDate);
		
		Date outputDate = inputDate.toGregorianCalendar().getTime();
		
		
		System.out.println("Converted Date is : "+outputDate);
		
		return outputDate;
	}
	
}
