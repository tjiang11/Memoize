package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.User;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimeBasedNotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimeBasedNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimeBasedNotificationFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "TimeBasedNotificationFragment";
    private final String fragmentName = FRAGMENTNAME;

    private int PORT = 8000;
    private String baseURL = "http://10.0.3.2:" + PORT;

    public String getFragmentName(){
        return this.fragmentName;
    }

    public static TimeBasedNotificationFragment newInstance(User user) {
        TimeBasedNotificationFragment fragment = new TimeBasedNotificationFragment();
        // owner = user;
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
        final TextView eventDayField = (TextView) view.findViewById(R.id.event_day_timebased);
        final TextView eventTimeField = (TextView) view.findViewById(R.id.event_time_timebased);

        Button saveButton = (Button) view.findViewById(R.id.save_button_time_based_notif);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eventNameField.getText().toString().equals("") || eventLocationNameField.getText().toString().equals("")
                        || eventDayField.getText().equals("") || eventTimeField.getText().equals("")) {
                    makeToast("One or more of your fields has not been filled.");
                } else {

                    //make api call to create a new event!
                    AndroidNetworking.post(baseURL + "/users/1/locationreminders/")
                            .addBodyParameter("name", eventNameField.getText().toString())
                            .addBodyParameter("description", eventDescriptionField.getText().toString())
                            .addBodyParameter("location_descriptor", eventLocationNameField.getText().toString())
                            .addBodyParameter("time", eventTimeField.getText().toString())
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    //TODO: Make sure this works when connected to the server; that is, that the toast is there for the appropriate
                                    //amount of time even when it transitions to the home fragment.
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
                                    Log.e("tag", "noooooo");
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
