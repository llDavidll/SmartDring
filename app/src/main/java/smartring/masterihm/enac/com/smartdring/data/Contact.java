package smartring.masterihm.enac.com.smartdring.data;

import java.io.Serializable;

/**
 * Created by arnaud on 07/11/2014.
 */
public class Contact implements Serializable {

    private String contactName;
    private String contactPhoneNumber;

    public Contact(String contactName, String contactPhoneNumber) {
        this.contactName = contactName;
        this.contactPhoneNumber = contactPhoneNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhoneNumber() {
        return contactPhoneNumber;
    }

    public void setContactPhoneNumber(String contactPhoneNumber) {
        this.contactPhoneNumber = contactPhoneNumber;
    }
}
