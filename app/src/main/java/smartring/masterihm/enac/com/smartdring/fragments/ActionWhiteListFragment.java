package smartring.masterihm.enac.com.smartdring.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.data.Profile;

/**
 * Created by arnaud on 18/10/2014.
 */
public class ActionWhiteListFragment extends Fragment {

    public static final String TAG = "ActionWhiteListeFragmentTag";
    //private static final String PROFILE_KEY = "Whilte_List";

    public static ActionWhiteListFragment getInstance() {

        ActionWhiteListFragment fragment = new ActionWhiteListFragment();

       /* Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, 1);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_action_whitelist, container, false);
        return profilesView;
    }
}

