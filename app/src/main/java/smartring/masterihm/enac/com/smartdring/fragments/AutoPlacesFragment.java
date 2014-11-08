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
import android.widget.ListView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.adapters.PlacesAdapter;
import smartring.masterihm.enac.com.smartdring.data.Place;
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
    private PlacesAdapter mAdapter;

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
    public void onResume() {
        super.onResume();
        mAdapter.refresh(SmartDringDB.getDatabase(SmartDringDB.APP_DB).getPlaces(false));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mAdapter != null) {
            Place p = mAdapter.getItem(i);
            if (p.getId() < 0) {
                p.setDefault(false);
                p.setId(SmartDringDB.getDatabase(SmartDringDB.APP_DB).save(p));
                mAdapter.add(p);
            }
            PlaceEditionFragment editionFragment = PlaceEditionFragment.getInstance(p);
            editionFragment.setPlaceFragment(this);
            FragmentManager fm = getFragmentManager();
            Fragment existingFragment = fm.findFragmentByTag(PlaceEditionFragment.TAG);
            FragmentTransaction ft = fm.beginTransaction();
            if (existingFragment != null) {
                ft = ft.remove(existingFragment);
                fm.popBackStack();
            }
            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                    R.anim.fade_in, R.anim.fade_out)
                    .add(R.id.activity_topcontainer, editionFragment, PlaceEditionFragment.TAG)
                    .addToBackStack(PlaceEditionFragment.TAG)
                    .commit();

            fm.executePendingTransactions();
        }
    }

    public void updateListView() {
        mAdapter.clear();
        mAdapter.addAll(SmartDringDB.getDatabase(SmartDringDB.APP_DB).getPlaces(false));
    }
}
