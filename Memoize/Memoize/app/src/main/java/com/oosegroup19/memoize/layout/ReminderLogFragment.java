package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.google.gson.Gson;
import com.oosegroup19.memoize.activity.HomePageActivity;
import com.oosegroup19.memoize.structures.LastResortReminderItem;
import com.oosegroup19.memoize.structures.LocationReminderItem;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.ReminderItem;
import com.oosegroup19.memoize.structures.ReminderItemAdapter;
import com.oosegroup19.memoize.structures.TimeReminderItem;
import com.oosegroup19.memoize.structures.User;

import org.json.JSONArray;

import java.io.FileReader;
import java.util.ArrayList;

import static com.oosegroup19.memoize.activity.HomePageActivity.PREFS_NAME;

/** The ReminderLogFragment class contains the logic for the log of reminders tab.
 *  Created by smsukardi on 11/12/16.
 */
public class ReminderLogFragment extends BaseFragment {
    public final static String FRAGMENTNAME = "ReminderLogFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance Fields
    private ListView remindersList;
    protected static ArrayList<ReminderItem> reminderItems;
    protected static ReminderItemAdapter aa;

    private static Context context;
    private static Cursor curse;

    /** Required empty ReminderLogFragment constructor.
     */
    public ReminderLogFragment() {}

    public static ReminderLogFragment newInstance(User user) {
        ReminderLogFragment fragment = new ReminderLogFragment();
        context = HomePageActivity.getContext();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity myActivity = (AppCompatActivity) getActivity();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate sthe layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder_log, container, false);
        getActivity().setTitle("Reminder Log");

        remindersList = (ListView) view.findViewById(R.id.itemList);

        //Creates an ArrayList of courses from database
        reminderItems = new ArrayList<ReminderItem>();

        //Makes array adapter to bind arraylist to listview with new custom item layout
        aa = new ReminderItemAdapter(this.getActivity(), R.layout.list_contents, reminderItems);
        remindersList.setAdapter(aa);

        //Sets an OnClickListener when an item in the list if clicked
        remindersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position) instanceof LocationReminderItem) {
                    LocationReminderItem locReminderItem = (LocationReminderItem) parent.getItemAtPosition(position);

                    //Segues to a ReminderDetailFragment, sending the LocationReminderItem that was
                    //clicked on to the ReminderDetail
                    ReminderDetailFragment fragment = ReminderDetailFragment.newInstance(locReminderItem);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_main, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else { //must be a timereminderitem
                    TimeReminderItem timeReminderItem = (TimeReminderItem) parent.getItemAtPosition(position);

                    //Segues to a ReminderDetailFragment, sending the TimeReminderItem that was clicked
                    //on to the ReminderDetail
                    ReminderDetailFragment fragment = ReminderDetailFragment.newInstance(timeReminderItem);
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_main, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });


        //Logs for debugging
        Log.i("ReminderLogFrag", "attempting networking request...");
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

        //Now, listview is bound to user's array data
        //Creates request to REST api to retrieve LocationReminders
        AndroidNetworking.get(HomePageActivity.baseURL + "/users/" + settings.getString("user_id", "0") + "/locationreminders/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        LocationReminderItem[] myReminderItems = gson.fromJson(response, LocationReminderItem[].class);

                        //Adds each LocationReminder to the list
                        for (LocationReminderItem reminderItem : myReminderItems) {
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            aa.add(reminderItem);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "Error on loading location reminders.");
                        Log.e("tag", anError.getMessage());
                    }
                });

        //Creates request to REST api to retrieve TimeReminders
        AndroidNetworking.get(HomePageActivity.baseURL + "/users/" + settings.getString("user_id", "0") + "/timereminders/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        TimeReminderItem[] myReminderItems = gson.fromJson(response, TimeReminderItem[].class);

                        //Adds each TimeReminder to the list
                        for (TimeReminderItem reminderItem : myReminderItems) {
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            aa.add(reminderItem);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "Error on loading time reminders");
                        Log.e("tag", anError.getMessage());
                    }
                });

        //Creates request to REST api to retrieve LastResortReminders
        AndroidNetworking.get(HomePageActivity.baseURL + "/users/" + settings.getString("user_id", "0") + "/lastresortreminders/")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        LastResortReminderItem[] myReminderItems = gson.fromJson(response, LastResortReminderItem[].class);

                        //Adds each LastResortReminder to the list
                        for (LastResortReminderItem reminderItem : myReminderItems) {
                            Log.i("ReminderLogFrag", reminderItem.toString());
                            aa.add(reminderItem);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tag", "Error on loading last resort reminders");
                        Log.e("tag", anError.getMessage());
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
