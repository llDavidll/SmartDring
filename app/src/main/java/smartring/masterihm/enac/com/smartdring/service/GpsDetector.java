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

import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

/**
 * Created by David on 21/10/2014.
 */
public class GpsDetector {

    private final static String BROADCAST_KEY_PLACE_ID = "PLACE_ID";
    private final static int LOCALISATION_PRECISION = 50;
    private int inZone;
    private BroadcastReceiver mBroadcast;
    private LocationManager mLocationManager;

    private GpsDetectorInterface mGpsDetectorListener;

    private boolean isRunning;

    private Map<Place, PendingIntent> mLocations;

    public GpsDetector(Context context, GpsDetectorInterface listener) {

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

    public void refreshLocations(Context context) {
        // Here, create the pending intent for each place in the "places" from the database.
        List<Place> newList = SmartDringDB.getDatabase(SmartDringDB.SERVICE_DB).getPlaces();
        List<Place> oldList = new ArrayList<Place>(mLocations.keySet());

        for (Place oldPlace : oldList) {
            if (!newList.contains(oldPlace)) {
                mLocationManager.removeProximityAlert(mLocations.get(oldPlace));
                mLocations.remove(oldPlace);
            }
        }

        for (Place newPlace : newList) {
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

    public void startDetection() {
        if (!isRunning) {
            for (Place place : mLocations.keySet()) {
                mLocationManager.addProximityAlert(place.getLatitude(), place.getLongitude(), LOCALISATION_PRECISION, -1, mLocations.get(place));
            }
            isRunning = true;
        }
    }

    public void stopDetection(Context context) {
        if (isRunning) {
            for (PendingIntent intent : mLocations.values()) {
                mLocationManager.removeProximityAlert(intent);
            }
            context.unregisterReceiver(mBroadcast);
            isRunning = false;
        }
    }

    private void placeEntered(Context context, Place placeEntered) {
        inZone++;
        mGpsDetectorListener.currentPlaceChanged(placeEntered);
    }

    private void placeExited(Context context, Place placeExited) {
        if (inZone > 0) {
            inZone--;
        }
        if (inZone == 0) {
            mGpsDetectorListener.currentPlaceChanged(null);
        }
    }

    public interface GpsDetectorInterface {
        void currentPlaceChanged(Place currentPlace);
    }

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
                    placeEntered(context, triggeringPlace);
                } else {
                    placeExited(context, triggeringPlace);
                }
            }
        }
    }
}
