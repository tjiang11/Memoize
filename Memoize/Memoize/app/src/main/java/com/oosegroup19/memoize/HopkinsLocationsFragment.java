package com.oosegroup19.memoize;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HopkinsLocationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HopkinsLocationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HopkinsLocationsFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "HopkinsLocationsFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance Fields
    private ListView hopkinsLocationsList;

// TODO: THESE MUST BE CHANGED
//    protected static ArrayList<ReminderItem> hopkinsLocationItems;
//    protected static ReminderItemAdapter aa;

    private OnFragmentInteractionListener mListener;

    public String getFragmentName(){
        return this.fragmentName;
    }

    public static HopkinsLocationsFragment newInstance(User user) {
        HopkinsLocationsFragment fragment = new HopkinsLocationsFragment();
        // owner = user;
        return fragment;
    }

    public HopkinsLocationsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_hopkins_locations, container, false);

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
}
