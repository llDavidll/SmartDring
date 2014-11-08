package smartring.masterihm.enac.com.smartdring;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import smartring.masterihm.enac.com.smartdring.adapters.SmartDringPagerAdapter;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;
import smartring.masterihm.enac.com.smartdring.data.SmartDringPreferences;
import smartring.masterihm.enac.com.smartdring.fragments.ActionWhiteListFragment;
import smartring.masterihm.enac.com.smartdring.service.IServiceCommunication;
import smartring.masterihm.enac.com.smartdring.service.SmartDringService;

/**
 * Main activity holding the multiple fragments in a viewpager.
 */
public class SmartDringActivity extends FragmentActivity {

    private ServiceManagement mServiceManagment = new ServiceManagement();

    private static Fragment fragActivityResult = null;

    public static void setFragActivityResult(Fragment fragActivityResult) {
        SmartDringActivity.fragActivityResult = fragActivityResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SmartDringDB.getDatabase(SmartDringDB.APP_DB) == null) {
            SmartDringDB.initializeDB(this, SmartDringDB.APP_DB);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartdring);
        initView();
    }

    @Override
    protected void onStart() {
        if (SmartDringPreferences.getBooleanPreference(this, SmartDringPreferences.SMARTRING_STATE)) {
            mServiceManagment.bindActivityToService();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (SmartDringPreferences.getBooleanPreference(this, SmartDringPreferences.SMARTRING_STATE)) {
            mServiceManagment.unbindActivityFromService();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ActionWhiteListFragment f = (ActionWhiteListFragment) getSupportFragmentManager().findFragmentByTag(ActionWhiteListFragment.TAG);
        if (fragActivityResult != null) {
            fragActivityResult.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Initialize the view pager.
     */
    private void initView() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.activity_smartdring_viewpager);
        SmartDringPagerAdapter mAdapter = new SmartDringPagerAdapter(getSupportFragmentManager(), this);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_smartdring_tabs);
        tabs.setViewPager(mViewPager);
    }

    @Override
    protected void finalize() throws Throwable {
        SmartDringDB.closeDB(SmartDringDB.APP_DB);
        super.finalize();
    }

    public ServiceManagement getService() {
        return mServiceManagment;
    }

    /**
     * Class used to communicate with the service. Service communication should be done only here.
     */
    public class ServiceManagement {
        // Binder used to send command to the service.
        private IServiceCommunication service;
        // Connection to the service.
        private ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                service = IServiceCommunication.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                service = null;
            }
        };

        /**
         * Create the service if needed, then bind it to the activity.
         */
        public void bindActivityToService() {
            Intent intent = new Intent(SmartDringActivity.this, SmartDringService.class);
            startService(intent);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }

        /**
         * Unbind the service, then stop it.
         */
        public void stopService() {
            unbindActivityFromService();
            SmartDringActivity.this.stopService(new Intent(SmartDringActivity.this, SmartDringService.class));
        }

        /**
         * Unbind the service from the activity.
         */
        public void unbindActivityFromService() {
            service = null;
            unbindService(serviceConnection);
        }

        /**
         * Notify the service that the preferences have been updated.
         */
        public void preferencesUpdated() {
            if (service != null) {
                try {
                    service.preferencesUpdated();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
