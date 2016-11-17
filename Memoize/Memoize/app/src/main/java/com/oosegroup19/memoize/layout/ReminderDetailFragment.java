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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReminderDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReminderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderDetailFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "ReminderDetailFragment";
    private final String fragmentName = FRAGMENTNAME;

    private static ReminderItem reminderItem;

    public ReminderDetailFragment() {
        // Required empty public constructor
    }

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

        // TODO: Figure out why doesn't this work?
        // Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        // toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        TextView taskName = (TextView) view.findViewById(R.id.task_name_detail);
        TextView taskLocation = (TextView) view.findViewById(R.id.task_location_detail);
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description_detail);
        TextView latitudeOrStartTime = (TextView) view.findViewById(R.id.task_latitude_or_start_time);
        TextView longitudeOrEndTime = (TextView) view.findViewById(R.id.task_longitude_or_end_time);

        taskName.setText(reminderItem.getEvent());
        taskLocation.setText(reminderItem.getLocation());
        taskDescription.setText(reminderItem.getDescription());

        if (reminderItem instanceof LocationReminderItem) {
            LocationReminderItem temp = (LocationReminderItem) reminderItem;
            latitudeOrStartTime.setText("Latitude: " + String.valueOf(temp.getLatitude()));
            longitudeOrEndTime.setText("Longitude: " + String.valueOf(temp.getLongitude()));
        } else if (reminderItem instanceof TimeReminderItem) {
            TimeReminderItem temp = (TimeReminderItem) reminderItem;
            latitudeOrStartTime.setText("Start Time: " + String.valueOf(temp.getStartTime()));
            longitudeOrEndTime.setText("");
        }

        ImageButton backButton = (ImageButton) view.findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
