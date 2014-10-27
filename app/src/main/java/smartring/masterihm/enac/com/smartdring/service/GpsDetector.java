package smartring.masterihm.enac.com.smartdring.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

/**
 * Created by David on 21/10/2014.
 */
public class GpsDetector implements LocationListener {

    private LocationManager mLocationManager;

    private Map<Place, PendingIntent> mLocations;

    public GpsDetector(Context context) {
        // Acquire a reference to the system Location Manager
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        mLocations = new HashMap<Place, PendingIntent>(3);
        // Here, create the pending intent for each place in the "places" from the database.
        for (Place place : SmartDringDB.getDatabase(SmartDringDB.SERVICE_DB).getPlaces()) {
            // http://stackoverflow.com/a/16444917
        }

    }

    public void startDetection() {
        for (Place place : SmartDringDB.getDatabase(SmartDringDB.SERVICE_DB).getPlaces()) {
            mLocationManager.addProximityAlert(place.getLatitude(), place.getLongitude(), 10, -1, mLocations.get(place));
        }
    }

    public void stopDetection() {
        for (PendingIntent intent : mLocations.values()) {
            mLocationManager.removeProximityAlert(intent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
