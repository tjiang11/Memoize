package com.oosegroup19.memoize;

import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

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

    private OnFragmentInteractionListener mListener;

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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getFragmentName() {
        return this.fragmentName;
    }
}
