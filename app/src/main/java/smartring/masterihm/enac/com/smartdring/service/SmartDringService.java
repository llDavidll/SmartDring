package smartring.masterihm.enac.com.smartdring.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

/**
 * Service of the application always running in background.
 * This service receive notification of all context events and changes the sound profile accordingly.
 */
public class SmartDringService extends Service {

    // Binder used by the activity to communicate with the service.
    private final IServiceCommunication.Stub mBinder = new IServiceCommunication.Stub() {
        @Override
        public void preferencesUpdated() throws RemoteException {
            // Update the service behaviour according to the current data.
            mContextHandler.updateContextHandler(SmartDringService.this);
            mContextDetector.updateContextDetection(SmartDringService.this);
        }
    };

    private SmartDringDB mDatabase;
    // Context detector, creating notifications on context change.
    private ContextChangeDetector mContextDetector;
    // Context handler, receiving and handling notification on context change.
    private ContextEventHandler mContextHandler;

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
        // Init database
        mDatabase = new SmartDringDB(this, SmartDringDB.SERVICE_DB);
        // Create context handler
        mContextHandler = new ContextEventHandler(this);
        // Create context detector.
        mContextDetector = new ContextChangeDetector(this, mContextHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContextHandler.startContextHandle(this);
        mContextDetector.startContextDetection(this);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        mContextHandler.stopContextHandle();
        mContextDetector.stopContextDetection(this);
        mDatabase.closeDB();
        super.onDestroy();
    }

    public SmartDringDB getDB(){
        return mDatabase;
    }

}

