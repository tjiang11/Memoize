package com.oosegroup19.memoize;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by smsukardi on 11/12/16.
 */
public class ReminderLogFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "ReminderLogFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance Fields
    private ListView remindersList;
    protected static ArrayList<ReminderItem> reminderItems;
    protected static ReminderItemAdapter aa;

    //private OnFragmentInteractionListener mListener;

    private Context context;
    private static Cursor curse;
    //protected static ListDatabaseAdapter dbAdapt;

    //Constructor
    public ReminderLogFragment() {}

    public static ReminderLogFragment newInstance(User user) {
        ReminderLogFragment fragment = new ReminderLogFragment();
        // owner = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        AppCompatActivity myActivity = (AppCompatActivity) getActivity();
        ActionBar myActionBar = myActivity.getSupportActionBar();
        myActionBar.setTitle("Upcoming Reminders");
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
        View view = inflater.inflate(R.layout.fragment_reminder_log, container, false);
        getActivity().setTitle("Locations");

        // dbAdapt = new ListDatabaseAdapter(getActivity());
        // dbAdapt.open();

        remindersList = (ListView) view.findViewById(R.id.itemList);
        // create ArrayList of courses from database
        reminderItems = new ArrayList<ReminderItem>();

        // make array adapter to bind arraylist to listview with new custom item layout
        aa = new ReminderItemAdapter(this.getActivity(), R.layout.list_contents, reminderItems);
        remindersList.setAdapter(aa);

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
        ReminderItem item1 = new LocationReminderItem("This is a Test Event", "a test event description!", 500, 600);
        ReminderItem item2 = new LocationReminderItem("Cry", "an event in which copious amounts of crying occur. lol.", 600, 700);
        ReminderItem item3 = new LocationReminderItem("Cry A Lot", "cry, redux. this is an event description for the ages.", 100, 400);
        ReminderItem item4 = new LocationReminderItem("SADNESS", "need I say more?", 30, 20);

        aa.add(item1);
        aa.add(item2);
        aa.add(item3);
        aa.add(item4);

        return view;
    }

    /*
    public void updateMainActivity() {
        ((MainActivity) getActivity()).refresh();
    } */

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
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*
        mListener = null;
        */
    }

    public String getFragmentName() {
        return this.fragmentName;
    }
}
