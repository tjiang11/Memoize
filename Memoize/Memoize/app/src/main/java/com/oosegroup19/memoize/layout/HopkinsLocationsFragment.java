package com.oosegroup19.memoize.layout;

import android.app.FragmentTransaction;
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
    public final static String FRAGMENTNAME = "HopkinsLocationsFragment";
    private final String fragmentName = FRAGMENTNAME;

    //Instance Fields
    private ListView hopkinsLocationsList;
    protected static ArrayList<HopkinsLocationItem> hopkinsLocationItems;
    protected static HopkinsLocationAdapter aa;

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

        hopkinsLocationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("HopkinsLocationFrag", String.valueOf(id));
//                Log.i("HopkinsLocationFrag", )
                HopkinsLocationItem value = (HopkinsLocationItem) parent.getItemAtPosition(position);
                Log.i("HopkinsLocationFrag", value.getLocationName());

                LocationBasedNotificationFragment fragment = LocationBasedNotificationFragment.newInstance(value.getLocationName(), value.getLatitude(), value.getLongitude());
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_main, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //TODO: Pass the LocationItem back to the parent so that it can be saved via POST request in LocationBasedNotificationFragment
            }
        });


        //now, listview is bound to user's array data

        //TODO: Actually put in the correct GPS coordinates
        HopkinsLocationItem item1 = new HopkinsLocationItem("AMR I", "amr1", 500, 600);
        HopkinsLocationItem item2 = new HopkinsLocationItem("AMR II", "amr2", 100, 0);
        HopkinsLocationItem item3 = new HopkinsLocationItem("Barnes and Noble", "barnesandnoble", 600, 700);
        HopkinsLocationItem item4 = new HopkinsLocationItem("Brody Learning Commons", "brody", 100, 400);
        HopkinsLocationItem item5 = new HopkinsLocationItem("Charles Commons", "commons", 400, 400);
        HopkinsLocationItem item6 = new HopkinsLocationItem("Gilman Hall", "gilman", 300, 250);
        HopkinsLocationItem item7 = new HopkinsLocationItem("Hackerman Hall", "hackerman", 300, 250);
        HopkinsLocationItem item8 = new HopkinsLocationItem("Interfaith Center", "interfaithcenter", 400, 400);
        HopkinsLocationItem item9 = new HopkinsLocationItem("Malone Hall", "malone", 300, 250);
        HopkinsLocationItem item10 = new HopkinsLocationItem("Hackerman Hall", "mattin", 300, 250);
        HopkinsLocationItem item11 = new HopkinsLocationItem("Milton S. Eisenhower Library", "mse", 300, 250);
        HopkinsLocationItem item12 = new HopkinsLocationItem("Shaffer Hall", "shaffer", 300, 250);

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
