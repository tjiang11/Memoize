package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.activity.HomePageActivity;
import com.oosegroup19.memoize.structures.User;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Calendar;

import static com.oosegroup19.memoize.activity.HomePageActivity.PREFS_NAME;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationBasedNotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationBasedNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationBasedNotificationFragment extends BaseFragment {
    Context context;

    public final static String FRAGMENTNAME = "LocationBasedNotificationFragment";
    private final String fragmentName = FRAGMENTNAME;

    private static double eventLatitude = -1;
    private static double eventLongitude = -1;

    private static String eventName = "";
    private static String eventLocationName = "";
    private static String eventDescription = "";
    private static int eventRadius = 100;

    private int startHour = -1;
    private int startMinute = -1;

    private int endHour = -1;
    private int endMinute = -1;

//    private TextView currStartTimeTextField;
//    private TextView currEndTimeTextField;

    Calendar calendar = Calendar.getInstance();

    TimePickerDialog.OnTimeSetListener onTimeSetListenerBegin = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            startHour = hourOfDay;
            startMinute = minute;

            //currStartTimeTextField.setText("Start Hour: " + startHour + ":" + (startMinute < 10 ? "0" + startMinute : startMinute));
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListenerEnd = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            endHour = hourOfDay;
            endMinute = minute;

            //currEndTimeTextField.setText("End Hour: " + endHour + ":" + (endMinute < 10 ? "0" + endMinute: endMinute));
        }
    };

    public String getFragmentName(){
        return this.fragmentName;
    }

    public static LocationBasedNotificationFragment newInstance(String loc, double latitude, double longitude) {
        LocationBasedNotificationFragment fragment = new LocationBasedNotificationFragment();
        eventLocationName = loc;
        eventLatitude = latitude;
        eventLongitude = longitude;

        return fragment;
    }

    public static LocationBasedNotificationFragment newInstance(String loc, double latitude, double longitude, String name, String description, int radius) {
        LocationBasedNotificationFragment fragment = new LocationBasedNotificationFragment();
        eventName = name;
        eventLocationName = loc;
        eventDescription = description;
        eventRadius = radius;
        eventLatitude = latitude;
        eventLongitude = longitude;

        // owner = user;
        return fragment;
    }

    public static LocationBasedNotificationFragment newInstance(User user) {
        LocationBasedNotificationFragment fragment = new LocationBasedNotificationFragment();
        // owner = user;
        return fragment;
    }

    public LocationBasedNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = HomePageActivity.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_based_notification, container, false);

        final Button chooseHopkinsLocationsButton = (Button) view.findViewById(R.id.choose_hopkins_loc_button);
        final Button dropPinButton = (Button) view.findViewById(R.id.drop_pin_button);
        final Button saveButton = (Button) view.findViewById(R.id.save_button_location_based_notif);

//        final Button chooseStartTimeButton = (Button) view.findViewById(R.id.choose_start_time_button);
//        final Button chooseEndTimeButton = (Button) view.findViewById(R.id.choose_end_time_button);

        final TextView eventNameField = (TextView) view.findViewById(R.id.event_name_locationbased);
        final TextView eventLocationNameField = (TextView) view.findViewById(R.id.event_location_locationbased);
        final TextView eventDescriptionField = (TextView) view.findViewById(R.id.event_description_locationbased);
        final TextView currLatLongField = (TextView) view.findViewById(R.id.currLocationTextView);

        final TextView radiusTextView = (TextView) view.findViewById(R.id.eventRadiusView);
        final SeekBar radiusBar = (SeekBar) view.findViewById(R.id.radius_seek_bar);
//
//        currStartTimeTextField = (TextView) view.findViewById(R.id.startTimeText);
//        currEndTimeTextField = (TextView) view.findViewById(R.id.endTimeText);

        //Initialize name, location, description, and radius bar to what was passed in the constructor/the default
        eventNameField.setText(eventName);
        eventLocationNameField.setText(eventLocationName);
        eventDescriptionField.setText(eventDescription);
        radiusTextView.setText("Event Radius: " + eventRadius);
        radiusBar.setProgress(eventRadius);

        currLatLongField.setText(eventLatitude == -1 ? "No Location Selected" : "Latitude: " + eventLatitude + " " + " Longitude: " + eventLongitude);

        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusTextView.setText("Event Radius: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        chooseHopkinsLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HopkinsLocationsFragment fragment = HopkinsLocationsFragment.newInstance("location",
                        eventNameField.getText().toString(), eventLocationNameField.getText().toString(), eventDescriptionField.getText().toString(), radiusBar.getProgress());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        dropPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to home fragment
                DropPinFragment fragment = DropPinFragment.newInstance("location",
                        eventNameField.getText().toString(), eventLocationNameField.getText().toString(), eventDescriptionField.getText().toString(), radiusBar.getProgress());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

//        chooseStartTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new TimePickerDialog(getActivity(), onTimeSetListenerBegin, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
//            }
//        });
//
//        chooseEndTimeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new TimePickerDialog(getActivity(), onTimeSetListenerEnd, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
//            }
//        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventNameField.getText().toString().equals("") || eventLocationNameField.getText().toString().equals("")
                        || eventLatitude == -1 || eventLongitude == -1) {
                    makeToast("One or more of your fields has not been filled.");
                } else {

                    DecimalFormat df = new DecimalFormat("#.########"); //for max 8 digit latitudes/longitudes

//                    String startTime = ((startHour < 10) ? ("0" + startHour) : startHour) + ":" + ((startMinute < 10) ? ("0" + startMinute) : startMinute) + ":00";
//                    String endTime = ((endHour < 10) ? ("0" + endHour) : endHour) + ":" + ((endMinute < 10) ? ("0" + endMinute) : endMinute) + ":00";

                    //Make sure that startTime < endTime
//                    if (endHour < startHour) {
//                        makeToast("Your end time must be after your start time.");
//                    } else if (endHour == startHour && endMinute <= startMinute) {
//                        makeToast("Your end time must be after your start time.");
//                    } else {
                        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
                        //make api call to create a new event!
                        AndroidNetworking.post(HomePageActivity.baseURL + "/users/" + settings.getString("user_id", "0") + "/locationreminders/")
                                .addBodyParameter("name", eventNameField.getText().toString())
                                .addBodyParameter("description", eventDescriptionField.getText().toString())
                                .addBodyParameter("location_descriptor", eventLocationNameField.getText().toString())

//                                .addBodyParameter("start_time", startTime)
//                                .addBodyParameter("end_time", endTime)

                                .addBodyParameter("longitude", df.format(eventLongitude))
                                .addBodyParameter("latitude", df.format(eventLatitude))
                                .addBodyParameter("radius", Integer.toString(radiusBar.getProgress()))
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //TODO: Make sure this works when connected to the server; that is, that the toast is there for the appropriate
                                        //amount of time even when it transitions to the home fragment.
                                        makeToast("Your location based notification has been successfully created!");
                                        Log.i("tag", "Success");
                                        Log.i("tag", response.toString());

                                        //Go back to home fragment
                                        NewNotificationFragment fragment = new NewNotificationFragment();
                                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame_main, fragment);
                                        fragmentTransaction.addToBackStack(null);
                                        fragmentTransaction.commit();
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        Log.e("tag", "noooooo");
                                        Log.e("tag", anError.toString());
                                        makeToast("Your Notification could not be saved to the database. Please try again with " +
                                                "a more secure connection.");
                                    }
                                });
                   // }
                }
            }
        });

        return view;
    }

    public void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
