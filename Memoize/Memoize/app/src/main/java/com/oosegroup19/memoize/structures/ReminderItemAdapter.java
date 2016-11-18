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
 * Created by smsukardi on 11/12/16.
 */

//Adapter for the ListView of locations that the user has saved
public class ReminderItemAdapter extends ArrayAdapter<ReminderItem> {
    int resource;

    public ReminderItemAdapter(Context context, int res, ArrayList<ReminderItem> items) {
        super(context, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout lessonView;
        ReminderItem reminder = getItem(position);

        if (convertView == null) {
            lessonView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, lessonView, true);
        } else {
            lessonView = (LinearLayout) convertView;
        }

        TextView taskNameView = (TextView) lessonView.findViewById(R.id.task_name);
        TextView taskLocationView = (TextView) lessonView.findViewById(R.id.task_location);
        TextView latitudeView = (TextView) lessonView.findViewById(R.id.task_latitude);
        TextView longitudeView = (TextView) lessonView.findViewById(R.id.task_longitude);

        taskNameView.setText(reminder.getName());
        taskLocationView.setText("Event Location: " + reminder.getLocationDescriptor());

        if (reminder instanceof LocationReminderItem) {
            LocationReminderItem tempReminder = (LocationReminderItem) reminder;
            latitudeView.setText("Latitude: " + tempReminder.getLatitude());
            longitudeView.setText("Longitude: " + tempReminder.getLongitude());

        } else if (reminder instanceof TimeReminderItem) {
            TimeReminderItem tempReminder = (TimeReminderItem) reminder;
            latitudeView.setText("Start Time: " + tempReminder.getTime());
            //longitudeView.setText("End Time: " + tempReminder.getEndTime());
        }

        return lessonView;
    }

}

