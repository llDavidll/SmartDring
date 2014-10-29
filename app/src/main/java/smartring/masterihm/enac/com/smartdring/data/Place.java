package smartring.masterihm.enac.com.smartdring.data;

import java.io.Serializable;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Data class, used to store a place in memory.
 */
public class Place implements Serializable {

    private int mId;
    private boolean mIsDefault;
    private String mName;
    private double mLatitude;
    private double mLongitude;
    /**
     * Id of the associated profile. If <0, then no profile is associated to this place.
     */
    private int mAssociatedProfile;

    public Place() {
        mId = 0;
        mIsDefault = false;
        mName = "";
        mLatitude = 0;
        mLongitude = 0;
        mAssociatedProfile = -1;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public boolean isDefault() {
        return mIsDefault;
    }

    public void setDefault(boolean isDefault) {
        mIsDefault = isDefault;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public int getAssociatedProfile() {
        return mAssociatedProfile;
    }

    public void setAssociatedProfile(int associatedProfile) {
        mAssociatedProfile = associatedProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            return ((Place) o).getId() == getId();
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(getId()).hashCode();
    }
}
