package com.oosegroup19.memoize.layout;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.oosegroup19.memoize.activity.HomePageActivity.PREFS_NAME;


/**
 * The TimeBasedNotificationFragment class contains the logic for
 * creating a new TimeBasedNotification, as well as a new
 * LastResortNotification.
 */
public class TimeBasedNotificationFragment extends BaseFragment {
    //Retrieves the context
    Context context = HomePageActivity.getContext();

    //The name of the fragment for referencing
    public final static String FRAGMENTNAME = "TimeBasedNotificationFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance Fields
    private TextView dateTextField;
    private TextView timeTextField;

    private int notificationHour = -1;
    private int notificationMinute = -1;

    private int notificationYear = -1;
    private int notificationMonth = -1;
    private int notificationDay = -1;

    private static double eventLatitude = -1;
    private static double eventLongitude = -1;

    private static String eventName = "";
    private static String eventLocation = "";
    private static String eventDescription = "";

    // An instance of the calendar, for time
    Calendar calendar = Calendar.getInstance();

    // Creates a dateSetListener object so that the user can later choose a specific date
    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            notificationYear = year;
            notificationMonth = monthOfYear + 1;
            notificationDay = dayOfMonth;

            //Sets the text field to show the chosen date once a date has been chosen
            dateTextField.setText("Date: " + notificationMonth + "/" + notificationDay + "/" + notificationYear);
        }
    };

    //Creates a timeSetListener object so that the user can later choose a specific time
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            notificationHour = hourOfDay;
            notificationMinute = minute;

            //Sets the text field to show the chosen time when a time has been chosen
            timeTextField.setText("Time: " + notificationHour + ":" + (notificationMinute < 10 ? "0" + notificationMinute: notificationMinute));
        }
    };

    //Returns the name of the fragment
    public String getFragmentName() {
        return this.fragmentName;
    }

    /** A basic constructor for a TimeBasedNotificationFragment
     */
    public static TimeBasedNotificationFragment newInstance(double latitude, double longitude) {
        TimeBasedNotificationFragment fragment = new TimeBasedNotificationFragment();
        eventLatitude = latitude;
        eventLongitude = longitude;
        return fragment;
    }

    /** A constructor for when creating a TimeBasedNotificationFragment after the user has
     *  dropped a pin or chosen a Hopkins Location. Persists data so that if it is called again
     *  the filled fields remain filled.
     */
    public static TimeBasedNotificationFragment newInstance(double latitude, double longitude, String name, String loc, String description) {
        TimeBasedNotificationFragment fragment = new TimeBasedNotificationFragment();
        eventLatitude = latitude;
        eventLongitude = longitude;

        eventName = name;
        eventLocation = loc;
        eventDescription = description;

        return fragment;

    }

    /** Required public constructor.
     */
    public TimeBasedNotificationFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_based_notification, container, false);

        //Retrieves the UI elements
        final TextView eventNameField = (TextView) view.findViewById(R.id.event_name_timebased);
        final TextView eventLocationNameField = (TextView) view.findViewById(R.id.event_location_timebased);
        final TextView eventDescriptionField = (TextView) view.findViewById(R.id.event_description_timebased);

        dateTextField = (TextView) view.findViewById(R.id.dateText);
        timeTextField = (TextView) view.findViewById(R.id.timeText);

        final Button chooseHopkinsLocationsButton = (Button) view.findViewById(R.id.choose_hopkins_loc_button_time_based);
        final Button chooseDateButton = (Button) view.findViewById(R.id.choose_date_button);
        final Button chooseTimeButton = (Button) view.findViewById(R.id.choose_time_button);
        final Button dropPinButton = (Button) view.findViewById(R.id.drop_pin_button_time_based);
        final Button saveButton = (Button) view.findViewById(R.id.save_button_time_based_notif);

        final CheckBox notifyTooFarCheckBox = (CheckBox) view.findViewById(R.id.notifyTooFar);

        //Sets the text to eventName, eventLocation, and eventDescription (for if they had
        //been filled before and the notification is segued to
        eventNameField.setText(eventName);
        eventLocationNameField.setText(eventLocation);
        eventDescriptionField.setText(eventDescription);

        //Creates a date picker dialog when the chooseDateButton is selected
        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Creates a time picker dialog when the chooseTimeButton is selected
        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        //Segues to a HopkinsLocationsFragment when the Choose Hopkins Location button is selected
        chooseHopkinsLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HopkinsLocationsFragment fragment = HopkinsLocationsFragment.newInstance("time", eventNameField.getText().toString(),
                        eventLocationNameField.getText().toString(), eventDescriptionField.getText().toString());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //Segues to a DropPinFragment when the Drop Pin button is selected
        dropPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go back to home fragment
                DropPinFragment fragment = DropPinFragment.newInstance("time", eventNameField.getText().toString(),
                        eventLocationNameField.getText().toString(), eventDescriptionField.getText().toString());

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //Makes the REST api call when the "Save" button is sected
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Error checks if any fields are empty
                if (eventNameField.getText().toString().equals("") || eventLocationNameField.getText().toString().equals("") ||
                        notificationMinute == -1 || notificationHour == -1 || notificationDay == -1 ||
                        notificationMonth == -1 || notificationYear == -1) {
                    makeToast("One or more of your fields has not been filled.");

                } else if (!notifyTooFarCheckBox.isChecked()){
                    //Formats the time to send into a string from the variables filled in
                    String timeToSend = String.format("%04d-%02d-%02dT%02d:%02d:00", notificationYear, notificationMonth, notificationDay,
                            notificationHour, notificationMinute);

                    //Creates dateformat object to convert the time into greenwich mean time.
                    DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    String date = timeToSend.substring(0, 10);
                    String time = timeToSend.substring(11);
                    String dateTimeNew = "";
                    String datetime = date + time;
                    try {
                        Date oldDate = df.parse(datetime);
                        Log.i("tag send time", "original " + timeToSend);
                        Date newDate = new Date(oldDate.getTime() + 5 * 3600 * 1000);
                        timeToSend = df.format(newDate);
                        String dateNew = timeToSend.substring(0, 10);
                        String timeNew = timeToSend.substring(10);
                        dateTimeNew = dateNew + "T" + timeNew;
                        Log.i("tag", dateTimeNew);
                    } catch (ParseException e) {
                        Log.e("tag", e.getMessage());
                    }

                    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

                    //Make the api call to create a new TimeBasedNotification in the backend
                    AndroidNetworking.post(HomePageActivity.baseURL + "/users/" + settings.getString("user_id", "0") + "/timereminders/")
                            .addBodyParameter("name", eventNameField.getText().toString())
                            .addBodyParameter("description", eventDescriptionField.getText().toString())
                            .addBodyParameter("location_descriptor", eventLocationNameField.getText().toString())
                            .addBodyParameter("time", dateTimeNew)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    makeToast("Your time based notification has been successfully created!");
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
                                    Log.e("tag", "A networking error occurred.");
                                    Log.e("tag", anError.getMessage());
                                    makeToast("Your Notification could not be saved to the database. Please try again with " +
                                            "a more secure connection.");
                                }
                            });
                } else {
                    //A DecimalFormat object, so that the latidudes/longitudes are maximum 8 digits
                    DecimalFormat df_loc = new DecimalFormat("#.########"); //for max 8 digit latitudes/longitudes

                    //Formats the string to send greenwich mean time
                    String timeToSend = String.format("%04d-%02d-%02dT%02d:%02d:00", notificationYear, notificationMonth, notificationDay,
                            notificationHour, notificationMinute);

                    DateFormat df_last_res = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    String date = timeToSend.substring(0, 10);
                    String time = timeToSend.substring(11);
                    String dateTimeNew = "";
                    String datetime = date + time;
                    try {
                        Date oldDate = df_last_res.parse(datetime);
                        Log.i("tag send time", "original " + timeToSend.toString());
                        Date newDate = new Date(oldDate.getTime() + 5 * 3600 * 1000);
                        timeToSend = df_last_res.format(newDate);
                        String dateNew = timeToSend.substring(0, 10);
                        String timeNew = timeToSend.substring(10);
                        dateTimeNew = dateNew + "T" + timeNew;
                        Log.i("tag", dateTimeNew);
                    } catch (ParseException e) {
                        Log.e("tag", e.getMessage());
                    }

                    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

                    //Makes the RESTful API call to create a new event
                    AndroidNetworking.post(HomePageActivity.baseURL + "/users/" + settings.getString("user_id", "0") + "/lastresortreminders/")
                            .addBodyParameter("name", eventNameField.getText().toString())
                            .addBodyParameter("description", eventDescriptionField.getText().toString())
                            .addBodyParameter("location_descriptor", eventLocationNameField.getText().toString())
                            .addBodyParameter("time", dateTimeNew)
                            .addBodyParameter("longitude", df_loc.format(eventLongitude))
                            .addBodyParameter("latitude", df_loc.format(eventLatitude))
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    makeToast("Your last resort notification has been successfully created!");
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
                                    Log.e("tag", "A networking error occurred.");
                                    Log.e("tag", anError.getMessage());
                                    makeToast("Your Notification could not be saved to the database. Please try again with " +
                                            "a more secure connection.");
                                }
                            });
                }
            }
        });

        return view;
    }

    /**Creates a toast to show to the user.
     * @param message The contents of the toast to be made
     */
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
