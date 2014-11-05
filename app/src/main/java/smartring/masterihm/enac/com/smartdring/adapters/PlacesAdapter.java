package smartring.masterihm.enac.com.smartdring.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import smartring.masterihm.enac.com.smartdring.R;
import smartring.masterihm.enac.com.smartdring.data.Place;
import smartring.masterihm.enac.com.smartdring.data.Profile;

/**
 * Created by David on 13/10/2014.
 * <p/>
 * Adapter used to display a list of places.
 */
public class PlacesAdapter extends ArrayAdapter<Place> {

    /**
     * Layout inflater used to create the item views.
     */
    private final LayoutInflater mInflater;

    public PlacesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (position >= getCount() - 1) {
            // Special view for the last item (add new)
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_place_specialadd, parent, false);
            }
        } else {
            // Normal view for a profile.
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_place, parent, false);

                holder = new ViewHolder();
                holder.nameTV = (TextView) convertView.findViewById(android.R.id.text1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Place item = getItem(position);
            if (item != null) {
                holder.nameTV.setText(item.getName());
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
    Place getItem(int position) {
        if (position == getCount() - 1) {
            return null;
        }
        return super.getItem(position);
    }

    /**
     * ViewHolder use to improve the performance of the grid/list
     */
    private class ViewHolder {
        private TextView nameTV;
    }
}
