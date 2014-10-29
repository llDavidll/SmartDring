package smartring.masterihm.enac.com.smartdring.data;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Data class, used to store a profile in memory.
 */
public class Profile implements Serializable {

    private int mId;
    private boolean mIsDefault;
    private String mName;
    private int mColor;

    public Profile() {
        mId = 0;
        mIsDefault = false;
        mName = "";
        mColor = Color.WHITE;
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

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
