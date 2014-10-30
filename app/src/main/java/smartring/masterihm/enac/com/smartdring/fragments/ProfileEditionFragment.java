package smartring.masterihm.enac.com.smartdring.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.AudioManager;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.Profile;

/**
 * Created by David on 14/10/2014.
 * <p/>
 * Fragment used to create/modify a profile.
 */
public class ProfileEditionFragment extends Fragment {

    public final static String TAG = "ProfileEditionFragmentTag";
    private static final String PROFILE_KEY = "PROFILE_KEY";

    public AudioManager am;

    public static ProfileEditionFragment getInstance(@Nullable Profile profile) {

        ProfileEditionFragment fragment = new ProfileEditionFragment();
        Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, profile);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_edition, container, false);
        FragmentActivity fa = SmartDringActivity.mainActivity;
        am = (AudioManager)fa.getSystemService(Context.AUDIO_SERVICE);
        return v;
    }
}
