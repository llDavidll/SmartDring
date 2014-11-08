package smartring.masterihm.enac.com.smartdring.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.data.Contact;
import smartring.masterihm.enac.com.smartdring.data.Profile;
import smartring.masterihm.enac.com.smartdring.data.SmartDringDB;
import smartring.masterihm.enac.com.smartdring.fragments.ActionBlackListFragment;
import smartring.masterihm.enac.com.smartdring.fragments.ActionWhiteListFragment;
import smartring.masterihm.enac.com.smartdring.fragments.ProfilesFragment;

/**
 * Created by arnaud on 07/11/2014.
 */
public class BlackContactsAdapter extends ArrayAdapter<Contact> {

    private final LayoutInflater mInflater;

    public void setActionBlackListFragment(ActionBlackListFragment actionBlackListFragment) {
        this.actionBlackListFragment = actionBlackListFragment;
    }
    private ActionBlackListFragment actionBlackListFragment;

    public BlackContactsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position >= getCount() - 1) {
            // Special view for the last item (add new)
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_contact_specialadd, parent, false);
            }
        } else {
            // Normal view for a profile.
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_black_contact, parent, false);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.fragment_action_blacklist_name);
                holder.phone = (TextView) convertView.findViewById(R.id.fragment_action_blacklist_phone);
                holder.deletebutton = (Button) convertView.findViewById(R.id.fragment_action_blacklist_deletebutton);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Contact item = getItem(position);
            if (item != null) {
                holder.name.setText(item.getContactName());
                holder.phone.setText(item.getContactPhoneNumber());
                holder.deletebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SmartDringDB.getDatabase(SmartDringDB.APP_DB).deleteBlack(item);
                        actionBlackListFragment.updateAdapter();
                    }
                });
            }
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= getCount() - 1) {
            // Special item (add new)
            return 1;
        } else {
            // Normal item
            return 0;
        }
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public
    @Nullable
    Contact getItem(int position) {
        if (position == getCount() - 1) {
            return new Contact();
        }
        return super.getItem(position);
    }

    private class ViewHolder {
        private TextView name;
        private TextView phone;
        private Button deletebutton;
    }
}
