package smartring.masterihm.enac.com.smartdring.data;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.support.v4.app.FragmentActivity;

import java.io.Serializable;

import smartring.masterihm.enac.com.smartdring.SmartDringActivity;

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
        mName = "";
        mColor = Color.DKGRAY;
        mPhoneLvl = 3;
        mNotifLvl = 3;
        mMediaLvl = 3;
        mCallLvl = 3;
        mAlarmLvl = 3;
        
    }

    // create profile with current sound level of the phone
    public Profile(Context c) {
        mId = -1;
        mIsDefault = false;
        mName = "";
        mColor = Color.DKGRAY;
        AudioManager am = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        mPhoneLvl = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
        mNotifLvl = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        mMediaLvl = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        mCallLvl = am.getStreamVolume(AudioManager.STREAM_RING);
        mAlarmLvl = am.getStreamVolume(AudioManager.STREAM_ALARM);
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
