package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.LocationReminderItem;
import com.oosegroup19.memoize.structures.ReminderItem;
import com.oosegroup19.memoize.structures.TimeReminderItem;
import com.oosegroup19.memoize.structures.User;

import org.w3c.dom.Text;

/**
 * Class for showing ReminderDetails (ie. more information associated with the reminder
 * from the reminder log).
 */
public class ReminderDetailFragment extends BaseFragment {
    //The fragment name variables.
    public final static String FRAGMENTNAME = "ReminderDetailFragment";
    private final String fragmentName = FRAGMENTNAME;

    //The ReminderItem we are showing details for.
    private static ReminderItem reminderItem;

    /**ReminderDetailFragment required empty constructor.
     */
    public ReminderDetailFragment() {}

    /**The ReminderDetailFragment constructor called.
     *
     * @param loc The ReminderItem to be shown details of.
     * @return The ReminderDetailFragment
     */
    public static ReminderDetailFragment newInstance(ReminderItem loc) {
        ReminderDetailFragment fragment = new ReminderDetailFragment();
        reminderItem = loc;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder_detail, container, false);

        //Retrieve the TextViews from the UI.
        TextView taskName = (TextView) view.findViewById(R.id.task_name_detail);
        TextView taskLocation = (TextView) view.findViewById(R.id.task_location_detail);
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description_detail);
        TextView latitudeOrStartTime = (TextView) view.findViewById(R.id.task_latitude_or_start_time);
        TextView longitudeOrEndTime = (TextView) view.findViewById(R.id.task_longitude_or_end_time);

        //Set the name, location, and description to the appropriate fields from the ReminderItem.
        taskName.setText(reminderItem.getName());
        taskLocation.setText(reminderItem.getLocationDescriptor());
        taskDescription.setText(reminderItem.getDescription());

        //If it is a LocationReminderItem, set the fields to show latitude and longitude.
        //if it is a TimeReminderItem, sent the fields to show the time the alarm is to be put off.
        if (reminderItem instanceof LocationReminderItem) {
            LocationReminderItem temp = (LocationReminderItem) reminderItem;
            latitudeOrStartTime.setText("Latitude: " + String.valueOf(temp.getLatitude()));
            longitudeOrEndTime.setText("Longitude: " + String.valueOf(temp.getLongitude()));
        } else if (reminderItem instanceof TimeReminderItem) {
            TimeReminderItem temp = (TimeReminderItem) reminderItem;
            latitudeOrStartTime.setText("Time: " + String.valueOf(temp.getTime()));
            longitudeOrEndTime.setText("");
        }

        //The Back button
        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_button);

        //Goes back to the ReminderLog.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initiates a transition to a ReminderLogFragment
                ReminderLogFragment fragment = new ReminderLogFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public String getFragmentName() {
        return this.fragmentName;
    }

}
