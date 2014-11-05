package smartring.masterihm.enac.com.smartdring.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.adapters.PlacesAdapter;
import smartring.masterihm.enac.com.smartdring.adapters.ProfilesAdapter;
import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.Profile;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Fragment displaying the places.
 */
public class AutoPlacesFragment extends Fragment implements AdapterView.OnItemClickListener {

    public final static String TAG = "AutoPlacesFragmentTag";

    /**
     * Adapter holding the data for the list.
     */
    private ArrayAdapter<Place> mAdapter;

    public static AutoPlacesFragment getInstance() {

        return new AutoPlacesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PlacesAdapter(getActivity());
        mAdapter.addAll(SmartDringDB.getDatabase(SmartDringDB.APP_DB).getPlaces(false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View placesView = inflater.inflate(R.layout.fragment_autoplaces, container, false);

        ListView list = (ListView) placesView.findViewById(R.id.fragment_autoplaces_list);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(this);
        return placesView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mAdapter != null) {
            Fragment editionFragment = PlaceEditionFragment.getInstance(mAdapter.getItem(i));
            FragmentManager fm = getFragmentManager();
            Fragment existingFragment = fm.findFragmentByTag(PlaceEditionFragment.TAG);
            FragmentTransaction ft = fm.beginTransaction();
            if (existingFragment != null) {
                ft = ft.remove(existingFragment);
                fm.popBackStack();
            }
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                    R.anim.fade_in, R.anim.fade_out)
                    .add(R.id.fragment_autoplaces_popup_container, editionFragment, PlaceEditionFragment.TAG)
                    .addToBackStack(PlaceEditionFragment.TAG)
                    .commit();

            fm.executePendingTransactions();
        }
    }
}
