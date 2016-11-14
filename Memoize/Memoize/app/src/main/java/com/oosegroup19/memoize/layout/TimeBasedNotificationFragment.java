package com.oosegroup19.memoize.layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.User;


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

    private OnFragmentInteractionListener mListener;


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
        getActivity().setTitle("Testing3");

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
        void onFragmentInteraction(Uri uri);
    }
}