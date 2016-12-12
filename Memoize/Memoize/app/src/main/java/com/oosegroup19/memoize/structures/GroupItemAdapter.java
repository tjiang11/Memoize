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

public class GroupItemAdapter extends ArrayAdapter<GroupItem> {
    int resource;
    public GroupItemAdapter(Context context, int res, ArrayList<GroupItem> items) {
        super(context, res, items);
        resource = res;
    }


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

        TextView groupNameView = (TextView) lessonView.findViewById(R.id.group_name);
        TextView groupDescriptionView = (TextView) lessonView.findViewById(R.id.group_description);

        groupNameView.setText(group.getName());
        groupDescriptionView.setText(group.getDescription());

        return lessonView;
    }

}



