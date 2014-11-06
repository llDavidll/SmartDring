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

    private int mPhoneLvl;
    private int mNotifLvl;
    private int mMediaLvl;
    private int mCallLvl;
    private int mAlarmLvl;

    public Profile() {
        mId = -1;
        mIsDefault = false;
        mName = "New";
        mColor = Color.DKGRAY;
        mPhoneLvl = 3;
        mNotifLvl = 3;
        mMediaLvl = 3;
        mCallLvl = 3;
        mAlarmLvl = 3;
        
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

    public int getmPhoneLvl() {
        return mPhoneLvl;
    }

    public void setmPhoneLvl(int mPhoneLvl) {
        this.mPhoneLvl = mPhoneLvl;
    }

    public int getmNotifLvl() {
        return mNotifLvl;
    }

    public void setmNotifLvl(int mNotifLvl) {
        this.mNotifLvl = mNotifLvl;
    }

    public int getmMediaLvl() {
        return mMediaLvl;
    }

    public void setmMediaLvl(int mMediaLvl) {
        this.mMediaLvl = mMediaLvl;
    }

    public int getmCallLvl() {
        return mCallLvl;
    }

    public void setmCallLvl(int mCallLvl) {
        this.mCallLvl = mCallLvl;
    }

    public int getmAlarmLvl() {
        return mAlarmLvl;
    }

    public void setmAlarmLvl(int mAlarmLvl) {
        this.mAlarmLvl = mAlarmLvl;
    }
}
