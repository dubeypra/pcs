/**
 * 
 */
package telestra;

/**
 * @author prdubey
 * 
 */
public class TscConnectionDetails implements Comparable<TscConnectionDetails> {

    private String displayName;
    private String displayNetworkName;

    private char freIsManaged;
    private char uiInProvisioning;

    private String customer;
    private String label;

    private long freId;

    public long getFreId() {
	return freId;
    }

    public void setFreId(long freId) {
	this.freId = freId;
    }

    private String freKey;
    private String connectionKey;

    public String getConnectionKey() {
	return connectionKey;
    }

    public void setConnectionKey(String connectionKey) {
	this.connectionKey = connectionKey;
    }

    public String getFreKey() {
	return freKey;
    }

    public void setFreKey(String freKey) {
	this.freKey = freKey;
    }

    public String getCustomer() {
	return customer;
    }

    public void setCustomer(String customer) {
	if (customer == null) {
	    this.customer = "";
	} else {
	    this.customer = customer;
	}

    }

    public String getLabel() {
	return label;
    }

    public void setLabel(String label) {
	if (label == null) {
	    this.label = "";
	} else {
	    this.label = label;
	}
    }

    public String getDisplayName() {

	return displayName;
    }

    public void setDisplayName(String displayName) {
	if (displayName == null) {
	    this.displayName = "";
	} else {
	    this.displayName = displayName;
	}
	this.displayName = displayName;
    }

    public String getDisplayNetworkName() {
	return displayNetworkName;
    }

    public void setDisplayNetworkName(String displayNetworkName) {
	if (displayNetworkName == null) {
	    this.displayNetworkName = "";
	} else {
	    this.displayNetworkName = displayNetworkName;
	}

    }

    public char getFreIsManaged() {
	return freIsManaged;
    }

    public void setFreIsManaged(char freIsManaged) {
	this.freIsManaged = freIsManaged;
    }

    public char getUiInProvisioning() {
	return uiInProvisioning;
    }

    public void setUiInProvisioning(char uiInProvisioning) {
	this.uiInProvisioning = uiInProvisioning;
    }

    @Override
    public String toString() {
	return "TscConnectionDetails [displayName=" + displayName + ", displayNetworkName=" + displayNetworkName + ", freIsManaged=" + freIsManaged + ", uiInProvisioning=" + uiInProvisioning
	        + ", customer=" + customer + ", label=" + label + ", freId=" + freId + ", freKey=" + freKey + ", connectionKey=" + connectionKey + "]";
    }

    @Override
    public int compareTo(TscConnectionDetails dst) {
	int result = -1;

	if (getCustomer().equalsIgnoreCase(dst.getCustomer()) && getLabel().equalsIgnoreCase(dst.getLabel()) && getDisplayName().equalsIgnoreCase(dst.getDisplayName())
	        && getDisplayNetworkName().equalsIgnoreCase(dst.getDisplayNetworkName()) && getFreIsManaged() == dst.getFreIsManaged() && getUiInProvisioning() == dst.getUiInProvisioning()) {

	    result = 0;
	}
	return result;
    }

}
