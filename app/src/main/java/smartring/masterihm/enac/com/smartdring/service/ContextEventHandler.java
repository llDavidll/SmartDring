package smartring.masterihm.enac.com.smartdring.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.Contact;
import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.Profile;
import smartring.masterihm.enac.com.smartdring.data.SmartDringPreferences;

/**
 * Created by David on 29/10/2014.
 * <p/>
 * Handle context changes.
 */
public class ContextEventHandler implements ContextChangeDetector.ContextChangeInterface {

    // Unique Identification Number for the Notification.
    // We use it on Notification startDetection, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;

    // Notification builder used to update the notification.
    private NotificationCompat.Builder mNotifBuilder;
    // Notification manager used to display the notification.
    private NotificationManager mNotifManager;

    // Current state
    private ContextCurrentState mState;

    // Context used to apply external context changes.
    private SmartDringService mContext;

    /**
     * Create a context event handler.
     *
     * @param context the context used to apply the changes.
     */
    public ContextEventHandler(SmartDringService context) {
        mContext = context;
        mState = new ContextCurrentState();
        // Find default place
        for (Place place : mContext.getDB().getPlaces()) {
            if (place.isDefault()) {
                mState.currentPlace = place;
                mState.currentPlace.setName(context.getString(R.string.places_default_outdoor));
                mState.currentProfile = mContext.getDB().getProfile(mState.currentPlace.getAssociatedProfile());
                break;
            }
        }
        mNotifBuilder = new NotificationCompat.Builder(mContext);
        mNotifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        showNotification();
    }

    /**
     * Make sure we can apply changes.
     *
     * @param context the context used to apply the changes.
     */
    public void startContextHandle(SmartDringService context) {
        mContext = context;
    }

    /**
     * Make sur nothing stays behind when the service stops.
     */
    public void stopContextHandle() {
        mNotifManager.cancel(NOTIFICATION);
        mContext = null;
    }

    /**
     * Update the context handler according to new data.
     *
     * @param context the context used to apply the changes.
     */
    public void updateContextHandler(Context context) {
        if(mState.currentProfile != null) {
            mState.currentProfile = mContext.getDB().getProfile(mState.currentProfile.getId());
        }
        updateNotification();
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        Intent bIntent = new Intent(mContext, SmartDringActivity.class);
        PendingIntent pbIntent = PendingIntent.getActivity(mContext, 0, bIntent, 0);
        mNotifBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getCurrentProfileName())
                .setContentText(getNotifText(mState.currentPlace))
                .setOngoing(true)
                .setContentIntent(pbIntent);
        Notification barNotif = mNotifBuilder.build();
        mNotifManager.notify(NOTIFICATION, barNotif);
    }

    private void updateNotification() {
        mNotifBuilder.setContentTitle(getCurrentProfileName())
                .setContentText(getNotifText(mState.currentPlace));
        mNotifManager.notify(NOTIFICATION, mNotifBuilder.build());
    }

    // Get the text to display in the notification.
    private String getNotifText(Place place) {
        String result = mContext.getString(R.string.notification_current_place);
        if (place != null) {
            result += " " + place.getName();
        }
        return result;
    }

    // Get the title of the notification.
    private String getCurrentProfileName() {
        if (mState.currentProfile == null) {
            return mContext.getString(R.string.notification_no_profile);
        }
        return mState.currentProfile.getName();
    }


    @Override
    public void phoneFlippedStateChanged(boolean isPhoneFlipped) {
        if (isPhoneFlipped) {
            mState.isFlipped = true;
            Profile currentp = new Profile(mContext);
            mState.currentProfile = currentp;
            Profile pSilence = new Profile(Profile.SILENCE_MOD);
            pSilence.applySoundLevel(mContext);
        } else {
            Profile previousP = mState.currentProfile;
            if (previousP != null && mState.isFlipped) {
                mState.isFlipped = false;
                previousP.applySoundLevel(mContext);
            }
        }
        updateNotification();
    }

    @Override
    public void currentPlaceChanged(Place currentPlace) {
        mState.currentPlace = currentPlace;
        if(currentPlace != null){
            Profile placeProfile = mContext.getDB().getProfile(currentPlace.getAssociatedProfile());
            if (placeProfile != null) {
                mState.currentProfile = placeProfile;
            }
        }
        updateNotification();
    }

    @Override
    public void phoneCallStateChanged(String number, boolean ring) {
        if (ring) {
            mState.isOnCall = true;
            if (isInWhiteList(number)){
                // APPLY LOUD PROFILE
                mState.savedProfile = new Profile(mContext);
                mState.currentProfile = new Profile(Profile.LOUD_MOD);
                mState.currentProfile.applySoundLevel(mContext);
            }
            else if (isInBlackList(number)) {
                // APPLY SILENCE PROFILE
                mState.savedProfile = new Profile(mContext);
                mState.currentProfile = new Profile(Profile.SILENCE_MOD);
                mState.currentProfile.applySoundLevel(mContext);
            }
        } else {
            if (mState.savedProfile != null && mState.isOnCall) {
                mState.isOnCall = false;
                mState.currentProfile = mState.savedProfile;
                mState.savedProfile = null;
                mState.currentProfile.applySoundLevel(mContext);
            }
        }
    }


    private boolean isInBlackList(String number) {
        boolean isActivated = SmartDringPreferences.getBooleanPreference(mContext, SmartDringPreferences.BLACKLIST_STATE);
        if (!isActivated) {
            return false;
        }
        ArrayList<Contact> lc = (ArrayList<Contact>) mContext.getDB().getcontactList(false);
        for (Contact c : lc) {
            if (c.isTheSamePhoneNumber(number)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInWhiteList(String number) {
        boolean isActivated = SmartDringPreferences.getBooleanPreference(mContext, SmartDringPreferences.WHITELIST_STATE);
        if (!isActivated) {
            return false;
        }
        ArrayList<Contact> lc = (ArrayList<Contact>) mContext.getDB().getcontactList(true);
        for (Contact c : lc) {
            if (c.isTheSamePhoneNumber(number)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Current state of the context.
     */
    private static class ContextCurrentState {
        // Applied profile.
        private Profile currentProfile;
        // Saved profile
        private Profile savedProfile;

        // Current location of the device (if known).
        private Place currentPlace;
        // Current position of the phone (by default: not flipped).
        private boolean isFlipped;
        // On Call State
        private boolean isOnCall;
    }
}
