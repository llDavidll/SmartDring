package smartring.masterihm.enac.com.smartdring.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.SmartDringActivity;
import smartring.masterihm.enac.com.smartdring.adapters.WhiteContactsAdapter;
import smartring.masterihm.enac.com.smartdring.data.Contact;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * Created by arnaud on 18/10/2014.
 */
public class ActionWhiteListFragment extends Fragment implements AdapterView.OnItemClickListener, WhiteContactsAdapter.WhiteContactDelete {

    public static final String TAG = "ActionWhiteListeFragmentTag";
    private TextView nameText;
    public static final int PICK_CONTACT_REQUEST = 1;
    private WhiteContactsAdapter mAdapter;
    private Contact lastContact = null;

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
        mAdapter = new WhiteContactsAdapter(getActivity(), this);
        mAdapter.setActionWhiteListFragment(this);
        mAdapter.addAll(((SmartDringActivity) getActivity()).getDB().getcontactList(true));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View profilesView = inflater.inflate(R.layout.fragment_action_whitelist, container, false);

        ListView lView = (ListView) profilesView.findViewById(R.id.fragment_whitelist_list);
        lView.setAdapter(mAdapter);
        lView.setOnItemClickListener(this);

        Button save = (Button) profilesView.findViewById(R.id.fragment_action_whitelist_savebutton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(ActionWhiteListFragment.this)
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
            lastContact.setmId(((SmartDringActivity) getActivity()).getDB().saveWhite(lastContact));
            mAdapter.add(lastContact);
        }
    }

    private String getContactInfo(Intent data, String kind) {
        Uri contactUri = data.getData();
        String[] projection = {kind};
        Cursor cursor = getActivity().getContentResolver()
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
        }
    }

    public void updateAdapter() {
        mAdapter.clear();
        mAdapter.addAll(((SmartDringActivity) getActivity()).getDB().getcontactList(true));
    }

    @Override
    public void deleteWhiteContact(Contact contact) {
        ((SmartDringActivity) getActivity()).getDB().deleteWhite(contact);
    }
}

