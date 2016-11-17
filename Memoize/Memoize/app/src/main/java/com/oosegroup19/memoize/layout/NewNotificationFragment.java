package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewNotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewNotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewNotificationFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "NewNotificationFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Constructor
    public NewNotificationFragment() {}

    public static NewNotificationFragment newInstance(User user) {
        NewNotificationFragment fragment = new NewNotificationFragment();
        // owner = user;
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
        View view = inflater.inflate(R.layout.fragment_new_notification, container, false);
        getActivity().setTitle("Select Reminder Type");

        Button timeBasedNotifsButton = (Button) view.findViewById(R.id.time_based_button);
        Button locationBasedNotifsButton = (Button) view.findViewById(R.id.location_based_button);

        timeBasedNotifsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeBasedNotificationFragment fragment = new TimeBasedNotificationFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        locationBasedNotifsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationBasedNotificationFragment fragment = new LocationBasedNotificationFragment();
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
