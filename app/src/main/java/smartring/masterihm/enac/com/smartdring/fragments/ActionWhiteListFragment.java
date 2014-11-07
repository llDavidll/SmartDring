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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.adapters.ContactsAdapter;
import smartring.masterihm.enac.com.smartdring.adapters.ProfilesAdapter;
import smartring.masterihm.enac.com.smartdring.data.Contact;
import smartring.masterihm.enac.com.smartdring.data.Profile;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

import static android.provider.ContactsContract.CommonDataKinds.*;

/**
 * Created by arnaud on 18/10/2014.
 */
public class ActionWhiteListFragment extends Fragment {

    public static final String TAG = "ActionWhiteListeFragmentTag";
    private TextView nameText;
    public static final int PICK_CONTACT_REQUEST = 1;
    private ContactsAdapter mAdapter;

    public static ActionWhiteListFragment getInstance() {

        ActionWhiteListFragment fragment = new ActionWhiteListFragment();

       /* Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, 1);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ContactsAdapter(getActivity());
        mAdapter.add(new Contact("TOTO", "0683917453"));
        mAdapter.add(new Contact("Taph", "068398153"));
        mAdapter.add(new Contact("JEJ", "067887453"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_action_whitelist, container, false);

        ListView lView = (ListView) profilesView.findViewById(R.id.fragment_whitelist_list);
        lView.setAdapter(mAdapter);

        nameText = (TextView) profilesView.findViewById(R.id.phoneText);
        View button = profilesView.findViewById(R.id.button);
        final Fragment f = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmartDringActivity.setFragActivityResult(f);
                Intent contactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                contactIntent.setType(Phone.CONTENT_TYPE);
                startActivityForResult(contactIntent, PICK_CONTACT_REQUEST);
                FragmentManager fm = getFragmentManager();
            }
        });
        return profilesView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        nameText.setText("request : " + requestCode + " - result : " + resultCode);

        String number = getContactInfo(data, Phone.NUMBER);
        String name = getContactInfo(data, ContactsContract.Profile.DISPLAY_NAME);
        nameText.setText(number + " - " + name);
    }

    private String getContactInfo(Intent data, String kind) {
        Uri contactUri = data.getData();
        String[] projection = {kind};
        Cursor cursor = (Cursor) getActivity().getContentResolver()
                .query(contactUri, projection, null, null, null);
        cursor.moveToFirst();
        int column = cursor.getColumnIndex(kind);
        return cursor.getString(column);
    }
}

