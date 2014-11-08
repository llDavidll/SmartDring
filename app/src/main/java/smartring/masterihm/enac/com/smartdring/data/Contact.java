package smartring.masterihm.enac.com.smartdring.data;

import java.io.Serializable;

/**
 * Created by arnaud on 07/11/2014.
 */
public class Contact implements Serializable {

    private int mId;
    private String contactName;
    private String contactPhoneNumber;

    public Contact() {
        this.mId = -1;
        this.contactName = "";
        this.contactPhoneNumber = "";
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

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }
}
