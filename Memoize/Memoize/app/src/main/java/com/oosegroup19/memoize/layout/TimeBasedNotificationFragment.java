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
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeBasedNotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeBasedNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeBasedNotificationFragment extends BaseFragment {
    Context context = HomePageActivity.getContext();
    public final static String FRAGMENTNAME = "TimeBasedNotificationFragment";
    private final String fragmentName = FRAGMENTNAME;

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

    Calendar calendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            notificationYear = year;
            notificationMonth = monthOfYear + 1;
            notificationDay = dayOfMonth;

            dateTextField.setText("Date: " + notificationMonth + "/" + notificationDay + "/" + notificationYear);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            notificationHour = hourOfDay;
            notificationMinute = minute;

            timeTextField.setText("Time: " + notificationHour + ":" + (notificationMinute < 10 ? "0" + notificationMinute: notificationMinute));
        }
    };

    public String getFragmentName(){
        return this.fragmentName;
    }

    public static TimeBasedNotificationFragment newInstance(double latitude, double longitude) {
        TimeBasedNotificationFragment fragment = new TimeBasedNotificationFragment();
        eventLatitude = latitude;
        eventLongitude = longitude;
        return fragment;
    }

    public static TimeBasedNotificationFragment newInstance(double latitude, double longitude, String name, String loc, String description) {
        TimeBasedNotificationFragment fragment = new TimeBasedNotificationFragment();
        eventLatitude = latitude;
        eventLongitude = longitude;

        eventName = name;
        eventLocation = loc;
        eventDescription = description;

        return fragment;

    }

    public TimeBasedNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_time_based_notification, container, false);

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

        eventNameField.setText(eventName);
        eventLocationNameField.setText(eventLocation);
        eventDescriptionField.setText(eventDescription);

        chooseDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), onDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        chooseTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), onTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

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

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventNameField.getText().toString().equals("") || eventLocationNameField.getText().toString().equals("") ||
                        notificationMinute == -1 || notificationHour == -1 || notificationDay == -1 ||
                        notificationMonth == -1 || notificationYear == -1) {
                    makeToast("One or more of your fields has not been filled.");
                } else if (!notifyTooFarCheckBox.isChecked()){
                    String timeToSend = String.format("%04d-%02d-%02dT%02d:%02d:00", notificationYear, notificationMonth, notificationDay,
                            notificationHour, notificationMinute);

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

                    System.out.println("Time that is sent to api call: " + timeToSend);
                    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

                    //make api call to create a new event!
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
                    DecimalFormat df_loc = new DecimalFormat("#.########"); //for max 8 digit latitudes/longitudes

                    String timeToSend = String.format("%04d-%02d-%02dT%02d:%02d:00", notificationYear, notificationMonth, notificationDay,
                            notificationHour, notificationMinute);
                    Log.i("original time to send", timeToSend);
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

                    Log.i("Time to api last res", dateTimeNew);

                    System.out.println("Time that is sent to api call: " + dateTimeNew.toString());
                    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
                    //make api call to create a new event!
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
