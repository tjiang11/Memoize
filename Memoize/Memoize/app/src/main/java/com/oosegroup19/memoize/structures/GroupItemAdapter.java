package com.oosegroup19.memoize.structures;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oosegroup19.memoize.R;

import java.util.ArrayList;

/**
 * Created by Tony on 12/11/2016.
 */

/**A GroupItemAdapter for viewing groups.
 */
public class GroupItemAdapter extends ArrayAdapter<GroupItem> {
    int resource;

    /** The GroupItemAdapter constructor.
     *
     * @param context The application constructor
     * @param res
     * @param items An array of of GroupItems.
     */
    public GroupItemAdapter(Context context, int res, ArrayList<GroupItem> items) {
        super(context, res, items);
        resource = res;
    }

    /** Retrieves the view.
     *
     * @param position The position of the view.
     * @param convertView
     * @param parent The parent of the view.
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout lessonView;
        GroupItem group = getItem(position);

        if (convertView == null) {
            lessonView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, lessonView, true);
        } else {
            lessonView = (LinearLayout) convertView;
        }

        //Retrieves UI components
        TextView groupNameView = (TextView) lessonView.findViewById(R.id.group_name);
        TextView groupDescriptionView = (TextView) lessonView.findViewById(R.id.group_description);

        //Sets the text to the group name and description
        groupNameView.setText(group.getName());
        groupDescriptionView.setText(group.getDescription());

        return lessonView;
    }

}



