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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.adapters.BlackContactsAdapter;
import smartring.masterihm.enac.com.smartdring.data.Contact;

import static android.provider.ContactsContract.CommonDataKinds.*;

/**
 * Created by arnaud on 18/10/2014.
 */
public class ActionBlackListFragment extends Fragment implements AdapterView.OnItemClickListener, BlackContactsAdapter.BlackContactDelete {

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
        mAdapter = new BlackContactsAdapter(getActivity(), this);
        mAdapter.setActionBlackListFragment(this);
        mAdapter.addAll(((SmartDringActivity)getActivity()).getDB().getcontactList(false));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_action_blacklist, container, false);

        ListView lView = (ListView) profilesView.findViewById(R.id.fragment_blacklist_list);
        lView.setAdapter(mAdapter);
        lView.setOnItemClickListener(this);

        Button save = (Button) profilesView.findViewById(R.id.fragment_action_blacklist_savebutton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(ActionBlackListFragment.this)
                        .commit();
                getActivity().getSupportFragmentManager().executePendingTransactions();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return profilesView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (lastContact != null && data != null) {
            lastContact.setContactPhoneNumber(getContactInfo(data, Phone.NUMBER));
            lastContact.setContactName(getContactInfo(data, ContactsContract.Profile.DISPLAY_NAME));
            lastContact.setmId(((SmartDringActivity)getActivity()).getDB().saveBlack(lastContact));
            if (isNotAllreadyExistBlackList(lastContact)) {
                mAdapter.add(lastContact);
            }
        }
    }

    private boolean isNotAllreadyExistBlackList(Contact contact) {
        ArrayList<Contact> wlc = (ArrayList<Contact>) ((SmartDringActivity) getActivity()).getDB().getcontactList(true);
        ArrayList<Contact> blc = new ArrayList<Contact>();
        for (int i = 0; i < mAdapter.getCount(); ++i) {
            blc.add(mAdapter.getItem(i));
        }
        for (Contact c : blc) {
            if (c.getContactName().contentEquals(contact.getContactName())) {
                return false;
            }
        }
        for (Contact c : wlc) {
            if (c.getContactName().contentEquals(contact.getContactName())) {
                return false;
            }
        }
        return true;
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
        mAdapter.addAll(((SmartDringActivity)getActivity()).getDB().getcontactList(false));
        ((SmartDringActivity) getActivity()).getService().preferencesUpdated();
    }

    private void quitFragment() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .remove(ActionBlackListFragment.this)
                .commit();
        getActivity().getSupportFragmentManager().executePendingTransactions();
        getActivity().getSupportFragmentManager().popBackStack();
        updateAdapter();
    }

    @Override
    public void deleteBlackContact(Contact contact) {
        ((SmartDringActivity)getActivity()).getDB().deleteBlack(contact);
        ((SmartDringActivity) getActivity()).getService().preferencesUpdated();
    }
}

