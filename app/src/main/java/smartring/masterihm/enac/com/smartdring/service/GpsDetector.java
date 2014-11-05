package smartring.masterihm.enac.com.smartdring.service;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

/**
 * Created by David on 21/10/2014.
 * <p/>
 * Class used to access the location of the phone.
 */
public class GpsDetector {

    // Key used to identify the place id in the broadcast intent.
    private final static String BROADCAST_KEY_PLACE_ID = "PLACE_ID";

    // Localisation precision around each GPS position (meters)
    private final static int LOCALISATION_PRECISION = 60;

    // Broadcast receiver to receive proximity alerts.
    private BroadcastReceiver mBroadcast;
    // Location manager used to access the GPS functionnalities.
    private LocationManager mLocationManager;

    // Map of all locations linked with the intent they would trigger.
    private Map<Place, PendingIntent> mLocations;

    // The default location
    private Place mDefaultPlace;

    // Listener receiving the localisation updates.
    private ContextChangeDetector.ContextChangeInterface mGpsDetectorListener;

    // Is the GpsDetector running.
    private boolean isRunning;

    // How many zones is the phone in.
    private int inZone;

    /**
     * Creates a new GpsDetector.
     *
     * @param context  the context requesting the GpsDetector.
     * @param listener the listener requesting localisation updates.
     */
    public GpsDetector(Context context, ContextChangeDetector.ContextChangeInterface listener) {

        mGpsDetectorListener = listener;

        mBroadcast = new ProximityIntentReceiver();

        // When starting, we are not in any zone.
        inZone = 0;

        // Acquire a reference to the system Location Manager
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        IntentFilter filter = new IntentFilter(context.getPackageName());
        context.registerReceiver(mBroadcast, filter);

        mLocations = new HashMap<Place, PendingIntent>(3);
        // Here, create the pending intent for each place in the "places" from the database.
        refreshLocations(context);

    }

    /**
     * Refresh the list of locations from the database.
     *
     * @param context the context requesting the refresh.
     */
    public void refreshLocations(Context context) {
        List<Place> newList = SmartDringDB.getDatabase(SmartDringDB.SERVICE_DB).getPlaces(true);
        List<Place> oldList = new ArrayList<Place>(mLocations.keySet());

        // Set the default place
        for (Place place : newList) {
            if (place.isDefault()) {
                mDefaultPlace = place;
                mDefaultPlace.setName(context.getString(R.string.places_default_outdoor));
                break;
            }
        }
        // Remove the proximity alerts for the location not in the database anymore.
        for (Place oldPlace : oldList) {
            if (!oldPlace.isDefault()) {
                if (!newList.contains(oldPlace)) {
                    mLocationManager.removeProximityAlert(mLocations.get(oldPlace));
                    mLocations.remove(oldPlace);
                }
            }
        }

        // Add the new proximity alert for the new locations in the database.
        for (Place newPlace : newList) {
            if (!newPlace.isDefault()) {
                if (!mLocations.containsKey(newPlace)) {
                    Intent intent = new Intent(context.getPackageName());
                    intent.putExtra(BROADCAST_KEY_PLACE_ID, newPlace.getId());
                    PendingIntent proximityIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    mLocations.put(newPlace, proximityIntent);
                    if (isRunning) {
                        mLocationManager.addProximityAlert(newPlace.getLatitude(), newPlace.getLongitude(), LOCALISATION_PRECISION, -1, mLocations.get(newPlace));
                    }
                }
            }
        }
    }

    /**
     * Start the gps detection.
     */
    public void startDetection() {
        if (!isRunning) {
            for (Place place : mLocations.keySet()) {
                mLocationManager.addProximityAlert(place.getLatitude(), place.getLongitude(), LOCALISATION_PRECISION, -1, mLocations.get(place));
            }
            isRunning = true;
        }
    }

    /**
     * Stop the detection.
     *
     * @param context the context requesting the stop.
     */
    public void stopDetection(Context context) {
        if (isRunning) {
            for (PendingIntent intent : mLocations.values()) {
                mLocationManager.removeProximityAlert(intent);
            }
            context.unregisterReceiver(mBroadcast);
            isRunning = false;
        }
    }

    /**
     * A place was entered by the user.
     *
     * @param placeEntered the place that was entered.
     */
    private void placeEntered(Place placeEntered) {
        inZone++;
        mGpsDetectorListener.currentPlaceChanged(placeEntered);
    }

    /**
     * A place was exited by the user.
     *
     * @param placeExited the place that was exited.
     */
    private void placeExited(Place placeExited) {
        if (inZone > 0) {
            inZone--;
        }
        if (inZone == 0) {
            // The user is in no place he defined. Notify the listener that he is out of range of every place defined.
            mGpsDetectorListener.currentPlaceChanged(mDefaultPlace);
        }
    }

    /**
     * The broadcast receiver used to receive proximity alerts.
     */
    public class ProximityIntentReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean entering = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
            int placeId = intent.getIntExtra(BROADCAST_KEY_PLACE_ID, 0);
            Place triggeringPlace = null;
            for (Place place : mLocations.keySet()) {
                if (place.getId() == placeId) {
                    triggeringPlace = place;
                    break;
                }
            }
            if (triggeringPlace != null) {
                if (entering) {
                    placeEntered(triggeringPlace);
                } else {
                    placeExited(triggeringPlace);
                }
            }
        }
    }
}
