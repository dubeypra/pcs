package pcs.pradeep.dubey.com.utility;

/**
 * As per current functionality only India as a country and its states are
 * supported Later on as per need below mentioned method can be extended further
 * 
 * @author prdubey
 *
 */
public class AddressUtility {

	public final static String states[] = { "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh",
			"Assam", "Bihar", "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa",
			"Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala",
			"Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha",
			"Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttar Pradesh", "Uttarakhand",
			"West Bengal" };

	public final static String countries[] = { "India" }; // Till now only India country and its states are supported

	/**
	 * @param country
	 *            : Couuntry Name
	 * @return States of the country
	 */
	public static String[] getStates(String country) {
		return states;
	}

	/**
	 * @return List of all country
	 */
	public static String[] getCountries() {
		return countries;
	}
}
