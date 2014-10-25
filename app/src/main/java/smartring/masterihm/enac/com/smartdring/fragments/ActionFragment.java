package smartring.masterihm.enac.com.smartdring.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartRingActivity;
import smartring.masterihm.enac.com.smartdring.data.SmartRingPreferences;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Fragment displaying the actions.
 */
public class ActionFragment extends Fragment {

    public static ActionFragment getInstance() {

        return new ActionFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View actionView = inflater.inflate(R.layout.fragment_action, container, false);

        // Bound all the switches to their values in the shared preferences :

        Switch activationSwitch = (Switch) actionView.findViewById(R.id.fragment_action_switch_activation);
        boundSwitchToPreference(activationSwitch, SmartRingPreferences.SMARTRING_STATE, true);

        activationSwitch = (Switch) actionView.findViewById(R.id.fragment_action_switch_phoneflip);
        boundSwitchToPreference(activationSwitch, SmartRingPreferences.PHONEFLIP_STATE, false);

        activationSwitch = (Switch) actionView.findViewById(R.id.fragment_action_switch_whitelist);
        boundSwitchToPreference(activationSwitch, SmartRingPreferences.WHITELIST_STATE, false);

        activationSwitch = (Switch) actionView.findViewById(R.id.fragment_action_switch_blacklist);
        boundSwitchToPreference(activationSwitch, SmartRingPreferences.BLACKLIST_STATE, false);

        View whiteListView = actionView.findViewById(R.id.fragment_action_whitelist_layout);
        whiteListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // White list line clicked, open edit fragment
                FragmentManager fm = getChildFragmentManager();
                Fragment existingFragment = fm.findFragmentByTag(ActionWhiteListFragment.TAG);
                FragmentTransaction ft = fm.beginTransaction();
                fm.executePendingTransactions();
            }
        });

        View blackListView = actionView.findViewById(R.id.fragment_action_blacklist_layout);
        blackListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Black list line clicked, open edit fragment
            }
        });

        return actionView;
    }

    private void boundSwitchToPreference(final Switch pSwitch, final String pPreference, final boolean pDefault) {
        pSwitch.setChecked(SmartRingPreferences.getBooleanPreference(getActivity(), pPreference, pDefault));
        pSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SmartRingPreferences.setBooleanPreference(getActivity(), pPreference, b);
            }
        });
    }
}
