package smartring.masterihm.enac.com.smartdring.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.adapters.BlackContactsAdapter;
import smartring.masterihm.enac.com.smartdring.adapters.WhiteContactsAdapter;
import smartring.masterihm.enac.com.smartdring.data.Contact;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;

import static android.provider.ContactsContract.CommonDataKinds.*;

/**
 * Created by arnaud on 18/10/2014.
 */
public class ActionBlackListFragment extends Fragment implements AdapterView.OnItemClickListener {

    public static final String TAG = "ActionBlackListeFragmentTag";
    private TextView nameText;
    public static final int PICK_CONTACT_REQUEST = 1;
    private BlackContactsAdapter mAdapter;
    private Contact lastContact = null;

    public static ActionBlackListFragment getInstance() {

        ActionBlackListFragment fragment = new ActionBlackListFragment();

       /* Bundle args = new Bundle();
        args.putSerializable(PROFILE_KEY, 1);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new BlackContactsAdapter(getActivity());
        mAdapter.setActionBlackListFragment(this);
        mAdapter.addAll(SmartDringDB.getDatabase(SmartDringDB.APP_DB).getcontactList(false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_action_blacklist, container, false);

        ListView lView = (ListView) profilesView.findViewById(R.id.fragment_blacklist_list);
        lView.setAdapter(mAdapter);
        lView.setOnItemClickListener(this);

        return profilesView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (lastContact != null) {
            lastContact.setContactPhoneNumber(getContactInfo(data, Phone.NUMBER));
            lastContact.setContactName(getContactInfo(data, ContactsContract.Profile.DISPLAY_NAME));
            lastContact.setmId(SmartDringDB.getDatabase(SmartDringDB.APP_DB).saveBlack(lastContact));
            mAdapter.add(lastContact);
        }
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mAdapter != null) {
            Contact c = mAdapter.getItem(i);
            lastContact = c;
            SmartDringActivity.setFragActivityResult(this);
            Intent contactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
            contactIntent.setType(Phone.CONTENT_TYPE);
            startActivityForResult(contactIntent, PICK_CONTACT_REQUEST);
            FragmentManager fm = getFragmentManager();
        }
    }

    public void updateAdapter() {
        mAdapter.clear();
        mAdapter.addAll(SmartDringDB.getDatabase(SmartDringDB.APP_DB).getcontactList(false));
    }
}

