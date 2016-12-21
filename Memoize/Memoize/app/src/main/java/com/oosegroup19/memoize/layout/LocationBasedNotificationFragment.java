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
 * Use the {@link LocationBasedNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 * This fragment allows a user to create a LocationBasedNotification.
 */
public class LocationBasedNotificationFragment extends BaseFragment {
    //The context of the fragment
    Context context;

    //Name of fragment for moving back and forth between fragments
    public final static String FRAGMENTNAME = "LocationBasedNotificationFragment";
    private final String fragmentName = FRAGMENTNAME;

    //The variables relevant to LocationBasedNotifications
    //to be sent in the API calls
    private static double eventLatitude = -1;
    private static double eventLongitude = -1;

    private static String eventName = "";
    private static String eventLocationName = "";
    private static String eventDescription = "";
    private static int eventRadius = 100;


    /**
     * Returns the name of the current fragment.
     * @return The name of the fragment.
     */
    public String getFragmentName(){
        return this.fragmentName;
    }

    /**LocationBasedNotification constructor.
     *
     * @param loc The name of the location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @return The fragment to be initiated.
     */
    public static LocationBasedNotificationFragment newInstance(String loc, double latitude, double longitude) {
        LocationBasedNotificationFragment fragment = new LocationBasedNotificationFragment();
        eventLocationName = loc;
        eventLatitude = latitude;
        eventLongitude = longitude;

        return fragment;
    }

    /**LocationBasedNotification constructor for returning from the drop pin
     * or hopkins locations pages (so that the text fields are filled out and do
     * not need to be re-filled).
     *
     * @param loc The name of the location.
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param name The event that is to be occurring that a locationbasednotification is being created for.
     * @param description The description of the event.
     * @param radius The radius associated with the event.
     * @return The fragment to be initiated.
     */
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

    /**Basic LocationBasedNotificationFragment constructor.
     * @param user The user.
     * @return The fragment to be initiated.
     */
    public static LocationBasedNotificationFragment newInstance(User user) {
        LocationBasedNotificationFragment fragment = new LocationBasedNotificationFragment();
        return fragment;
    }

    /**The default public constructor for the LocationBasedNotificationFragment.
     */
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

        // Inflates the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_based_notification, container, false);

        // Retrieves the buttons from the screen.
        final Button chooseHopkinsLocationsButton = (Button) view.findViewById(R.id.choose_hopkins_loc_button);
        final Button dropPinButton = (Button) view.findViewById(R.id.drop_pin_button);
        final Button saveButton = (Button) view.findViewById(R.id.save_button_location_based_notif);

        final TextView eventNameField = (TextView) view.findViewById(R.id.event_name_locationbased);
        final TextView eventLocationNameField = (TextView) view.findViewById(R.id.event_location_locationbased);
        final TextView eventDescriptionField = (TextView) view.findViewById(R.id.event_description_locationbased);
        final TextView currLatLongField = (TextView) view.findViewById(R.id.currLocationTextView);

        final TextView radiusTextView = (TextView) view.findViewById(R.id.eventRadiusView);
        final SeekBar radiusBar = (SeekBar) view.findViewById(R.id.radius_seek_bar);

        //Initialize name, location, description, and radius bar to what was passed in the constructor/the default
        eventNameField.setText(eventName);
        eventLocationNameField.setText(eventLocationName);
        eventDescriptionField.setText(eventDescription);
        radiusTextView.setText("Event Radius: " + eventRadius);
        radiusBar.setProgress(eventRadius);

        //Set the text field to the latitude and longitude if they exist
        currLatLongField.setText(eventLatitude == -1 ? "No Location Selected" : "Latitude: " + eventLatitude + " " + " Longitude: " + eventLongitude);

        //Listener for the radius bar
        radiusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            //Change the text to the current radius selected on the seek bar.
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusTextView.setText("Event Radius: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //Listener for the ChooseHopkinsLocationButton
        chooseHopkinsLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initiates a transition to a HopkinsLocationFragment
                HopkinsLocationsFragment fragment = HopkinsLocationsFragment.newInstance("location",
                        eventNameField.getText().toString(), eventLocationNameField.getText().toString(), eventDescriptionField.getText().toString(), radiusBar.getProgress());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //Listener for the DropPinButton
        dropPinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Initiates a transition to a DropPinFragment
                DropPinFragment fragment = DropPinFragment.newInstance("location",
                        eventNameField.getText().toString(), eventLocationNameField.getText().toString(), eventDescriptionField.getText().toString(), radiusBar.getProgress());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //Listener for the Save Button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks to make sure the fields are not empty
                if (eventNameField.getText().toString().equals("") || eventLocationNameField.getText().toString().equals("")
                        || eventLatitude == -1 || eventLongitude == -1) {
                    makeToast("One or more of your fields has not been filled.");
                } else {
                    //DecimalFormat for formatting latitude & longitude
                    DecimalFormat df = new DecimalFormat("#.########"); //for max 8 digit latitudes/longitudes

                        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
                        //makes a RESTful API call to create a new event
                        AndroidNetworking.post(HomePageActivity.baseURL + "/users/" + settings.getString("user_id", "0") + "/locationreminders/")
                                .addBodyParameter("name", eventNameField.getText().toString())
                                .addBodyParameter("description", eventDescriptionField.getText().toString())
                                .addBodyParameter("location_descriptor", eventLocationNameField.getText().toString())
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
                }
            }
        });

        return view;
    }

    //Creates a toast
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
