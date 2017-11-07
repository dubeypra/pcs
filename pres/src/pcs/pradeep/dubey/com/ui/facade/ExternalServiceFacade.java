package pcs.pradeep.dubey.com.ui.facade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import pcs.pradeep.dubey.com.baseentity.AddressDetails;

public class ExternalServiceFacade extends Facade {
	public final static String FETCH_PIN_VALIDATION_URL = "http://postalpincode.in/api/pincode/";

	/**
	 * Based on the Pin code Provided , check that valid address or not
	 * 
	 * Success Record : {"Message":"Number of Post office(s) found:
	 * 7","Status":"Success","PostOffice":[{"Name":"Gandhi Nagar (East
	 * Delhi)","Description":"","BranchType":"Sub Post
	 * Office","DeliveryStatus":"Delivery","Taluk":"NA","Circle":"NA","District":"East
	 * Delhi","Division":"Delhi
	 * East","Region":"Delhi","State":"Delhi","Country":"India"},{"Name":"Gandhi
	 * Nagar Bazar","Description":"","BranchType":"Sub Post
	 * Office","DeliveryStatus":"Non-Delivery","Taluk":"NA","Circle":"NA","District":"East
	 * Delhi","Division":"Delhi
	 * East","Region":"Delhi","State":"Delhi","Country":"India"},{"Name":"Geeta
	 * Colony","Description":"","BranchType":"Sub Post
	 * Office","DeliveryStatus":"Non-Delivery","Taluk":"NA","Circle":"NA","District":"East
	 * Delhi","Division":"Delhi
	 * East","Region":"Delhi","State":"Delhi","Country":"India"},{"Name":"Kailash
	 * Nagar","Description":"","BranchType":"Sub Post
	 * Office","DeliveryStatus":"Non-Delivery","Taluk":"NA","Circle":"NA","District":"East
	 * Delhi","Division":"Delhi
	 * East","Region":"Delhi","State":"Delhi","Country":"India"},{"Name":"Raghubar
	 * Pura","Description":"","BranchType":"Sub Post
	 * Office","DeliveryStatus":"Non-Delivery","Taluk":"NA","Circle":"NA","District":"East
	 * Delhi","Division":"Delhi
	 * East","Region":"Delhi","State":"Delhi","Country":"India"},{"Name":"Rajgarh
	 * Colony","Description":"","BranchType":"Sub Post
	 * Office","DeliveryStatus":"Non-Delivery","Taluk":"NA","Circle":"NA","District":"East
	 * Delhi","Division":"Delhi
	 * East","Region":"Delhi","State":"Delhi","Country":"India"},{"Name":"Shastri
	 * Nagar (East Delhi)","Description":"","BranchType":"Sub Post
	 * Office","DeliveryStatus":"Non-Delivery","Taluk":"NA","Circle":"NA","District":"East
	 * Delhi","Division":"Delhi
	 * East","Region":"Delhi","State":"Delhi","Country":"India"}]}
	 * 
	 * 
	 * Error Record : {"Message":"No records
	 * found","Status":"Error","PostOffice":null}
	 * 
	 * @param addressDetails
	 *            : Address Details must contains all the parameters which required
	 *            for validation
	 * @return True if provided address and pin code matched
	 */
	public static boolean getAddressBasedOnPin(AddressDetails addressDetails) {

		if (addressDetails.getCountry() == null || addressDetails.getPinCode() == null
				|| addressDetails.getState() == null)
			return false;

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(FETCH_PIN_VALIDATION_URL);
		WebResource msgService = service.path(addressDetails.getPinCode());
		String msg = msgService.accept(MediaType.APPLICATION_JSON).get(String.class);
		System.out.println(msg);

		Map<String, Object> retMap = new Gson().fromJson(msg, new TypeToken<HashMap<String, Object>>() {
		}.getType());

		System.out.println("Converted Map is : " + retMap);

		String status = (String) retMap.get("Status");

		if (!status.equalsIgnoreCase("Success")) {
			return false; // Not a valid Pin code
		}

		ArrayList postOfficeList = (ArrayList) retMap.get("PostOffice");

		String country;
		String state;

		for (Object object : postOfficeList) {
			Map postOffice = ((Map) object);

			country = (String) postOffice.get("Country");
			state = (String) postOffice.get("State");

			if (addressDetails.getCountry().equalsIgnoreCase(country)
					&& addressDetails.getState().equalsIgnoreCase(state)) {
				return true;
			}

		}
		return false;
	}

	public static void main(String args[]) {
		AddressDetails addressDetails = new AddressDetails();
		addressDetails.setPinCode("122002");
		addressDetails.setCountry("India");
		addressDetails.setState("Delhi");
		System.out.println("Result is : " + getAddressBasedOnPin(addressDetails));
	}

}
