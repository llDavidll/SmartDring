package smartring.masterihm.enac.com.smartdring.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import smartring.masterihm.enac.com.smartdring.R;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Fragment for the "Auto" tab, containing either the "Time" or the "Places" fragment.
 */
public class AutoFragment extends Fragment {

    /**
     * Use this to get an instance of the AutoFragment
     *
     * @return a new instance of the AutoFragment
     */
    public static AutoFragment getInstance() {
        return new AutoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_auto, container, false);

        Switch autoSwitch = (Switch) profilesView.findViewById(R.id.fragment_auto_switch);
        // Change the fragment when the user clicks on the switch.
        autoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    displayFragment(AutoFragments.TIME);
                } else {
                    displayFragment(AutoFragments.PLACES);
                }
            }
        });
        // Init the view with the correct fragment.
        if (autoSwitch.isChecked()) {
            displayFragment(AutoFragments.TIME);
        } else {
            displayFragment(AutoFragments.PLACES);
        }
        return profilesView;
    }

    /**
     * Display the correct fragment inside the auto tab.
     *
     * @param toDisplay the identifier of the fragment to display.
     */
    private void displayFragment(AutoFragments toDisplay) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        final String tagToAdd;
        Fragment fragmentToAdd;
        // Get the right fragment tag and object.
        switch (toDisplay) {
            case PLACES:
                tagToAdd = AutoPlacesFragment.TAG;
                fragmentToAdd = fm.findFragmentByTag(tagToAdd);
                if (fragmentToAdd == null) {
                    fragmentToAdd = AutoPlacesFragment.getInstance();
                }
                break;

            default:
            case TIME:
                tagToAdd = AutoTimeFragment.TAG;
                fragmentToAdd = fm.findFragmentByTag(tagToAdd);
                if (fragmentToAdd == null) {
                    fragmentToAdd = AutoTimeFragment.getInstance();
                }
                break;
        }
        // Display the right fragment with the right tag.
        fm.beginTransaction()
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                        R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.fragment_auto_container, fragmentToAdd, tagToAdd)
                .commit();
    }

    /**
     * Identifiers for the fragment in the auto tab.
     */
    private enum AutoFragments {
        TIME,
        PLACES
    }
}
