package smartring.masterihm.enac.com.smartdring.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 15/10/2014.
 * <p/>
 * Shared preferences of SmartRing. Used to store simple data.
 */
public abstract class SmartDringPreferences {

    public static final String SMARTRING_STATE = "SMARTRING_STATE";
    public static final String PHONEFLIP_STATE = "PHONEFLIP_STATE";
    public static final String WHITELIST_STATE = "WHITELIST_STATE";
    public static final String BLACKLIST_STATE = "BLACKLIST_STATE";

    private static final Map<String, Boolean> DEFAULT_VALUES = new HashMap<String, Boolean>();

    private static SharedPreferences getSP(Context context) {
        if (DEFAULT_VALUES.isEmpty()) {
            DEFAULT_VALUES.put(SMARTRING_STATE, true);
            DEFAULT_VALUES.put(PHONEFLIP_STATE, true);
            DEFAULT_VALUES.put(WHITELIST_STATE, false);
            DEFAULT_VALUES.put(BLACKLIST_STATE, false);
        }
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static void setBooleanPreference(Context context, String functionality, boolean isActivated) {
        getSP(context).edit().putBoolean(functionality, isActivated).apply();
    }

    public static boolean getBooleanPreference(Context context, String functionality) {
        return getSP(context).getBoolean(functionality, DEFAULT_VALUES.get(functionality));
    }
}
