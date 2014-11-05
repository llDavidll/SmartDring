package smartring.masterihm.enac.com.smartdring.fragments;

import android.location.Location;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.data.Place;

/**
 * Created by David on 14/10/2014.
 * <p/>
 * Fragment used to create/modify a place.
 */
public class PlaceEditionFragment extends Fragment {

    public final static String TAG = "PlaceEditionFragmentTag";
    private static final String PLACE_KEY = "PLACE_KEY";

    private Place mPlace;


    private MapView mMapView;
    private GoogleMap googleMap;

    public static PlaceEditionFragment getInstance(Place place) {

        PlaceEditionFragment fragment = new PlaceEditionFragment();
        Bundle args = new Bundle();
        args.putSerializable(PLACE_KEY, place);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlace = (Place) getArguments().getSerializable(PLACE_KEY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View placeView = inflater.inflate(R.layout.fragment_place_edition, container, false);

        TextView title = (TextView) placeView.findViewById(R.id.fragment_place_edition_title);
        title.setText(mPlace.getName());

        mMapView = (MapView) placeView.findViewById(R.id.fragment_place_edition_mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng myLocation = new LatLng(location.getLatitude(),
                        location.getLongitude());
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 14));
            }
        });
        googleMap.setMyLocationEnabled(true);


        return placeView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    private void savePlace() {

    }
}
