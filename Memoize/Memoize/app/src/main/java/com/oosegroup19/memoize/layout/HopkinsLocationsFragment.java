package com.oosegroup19.memoize.layout;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.util.Log;

import com.oosegroup19.memoize.structures.HopkinsLocationAdapter;
import com.oosegroup19.memoize.structures.HopkinsLocationItem;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.User;

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
    //The name of the fragment
    public final static String FRAGMENTNAME = "HopkinsLocationsFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance fields
    private static String returnToFrag = "";

    private static String timeBasedReminderName = "";
    private static String timeBasedLocationDescription = "";
    private static String timeBasedEventDescription = "";

    private static String locationBasedEventName = "";
    private static String locationBasedEventLocationName = "";
    private static String locationBasedEventDescription = "";
    private static int locationBasedRadius = 100;

    //ListView instance fields
    private ListView hopkinsLocationsList;
    protected static ArrayList<HopkinsLocationItem> hopkinsLocationItems;
    protected static HopkinsLocationAdapter aa;

    private Context context;
    private static Cursor curse;
    //protected static ListDatabaseAdapter dbAdapt;

    /**
     * Required public constructor.
     */
    public HopkinsLocationsFragment() {}

    /**
     * Constructor returning a new instance of a HopkinsLocationFragment which persists
     * relevant information about locations.
     *
     * @param returnTo
     * @param name
     * @param locDescription
     * @param eventDescription
     * @return
     */
    public static HopkinsLocationsFragment newInstance(String returnTo, String name, String locDescription,
                                                       String eventDescription) {
        HopkinsLocationsFragment fragment = new HopkinsLocationsFragment();
        returnToFrag = returnTo;

        timeBasedReminderName = name;
        timeBasedLocationDescription = locDescription;
        timeBasedEventDescription = eventDescription;

        return fragment;
    }

    /**
     * A constructor returning a new instance of a HopkinsLocations fragment which persists
     * relevant information about time reminders.
     *
     * @param returnTo Which fragment type to eventually segue back to.
     * @param eventName The name of the reminder
     * @param eventLocationName The location description of the reminder
     * @param eventLocationDescriptionName The event description of the reminder
     * @param radius The radius of the reminder
     * @return
     */
    public static HopkinsLocationsFragment newInstance(String returnTo, String eventName, String eventLocationName, String eventLocationDescriptionName, int radius) {
        HopkinsLocationsFragment fragment = new HopkinsLocationsFragment();
        returnToFrag = returnTo;

        locationBasedEventName = eventName;
        locationBasedEventLocationName = eventLocationName;
        locationBasedEventDescription = eventLocationDescriptionName;
        locationBasedRadius = radius;

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

        hopkinsLocationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("HopkinsLocationFrag", String.valueOf(id));
                HopkinsLocationItem value = (HopkinsLocationItem) parent.getItemAtPosition(position);
                Log.i("HopkinsLocationFrag", value.getLocationName());
                Fragment fragment;
                if (returnToFrag.equals("location")) {
                    fragment = LocationBasedNotificationFragment.newInstance(value.getLocationName(),
                            value.getLatitude(), value.getLongitude(), locationBasedEventName, locationBasedEventDescription, locationBasedRadius);
                } else if (returnToFrag.equals("time")) {
                    fragment = TimeBasedNotificationFragment.newInstance(value.getLatitude(),
                            value.getLongitude(), timeBasedReminderName, timeBasedLocationDescription, timeBasedEventDescription);
                } else {
                    fragment = LocationBasedNotificationFragment.newInstance(value.getLocationName(), value.getLatitude(), value.getLongitude());
                    Log.e("tag", "Error retrieving return fragment.");
                }
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        //now, listview is bound to user's array data

        //TODO: Actually put in the correct GPS coordinates
        HopkinsLocationItem item1 = new HopkinsLocationItem("AMR I", "amr1", 39.330690, -76.618441);
        HopkinsLocationItem item2 = new HopkinsLocationItem("AMR II", "amr2", 39.331494, -76.619162);
        HopkinsLocationItem item3 = new HopkinsLocationItem("Barnes and Noble", "barnesandnoble", 39.32831472, -76.61625963);
        HopkinsLocationItem item4 = new HopkinsLocationItem("Brody Learning Commons", "brody", 39.32842042, -76.61933525);
        HopkinsLocationItem item5 = new HopkinsLocationItem("Charles Commons", "commons", 39.32857821, -76.61646080);
        HopkinsLocationItem item6 = new HopkinsLocationItem("Gilman Hall", "gilman", 39.32897241, -76.62163210);
        HopkinsLocationItem item7 = new HopkinsLocationItem("Hackerman Hall", "hackerman", 39.32681050, -76.62097227);
        HopkinsLocationItem item8 = new HopkinsLocationItem("Interfaith Center", "interfaithcenter", 39.33180231, -76.617327150);
        HopkinsLocationItem item9 = new HopkinsLocationItem("Malone Hall", "malone", 39.32620673, -76.62084889);
        HopkinsLocationItem item10 = new HopkinsLocationItem("Mattin", "mattin", 39.32789406, -76.61815595);
        HopkinsLocationItem item11 = new HopkinsLocationItem("Milton S. Eisenhower Library", "mse", 39.32905592, -76.61942732);
        HopkinsLocationItem item12 = new HopkinsLocationItem("Shaffer Hall", "shaffer", 39.32718033, -76.61989939);

        aa.add(item1);
        aa.add(item2);
        aa.add(item3);
        aa.add(item4);
        aa.add(item5);
        aa.add(item6);
        aa.add(item7);
        aa.add(item8);
        aa.add(item9);
        aa.add(item10);
        aa.add(item11);
        aa.add(item12);

        return view;
    }

    public void updateArray() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public String getFragmentName(){ return this.fragmentName; }
}
