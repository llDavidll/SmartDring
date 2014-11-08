package smartring.masterihm.enac.com.smartdring.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

/**
 * Created by David on 14/10/2014.
 * <p/>
 * Fragment used to create/modify a place.
 */
public class PlaceEditionFragment extends Fragment {

    public final static String TAG = "PlaceEditionFragmentTag";
    private static final String PLACE_KEY = "PLACE_KEY";

    private Place mPlace;

    private EditText mNameEditText;
    private MapView mMapView;
    private GoogleMap googleMap;

    public void setPlaceFragment(AutoPlacesFragment placeFragment) {
        this.placeFragment = placeFragment;
    }

    private AutoPlacesFragment placeFragment;

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

        mNameEditText = (EditText) placeView.findViewById(R.id.fragment_place_edition_title);
        mNameEditText.setText(mPlace.getName());
        if (mPlace.isDefault()) {
            mNameEditText.setEnabled(false);
        }

        mMapView = (MapView) placeView.findViewById(R.id.fragment_place_edition_mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        googleMap = mMapView.getMap();

        configureMap();

        Button updateButton = (Button) placeView.findViewById(R.id.fragment_place_edition_update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng mapPos = googleMap.getCameraPosition().target;
                mPlace.setLatitude(mapPos.latitude);
                mPlace.setLongitude(mapPos.longitude);
            }
        });


        Button deleteButton = (Button) placeView.findViewById(R.id.fragment_place_edition_delete);
        if (mPlace.isDefault()) {
            deleteButton.setVisibility(View.INVISIBLE);
        } else {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SmartDringDB.getDatabase(SmartDringDB.APP_DB).delete(mPlace);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .remove(PlaceEditionFragment.this)
                            .commit();
                    getActivity().getSupportFragmentManager().executePendingTransactions();
                    getActivity().getSupportFragmentManager().popBackStack();
                    placeFragment.updateListView();
                }
            });
        }

        Button saveButton = (Button) placeView.findViewById(R.id.fragment_place_edition_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePlace();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(PlaceEditionFragment.this)
                        .commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return placeView;
    }

    private void configureMap() {
        if (mPlace.getLatitude() != 0 && mPlace.getLongitude() != 0) {
            LatLng placeLocation = new LatLng(mPlace.getLatitude(), mPlace.getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(placeLocation)
                    .title(mPlace.getName())
                    .draggable(false));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(placeLocation, 14));
        } else {
            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(location.getLatitude(), location.getLongitude()),
                            14));
                    googleMap.setOnMyLocationChangeListener(null);
                }
            });
        }
        googleMap.setMyLocationEnabled(true);
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
        if (mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null) {
            mMapView.onLowMemory();
        }
    }


    private void savePlace() {
        mPlace.setName(mNameEditText.getText().toString());
        SmartDringDB.getDatabase(SmartDringDB.APP_DB).save(mPlace);
    }
}
