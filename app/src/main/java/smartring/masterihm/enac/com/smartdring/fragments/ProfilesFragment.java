package smartring.masterihm.enac.com.smartdring.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.adapters.ProfilesAdapter;
import smartring.masterihm.enac.com.smartdring.database.Profile;
import smartring.masterihm.enac.com.smartdring.database.SmartRingDB;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Profile fragment containing a grid of profiles.
 */
public class ProfilesFragment extends Fragment {

    /**
     * Adapter holding the data for the grid.
     */
    private ArrayAdapter<Profile> mAdapter;

    public static ProfilesFragment getInstance() {

        return new ProfilesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ProfilesAdapter(getActivity());
        mAdapter.addAll(SmartRingDB.getDatabase().getProfiles());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_profiles, container, false);

        GridView mGrid = (GridView) profilesView.findViewById(R.id.fragment_profiles_grid);
        mGrid.setAdapter(mAdapter);

        return profilesView;
    }
}
