package smartring.masterihm.enac.com.smartdring.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;
import smartring.masterihm.enac.com.smartdring.data.SmartDringPreferences;

public class SmartDringService extends Service implements SensorDetector.PhoneStateInterface, GpsDetector.GpsDetectorInterface {

    // Binder used by the activity to communicate with the service.
    private final IServiceCommunication.Stub mBinder = new IServiceCommunication.Stub() {
        @Override
        public void preferencesUpdated() throws RemoteException {
            updateService();
        }
    };
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    // Sensor detector used to detect the phone flip.
    private SensorDetector mSensorDetector;

    // Register for gps proximity alerts
    private GpsDetector mGpsDetector;

    private NotificationCompat.Builder mNotifBuilder;
    private NotificationManager mNotifManager;

    private IncallListener phoneListener;

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        // Init data and notification
        SmartDringDB.initializeDB(this, SmartDringDB.SERVICE_DB);
        mNotifBuilder = new NotificationCompat.Builder(this);
        mNotifManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();

        // Init context detection
        mSensorDetector = new SensorDetector(this, this);
        mGpsDetector = new GpsDetector(this, this);
        phoneListener = new IncallListener();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (SmartDringPreferences.getBooleanPreference(this, SmartDringPreferences.PHONEFLIP_STATE)) {
            mSensorDetector.startDetection();
        } else {
            mSensorDetector.stopDetection();
        }
        mGpsDetector.startDetection();
        return Service.START_STICKY;
    }

    /**
     * Initialize the service behaviour.
     */
    private void updateService() {
        if (SmartDringPreferences.getBooleanPreference(this, SmartDringPreferences.PHONEFLIP_STATE)) {
            mSensorDetector.startDetection();
        } else {
            mSensorDetector.stopDetection();
        }
        mGpsDetector.refreshLocations(this);
    }

    @Override
    public void onDestroy() {
        mSensorDetector.stopDetection();
        mGpsDetector.stopDetection(this);
        mNotifManager.cancel(NOTIFICATION);
        SmartDringDB.closeDB(SmartDringDB.SERVICE_DB);
        super.onDestroy();
    }

    @Override
    public void phoneFlippedStateChanged(boolean isPhoneFlipped) {
        Log.d("Phone state : ", isPhoneFlipped ? "Phone flipped" : "Nothing special");
    }

    @Override
    public void currentPlaceChanged(Place currentPlace) {
        mNotifBuilder.setContentText(getNotifText(currentPlace));
        mNotifManager.notify(NOTIFICATION, mNotifBuilder.build());
        if (currentPlace == null) {
            Toast.makeText(this, "Outdorr", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, currentPlace.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getNotifText(Place place) {
        String result = "Profile activated : ";
        result += SmartDringDB.getDatabase(SmartDringDB.SERVICE_DB).getProfiles().get(0).getName();
        result += " Place : ";
        if (place == null) {
            result += "Outdoor";
        } else {
            result += place.getName();
        }
        return result;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        Intent bIntent = new Intent(SmartDringService.this, SmartDringActivity.class);
        PendingIntent pbIntent = PendingIntent.getActivity(SmartDringService.this, 0, bIntent, 0);
        mNotifBuilder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("SmartDring")
                .setContentText(getNotifText(null))
                .setOngoing(true)
                .setContentIntent(pbIntent);
        Notification barNotif = mNotifBuilder.build();
        mNotifManager.notify(NOTIFICATION, barNotif);
    }
}

