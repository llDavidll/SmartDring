package smartring.masterihm.enac.com.smartdring.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import smartring.masterihm.enac.com.smartdring.SmartDringActivity;

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

    private static final Executor executor = Executors.newSingleThreadExecutor();

    private static SharedPreferences getSP(Context context) {
        if (DEFAULT_VALUES.isEmpty()) {
            DEFAULT_VALUES.put(SMARTRING_STATE, true);
            DEFAULT_VALUES.put(PHONEFLIP_STATE, true);
            DEFAULT_VALUES.put(WHITELIST_STATE, false);
            DEFAULT_VALUES.put(BLACKLIST_STATE, false);
        }
        return context.getSharedPreferences("smartdring.masterihm.enac.com.smartdring", Context.MODE_MULTI_PROCESS);
    }

    public static void setBooleanPreference(final SmartDringActivity activity, final String functionality, final boolean isActivated) {
        // Execute one change after the other to keep the user action in the correct order.
        // Do this in a different thread because commit() induces a lag.
        // commit is necessary to apply the change immediatly so the service can access them.
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getSP(activity).edit().putBoolean(functionality, isActivated).commit();
                if (SMARTRING_STATE.equals(functionality)) {
                    if (isActivated) {
                        activity.getService().bindActivityToService();
                    } else {
                        activity.getService().stopService();
                    }
                } else {
                    activity.getService().preferencesUpdated();
                }
            }
        });
    }

    public static boolean getBooleanPreference(Context context, String functionality) {
        return getSP(context).getBoolean(functionality, DEFAULT_VALUES.get(functionality));
    }
}
