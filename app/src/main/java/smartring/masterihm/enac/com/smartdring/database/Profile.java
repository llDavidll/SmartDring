package smartring.masterihm.enac.com.smartdring.database;

import android.graphics.Color;

import java.io.Serializable;

/**
 * Created by David on 13/10/2014.
 *
 * Data class, used to store a profile in memory.
 */
public class Profile implements Serializable {

    private int mId;
    private String mName;
    private int mColor;

    public Profile() {
        mId = 0;
        mName = "";
        mColor = Color.WHITE;
    }

    public void setId(int id) {
        mId = id;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public int getColor() {
        return mColor;
    }
}
