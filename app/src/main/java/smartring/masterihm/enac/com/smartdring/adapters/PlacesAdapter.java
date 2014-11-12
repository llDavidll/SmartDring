package smartring.masterihm.enac.com.smartdring.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

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

    private final PlaceSaver mListener;

    private final ArrayAdapter<Profile> mProfiles;

    public PlacesAdapter(Context context, PlaceSaver pListener) {
        super(context, android.R.layout.simple_list_item_1);
        mInflater = LayoutInflater.from(context);
        mListener = pListener;
        mProfiles = new ProfileSpinnerAdapter(context);
    }

    public void setProfiles(List<Profile> profiles) {
        mProfiles.clear();
        mProfiles.addAll(profiles);
        notifyDataSetChanged();
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
                holder.spinner = (Spinner) convertView.findViewById(R.id.item_place_spinner);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Place item = getItem(position);
            if (item != null) {
                holder.nameTV.setText(item.getName());
                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        item.setAssociatedProfile(mProfiles.getItem(i).getId());
                        mListener.savePlace(item);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            holder.spinner.setAdapter(mProfiles);
            Profile temp = new Profile();
            temp.setId(item.getAssociatedProfile());
            holder.spinner.setSelection(mProfiles.getPosition(temp));
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
    public Place getItem(int position) {
        if (position == getCount() - 1) {
            return new Place();
        }
        return super.getItem(position);
    }

    /**
     * ViewHolder use to improve the performance of the grid/list
     */
    private class ViewHolder {
        private TextView nameTV;
        private Spinner spinner;
    }

    public void refresh(List<Place> pNewList) {
        clear();
        addAll(pNewList);
    }

    private class ProfileSpinnerAdapter extends ArrayAdapter<Profile> {

        public ProfileSpinnerAdapter(Context context) {
            super(context, android.R.layout.simple_spinner_dropdown_item);
        }

        @Override
        public int getCount() {
            return super.getCount() + 1;
        }

        @Override
        public Profile getItem(int position) {
            if (position == 0) {
                Profile p = new Profile();
                p.setName(getContext().getString(R.string.fragment_place_selectionnone));
                return p;
            }
            return super.getItem(position - 1);
        }

        @Override
        public int getPosition(Profile item) {
            if (item.getId() < 0) {
                return 0;
            }
            return super.getPosition(item) + 1;
        }
    }

    public interface PlaceSaver {
        void savePlace(Place place);
    }
}
