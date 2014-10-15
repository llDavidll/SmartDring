package smartring.masterihm.enac.com.smartdring.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by David on 15/10/2014.
 * <p/>
 * Shared preferences of SmartRing. Used to store simple data.
 */
public abstract class SmartRingPreferences {

    public static final String SMARTRING_STATE = "SMARTRING_STATE";
    public static final String PHONEFLIP_STATE = "PHONEFLIP_STATE";
    public static final String WHITELIST_STATE = "WHITELIST_STATE";
    public static final String BLACKLIST_STATE = "BLACKLIST_STATE";

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }
    public static void setBooleanPreference(Context context, String functionality, boolean isActivated) {
        getSP(context).edit().putBoolean(functionality, isActivated).apply();
    }

    public static boolean getBooleanPreference(Context context, String functionality, boolean defaultValue) {
        return getSP(context).getBoolean(functionality, defaultValue);
    }
}
