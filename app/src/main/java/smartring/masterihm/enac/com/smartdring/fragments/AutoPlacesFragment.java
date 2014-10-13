package smartring.masterihm.enac.com.smartdring.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartring.masterihm.enac.com.smartdring.R;

/**
 * Created by David on 13/10/2014.
 *
 * Fragment displaying the places.
 */
public class AutoPlacesFragment extends Fragment {

    public final static String TAG = "AutoPlacesFragmentTag";

    public static AutoPlacesFragment getInstance() {

        return new AutoPlacesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_autoplaces, container, false);
    }
}
