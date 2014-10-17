package smartring.masterihm.enac.com.smartdring.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;
import smartring.masterihm.enac.com.smartdring.data.SmartDringPreferences;

public class SmartDringService extends Service {

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private int NOTIFICATION = R.string.local_service_started;


    private SensorDetector mSensorDetector;

    private SensorDetector.PhoneStateInterface mPhoneStateListener = new SensorDetector.PhoneStateInterface() {
        @Override
        public void phoneFlippedStateChanged(boolean isPhoneFlipped) {
            Log.d("Phone state : ", isPhoneFlipped ? "Phone flipped" : "Nothing special");
        }
    };

    public static void startService(Context context){
        context.startService(new Intent(context, SmartDringService.class));
    }

    public static void stopService(Context context){
        context.stopService(new Intent(context, SmartDringService.class));
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mSensorDetector = new SensorDetector(this, mPhoneStateListener);

        // Display a notification about us starting.  We put an icon in the status bar.
        showNotification();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initService();

        return Service.START_STICKY;
    }

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

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        Intent bIntent = new Intent(SmartDringService.this, SmartDringActivity.class);
        PendingIntent pbIntent = PendingIntent.getActivity(SmartDringService.this, 0, bIntent, 0);
        String title;
        if (SmartDringDB.getDatabase() == null) {
            SmartDringDB.initializeDB(this);
            title = SmartDringDB.getDatabase().getProfiles().get(0).getName();
            SmartDringDB.closeDB();
        } else {
            title = SmartDringDB.getDatabase().getProfiles().get(0).getName();
        }

        NotificationCompat.Builder bBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText("Profile activated")
                        .setOngoing(true)
                        .setContentIntent(pbIntent);
        Notification barNotif = bBuilder.build();
        this.startForeground(NOTIFICATION, barNotif);
    }


}
