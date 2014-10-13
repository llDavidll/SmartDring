package smartring.masterihm.enac.com.smartdring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.database.Profile;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Adapter used to display a list of grid of profiles.
 */
public class ProfilesAdapter extends ArrayAdapter<Profile> {

    /**
     * Layout inflater used to create the item views.
     */
    private final LayoutInflater mInflater;

    public ProfilesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_profile, parent, false);

            holder = new ViewHolder();
            holder.nameTV = (TextView) convertView.findViewById(android.R.id.text1);
            holder.background = convertView.findViewById(R.id.item_profile_background);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Profile item = getItem(position);
        holder.nameTV.setText(item.getName());
        holder.background.setBackgroundColor(item.getColor());

        return convertView;
    }

    /**
     * ViewHolder use to improve the performance of the grid/list
     */
    private class ViewHolder {
        private TextView nameTV;
        private View background;
    }
}
