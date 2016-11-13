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
 * {@link ReminderDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReminderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderDetailFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "ReminderDetailFragment";
    private final String fragmentName = FRAGMENTNAME;

    private OnFragmentInteractionListener mListener;

    public ReminderDetailFragment() {
        // Required empty public constructor
    }

    public static ReminderDetailFragment newInstance(User user) {
        ReminderDetailFragment fragment = new ReminderDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_reminder_detail, container, false);
        getActivity().setTitle("Reminder Detail");

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

    public String getFragmentName() {
        return this.fragmentName;
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
}
