package smartring.masterihm.enac.com.smartdring.service;

import android.content.Context;

import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.SmartDringPreferences;

/**
 * Created by David on 29/10/2014.
 * <p/>
 * Handle the context changes and apply the correct profile.
 */
public class ContextChangeDetector {
    // Sensor detector used to detect the phone flip.
    private SensorDetector mSensorDetector;

    // Register for gps proximity alerts
    private GpsDetector mGpsDetector;

    private IncallListener phoneListener;


    public ContextChangeDetector(SmartDringService context, ContextChangeInterface pListener) {
        // Init context detection
        mSensorDetector = new SensorDetector(context, pListener);
        mGpsDetector = new GpsDetector(context, pListener);
        phoneListener = new IncallListener(context, pListener);
    }

    public void startContextDetection(Context context) {
        if (SmartDringPreferences.getBooleanPreference(context, SmartDringPreferences.PHONEFLIP_STATE)) {
            mSensorDetector.startDetection();
        } else {
            mSensorDetector.stopDetection();
        }

        mGpsDetector.startDetection();

        if (SmartDringPreferences.getBooleanPreference(context, SmartDringPreferences.WHITELIST_STATE) ||
            SmartDringPreferences.getBooleanPreference(context, SmartDringPreferences.BLACKLIST_STATE) ) {
            phoneListener.startDetection();
        } else {
            phoneListener.stopDetection();
        }
    }

    public void stopContextDetection(Context context) {
        mSensorDetector.stopDetection();
        mGpsDetector.stopDetection(context);
    }

    public void updateContextDetection(SmartDringService context) {
        if (SmartDringPreferences.getBooleanPreference(context, SmartDringPreferences.PHONEFLIP_STATE)) {
            mSensorDetector.startDetection();
        } else {
            mSensorDetector.stopDetection();
        }
        if (SmartDringPreferences.getBooleanPreference(context, SmartDringPreferences.WHITELIST_STATE) ||
                SmartDringPreferences.getBooleanPreference(context, SmartDringPreferences.BLACKLIST_STATE) ) {
            phoneListener.startDetection();
        } else {
            phoneListener.stopDetection();
        }
        mGpsDetector.refreshLocations(context);
    }

    /**
     * Interface used to receive context change notification.
     */
    public interface ContextChangeInterface {
        /**
         * Called when the phone flipped state changes.
         *
         * @param isPhoneFlipped is the phone flipped.
         */
        void phoneFlippedStateChanged(boolean isPhoneFlipped);

        /**
         * The current place has changed.
         *
         * @param currentPlace the new place the user is in.
         */
        void currentPlaceChanged(Place currentPlace);

        /**
         * Called when the phone call state changes.
         */
        void phoneCallStateChanged(String number, boolean ring);
    }
}
