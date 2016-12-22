package com.oosegroup19.memoize.structures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oosegroup19.memoize.R;

import java.util.ArrayList;

/**
 * Created by smsukardi on 11/12/16.
 */

/**
 * Adapter for the ListView of locations that the user has saved
 */
public class HopkinsLocationAdapter extends ArrayAdapter<HopkinsLocationItem> {
    int resource;

    /** The adapter constructor.
     *
     * @param context The application context
     * @param res
     * @param items An ArrayList of HopkinsLocationItems
     */
    public HopkinsLocationAdapter(Context context, int res, ArrayList<HopkinsLocationItem> items) {
        super(context, res, items);
        resource = res;
    }

    /** Retrieves the view and sets it with HopkinsLocation information.
     *
     * @param position The position of the item.
     * @param convertView
     * @param parent The parent view of the item.
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout hopkinsLocationView;
        HopkinsLocationItem locationItem = getItem(position);

        if (convertView == null) {
            hopkinsLocationView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, hopkinsLocationView, true);
        } else {
            hopkinsLocationView = (LinearLayout) convertView;
        }

        // Retrieves UI components
        TextView locationNameView = (TextView) hopkinsLocationView.findViewById(R.id.hopkins_location_name);
        ImageView locationImageView = (ImageView) hopkinsLocationView.findViewById(R.id.hopkins_location_image);

        locationNameView.setText(locationItem.getLocationName());

        int resId = getContext().getResources().getIdentifier(locationItem.getImageRef(), "drawable", getContext().getPackageName());
        locationImageView.setImageResource(resId);

        return hopkinsLocationView;
    }

}

