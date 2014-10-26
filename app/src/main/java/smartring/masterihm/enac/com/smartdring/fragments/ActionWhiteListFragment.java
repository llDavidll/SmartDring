package smartring.masterihm.enac.com.smartdring.fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.data.Profile;

/**
 * Created by arnaud on 18/10/2014.
 */
public class ActionWhiteListFragment extends Fragment {

    public static final String TAG = "ActionWhiteListeFragmentTag";
    private TextView nameText;
    public static final int PICK_CONTACT_REQUEST = 1;

    public static ActionWhiteListFragment getInstance() {

        ActionWhiteListFragment fragment = new ActionWhiteListFragment();

       /* Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, 1);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_action_whitelist, container, false);
        nameText = (TextView) profilesView.findViewById(R.id.phoneText);
        View button = profilesView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setText("TiTi");
                Intent contactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                contactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(contactIntent, PICK_CONTACT_REQUEST);
                FragmentManager fm = getFragmentManager();
            }
        });
        return profilesView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        nameText.setText("TOTO");
        if (requestCode == PICK_CONTACT_REQUEST){
            nameText.setText("TOTO");
            if (resultCode == Activity.RESULT_OK) {
                String name = data.getStringExtra("name");
                nameText.setText(name);
            }
        }
    }
}

