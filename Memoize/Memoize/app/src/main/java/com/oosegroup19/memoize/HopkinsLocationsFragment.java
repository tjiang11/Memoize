package com.oosegroup19.memoize;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
    protected static ArrayList<HopkinsLocationItem> hopkinsLocationItems;
    protected static HopkinsLocationAdapter aa;

    private OnFragmentInteractionListener mListener;

    private Context context;
    private static Cursor curse;
    //protected static ListDatabaseAdapter dbAdapt;

    public HopkinsLocationsFragment() {}

    public static HopkinsLocationsFragment newInstance(User user) {
        HopkinsLocationsFragment fragment = new HopkinsLocationsFragment();
        // owner = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onResume(){
        super.onResume();
        AppCompatActivity myActivity = (AppCompatActivity) getActivity();
        ActionBar myActionBar = myActivity.getSupportActionBar();
        myActionBar.setTitle("Hopkins Locations");
        updateArray();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hopkins_locations, container, false);
        getActivity().setTitle("Locations");

        // dbAdapt = new ListDatabaseAdapter(getActivity());
        // dbAdapt.open();

        hopkinsLocationsList = (ListView) view.findViewById(R.id.hopkinsLocationsList);
        // create ArrayList of courses from database
        hopkinsLocationItems = new ArrayList<HopkinsLocationItem>();

        // make array adapter to bind arraylist to listview with new custom item layout
        aa = new HopkinsLocationAdapter(this.getActivity(), R.layout.hopkins_locations_list_contents, hopkinsLocationItems);
        hopkinsLocationsList.setAdapter(aa);

        updateArray();

        /*
        remindersList.setLongClickable(true);

        remindersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> listView, View view,
                                           int pos, long id) {
                Cursor curse = dbAdapt.getAllItems();
                curse.move(curse.getCount() - (int) id);    //reverse chronological order :D
                //extract hours
                //String hours = data.drivingLogData.get(pos).get("title");
                //hours = hours.substring(prefix.length(), hours.length());
                String hours = curse.getString(1);
                //extract date
                //String date = data.drivingLogData.get(pos).get("subtitle");
                String date = curse.getString(2);
                String lighting = curse.getString(3);
                String lesson = curse.getString(4);
                String weather = curse.getString(5);

                //show popup to edit log item
                EditDialog popUp = new EditDialog(getActivity(), DrivingLogFragment.this, curse.getLong(0));
                popUp.setHours(hours);
                popUp.setDate(date);

                if (lesson.charAt(0) == 'R') { //residential
                    popUp.setLesson(0);
                } else if (lesson.charAt(0) == 'C') { //commercial
                    popUp.setLesson(1);
                } else {    //highway
                    popUp.setLesson(2);
                }

                if (lighting.equalsIgnoreCase("Day")) {
                    popUp.setLighting(0);
                } else { //Night
                    popUp.setLighting(1);
                }

                if (weather.charAt(0) == 'C') { //clear
                    popUp.setWeather(0);
                } else if (weather.charAt(0) == 'R') { //rainy
                    popUp.setWeather(1);
                } else {    //snow/ice
                    popUp.setWeather(2);
                }

                popUp.show();
                return true;
            }
        });
        */


        //now, listview is bound to user's array data

        //populates the list with some dummy data
        HopkinsLocationItem item1 = new HopkinsLocationItem("AMR 1", "amr1", 500, 600);
        HopkinsLocationItem item2 = new HopkinsLocationItem("Barnes and Noble", "barnesandnoble", 600, 700);
        HopkinsLocationItem item3 = new HopkinsLocationItem("Brody Learning Commons", "brody", 100, 400);
        HopkinsLocationItem item4 = new HopkinsLocationItem("Gilman Hall", "gilman", 300, 250);

        aa.add(item1);
        aa.add(item2);
        aa.add(item3);
        aa.add(item4);

        return view;
    }

    public void updateArray() {
        /*
        curse = dbAdapt.getAllItems();
        listItems.clear();
        if (curse.moveToFirst())
            do {
                ReminderItem result = new ReminderItem(curse.getString(1), curse.getString(2),
                        curse.getString(3), curse.getString(4),
                        curse.getString(5));
                ReminderItem.add(0, result);  // puts in reverse order
            } while (curse.moveToNext());

        aa.notifyDataSetChanged();
        */
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

    public String getFragmentName(){ return this.fragmentName; }

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
