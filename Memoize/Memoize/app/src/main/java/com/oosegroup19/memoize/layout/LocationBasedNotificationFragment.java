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

import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.User;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationBasedNotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LocationBasedNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationBasedNotificationFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "LocationBasedNotificationFragment";
    private final String fragmentName = FRAGMENTNAME;

    private static double eventLatitude = -1;
    private static double eventLongitude = -1;
    private static String locationName = "";


    public String getFragmentName(){
        return this.fragmentName;
    }

    public static LocationBasedNotificationFragment newInstance(String loc, double latitude, double longitude) {
        LocationBasedNotificationFragment fragment = new LocationBasedNotificationFragment();
        locationName = loc;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_based_notification, container, false);
        getActivity().setTitle("Testing2");

        Button chooseHopkinsLocationsButton = (Button) view.findViewById(R.id.choose_hopkins_loc_button);
        Button locationBasedNotifsButton = (Button) view.findViewById(R.id.drop_pin_button);
        Button saveButton = (Button) view.findViewById(R.id.save_button_location_based_notif);

        TextView eventLocationNameField = (TextView) view.findViewById(R.id.event_location_locationbased);

        eventLocationNameField.setText(locationName);

        chooseHopkinsLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HopkinsLocationsFragment fragment = new HopkinsLocationsFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fieldsFilled()) {
                    makeFailureToast();
                } else {
                    //make api call to create a new event!


                    NewNotificationFragment fragment = new NewNotificationFragment();
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_main, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        //TODO: Implement savebutton and pushing info to database
        return view;
    }

    public boolean fieldsFilled() {
        return true;
    }

    public void makeFailureToast() {
        Log.i("LocationBasedNotificationFragment", "failure to save event");
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
