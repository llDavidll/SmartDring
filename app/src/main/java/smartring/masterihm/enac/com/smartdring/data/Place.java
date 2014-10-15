package smartring.masterihm.enac.com.smartdring.data;

import java.io.Serializable;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Data class, used to store a place in memory.
 */
public class Place implements Serializable {

    private int mId;
    private String mName;
    /**
     * Id of the associated profile. If <0, then no profile is associated to this place.
     */
    private int mAssociatedProfile;

    public Place() {
        mId = 0;
        mName = "";
        mAssociatedProfile = -1;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setAssociatedProfile(int associatedProfile) {
        mAssociatedProfile = associatedProfile;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public int getAssociatedProfile() {
        return mAssociatedProfile;
    }
}
