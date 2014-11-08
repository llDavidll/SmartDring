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
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.data.SmartDringPreferences;

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
        activationSwitch.setChecked(SmartDringPreferences.getBooleanPreference(getActivity(), SmartDringPreferences.SMARTRING_STATE));
        boundSwitchToPreference(activationSwitch, SmartDringPreferences.SMARTRING_STATE);

        activationSwitch = (Switch) actionView.findViewById(R.id.fragment_action_switch_phoneflip);
        boundSwitchToPreference(activationSwitch, SmartDringPreferences.PHONEFLIP_STATE);

        activationSwitch = (Switch) actionView.findViewById(R.id.fragment_action_switch_whitelist);
        boundSwitchToPreference(activationSwitch, SmartDringPreferences.WHITELIST_STATE);

        activationSwitch = (Switch) actionView.findViewById(R.id.fragment_action_switch_blacklist);
        boundSwitchToPreference(activationSwitch, SmartDringPreferences.BLACKLIST_STATE);

        View whiteListView = actionView.findViewById(R.id.fragment_action_whitelist_layout);
        whiteListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // White list line clicked, open edit fragment
                Fragment whiteFragment = ActionWhiteListFragment.getInstance();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

               /* Fragment existingFragment = fm.findFragmentByTag(ActionWhiteListFragment.TAG);
                if (existingFragment != null) {
                    ft = ft.remove(existingFragment);
                    fm.popBackStack();
                }*/

                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                        R.anim.fade_in, R.anim.fade_out)
                        .add(R.id.fragment_action_container, whiteFragment, ActionWhiteListFragment.TAG)
                        .addToBackStack(ActionWhiteListFragment.TAG)
                        .commit();
                fm.executePendingTransactions();
        }});

            View blackListView = actionView.findViewById(R.id.fragment_action_blacklist_layout);
        blackListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Black list line clicked, open edit fragment
                Fragment blackFragment = ActionBlackListFragment.getInstance();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

               /* Fragment existingFragment = fm.findFragmentByTag(ActionWhiteListFragment.TAG);
                if (existingFragment != null) {
                    ft = ft.remove(existingFragment);
                    fm.popBackStack();
                }*/

                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out,
                        R.anim.fade_in, R.anim.fade_out)
                        .add(R.id.fragment_action_container, blackFragment, ActionBlackListFragment.TAG)
                        .addToBackStack(ActionBlackListFragment.TAG)
                        .commit();
                fm.executePendingTransactions();
            }
        });

        return actionView;
    }

    private void boundSwitchToPreference(final Switch pSwitch, final String pPreference) {
        pSwitch.setChecked(SmartDringPreferences.getBooleanPreference(getActivity(), pPreference));
        pSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SmartDringPreferences.setBooleanPreference((SmartDringActivity) getActivity(), pPreference, b);
            }
        });
    }
}
