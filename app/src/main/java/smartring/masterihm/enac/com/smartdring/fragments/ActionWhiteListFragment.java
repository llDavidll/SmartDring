package smartring.masterihm.enac.com.smartdring.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartring.masterihm.enac.com.smartdring.R;

/**
 * Created by arnaud on 18/10/2014.
 */
public class ActionWhiteListFragment extends Fragment {

    public static final String TAG = "ActionWhiteListeFragmentTag";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_action_whitelist, container, false);

        return profilesView;
    }
}
