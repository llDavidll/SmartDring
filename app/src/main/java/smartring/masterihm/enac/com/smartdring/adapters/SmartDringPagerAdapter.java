package smartring.masterihm.enac.com.smartdring.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.fragments.ActionFragment;
import smartring.masterihm.enac.com.smartdring.fragments.AutoFragment;
import smartring.masterihm.enac.com.smartdring.fragments.ProfilesFragment;
import smartring.masterihm.enac.com.smartdring.types.MainPages;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Adapter used to display the multiple tabs of the application.
 */
public class SmartDringPagerAdapter extends FragmentPagerAdapter {

    private final MainPages[] mPages = new MainPages[]{
            MainPages.PROFILES,
            MainPages.AUTO,
            MainPages.ACTION};

    private final Context mContext;

    public SmartDringPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (mPages[i]) {
            case PROFILES:
                return ProfilesFragment.getInstance();

            case AUTO:
                return AutoFragment.getInstance();

            case ACTION:
                return ActionFragment.getInstance();

            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (mPages[position]) {
            case PROFILES:
                return mContext.getString(R.string.fragment_title_profile);

            case AUTO:
                return mContext.getString(R.string.fragment_title_auto);

            case ACTION:
                return mContext.getString(R.string.fragment_title_action);

            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getCount() {
        return mPages.length;
    }

}
