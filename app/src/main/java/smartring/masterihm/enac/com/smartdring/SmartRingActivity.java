package smartring.masterihm.enac.com.smartdring;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import smartring.masterihm.enac.com.smartdring.adapters.SmartRingPagerAdapter;
import smartring.masterihm.enac.com.smartdring.database.SmartRingDB;

/**
 * Main activity holding the multiple fragments in a viewpager.
 */
public class SmartRingActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SmartRingDB.initializeDB(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smartdring);
        initView();
    }

    /**
     * Initialize the view pager.
     */
    private void initView() {
        ViewPager mViewPager = (ViewPager) findViewById(R.id.activity_smartdring_viewpager);
        SmartRingPagerAdapter mAdapter = new SmartRingPagerAdapter(getSupportFragmentManager(), this);

        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(mAdapter);
        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.activity_smartdring_tabs);
        tabs.setViewPager(mViewPager);
    }

    @Override
    protected void finalize() throws Throwable {
        SmartRingDB.closeDB();
        super.finalize();
    }

    @Override
    public void onBackPressed() {
        // Issue with nested fragments :
        // https://code.google.com/p/android/issues/detail?id=40323
        // if there is a fragment and the back stack of this fragment is not empty,
        // then emulate 'onBackPressed' behaviour, because in default, it is not working
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}
