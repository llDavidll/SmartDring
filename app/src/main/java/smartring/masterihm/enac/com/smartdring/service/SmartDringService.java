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

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.SmartDringPreferences;

public class SmartDringService extends Service implements SensorDetector.PhoneStateInterface {

    // Binder used by the activity to communicate with the service.
    private final IServiceCommunication.Stub mBinder = new IServiceCommunication.Stub() {
        @Override
        public void preferencesUpdated() throws RemoteException {
            initService();
        }
    };
    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;
    // Sensor detector used to detect the phone flip.
    private SensorDetector mSensorDetector;

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
        mSensorDetector = new SensorDetector(this, this);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initService();

        return Service.START_STICKY;
    }

    /**
     * Initialize the service behaviour.
     */
    private void initService() {
        if (SmartDringPreferences.getBooleanPreference(this, SmartDringPreferences.PHONEFLIP_STATE)) {
            mSensorDetector.startDetection();
        } else {
            mSensorDetector.stopDetection();
        }
    }

    @Override
    public void onDestroy() {
        mSensorDetector.stopDetection();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(NOTIFICATION);

        super.onDestroy();
    }

    @Override
    public void phoneFlippedStateChanged(boolean isPhoneFlipped) {
        Log.d("Phone state : ", isPhoneFlipped ? "Phone flipped" : "Nothing special");
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        Intent bIntent = new Intent(SmartDringService.this, SmartDringActivity.class);
        PendingIntent pbIntent = PendingIntent.getActivity(SmartDringService.this, 0, bIntent, 0);

        NotificationCompat.Builder bBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("SmartDring")
                        .setContentText("Profile activated")
                        .setOngoing(true)
                        .setContentIntent(pbIntent);
        Notification barNotif = bBuilder.build();
        this.startForeground(NOTIFICATION, barNotif);
    }
}

