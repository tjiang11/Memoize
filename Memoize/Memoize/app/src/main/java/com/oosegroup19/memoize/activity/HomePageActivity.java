package com.oosegroup19.memoize.activity;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.oosegroup19.memoize.MyLocationListener;
import com.oosegroup19.memoize.R;
import com.oosegroup19.memoize.structures.User;
import com.oosegroup19.memoize.layout.BaseFragment;
import com.oosegroup19.memoize.layout.HomePageFragment;
import com.oosegroup19.memoize.layout.NewNotificationFragment;
import com.oosegroup19.memoize.layout.ReminderLogFragment;

import java.util.List;

public class HomePageActivity extends AppCompatActivity {

    /*######################## Data Variables ########################*/
    private final String CURRENTFRAGMENT = "currentFragment";
    private User user;

    /*###################### Storage Variables ######################*/
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor peditor;
    private String authenticationKey;

    /*###################### Location Variables ######################*/
    private LocationManager locationManager = null;
    private LocationListener locationListener = null;

    public static int PORT = 8000;
    //    public static String baseURL = "http://10.0.3.2:" + PORT; //uncomment if you are using emulator
    public static String baseURL = "http://0abf1c15.ngrok.io"; //uncomment and put your ngrok url here if using ngrok tunneling

    /*######################## View Elements ########################*/
    private BaseFragment baseFragment;
    private static TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Disables landscape mode
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //remove app title from home page
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        Context context = getApplicationContext();

        AndroidNetworking.initialize(context);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        peditor = myPrefs.edit();


        //Get current GPS location
        //http://rdcworld-android.blogspot.in/2012/01/get-current-location-coordinates-city.html
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = false;
        gpsStatus = isGpsOn();

        if (isGpsOn()) {
            locationListener = new MyLocationListener(context);

            //TODO: Ask for permissions before performing this request
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.e("HomePageActivity", "Permissions for location not yet given...");
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
            }
        } else {
            Log.i("HomePageActivity", "GPS is offline");
        }


        //Stuff for initializing bottom bar for tabbing between fragments
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.i("HomePageActivity", "Selected Tab Position: " + position);
                if (position == 0) {
                    baseFragment = ReminderLogFragment.newInstance(user);
                } else if (position == 1) {
                    baseFragment = HomePageFragment.newInstance(user);
                } else if (position == 2) {
                    baseFragment = NewNotificationFragment.newInstance(user);
                } /*else if (position == 3) {
                    baseFragment = GroupFragment.newInstance(user);
                } */ else {
                    baseFragment = ReminderLogFragment.newInstance(user);
                }

                //Save Tab Position
                peditor.putInt("TabPosition", position);
                peditor.commit();

                inflateAndCommitBaseFragment();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("HomePageActivity", "Unselected Tab Position: " + tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("HomePageActivity", "Reselected Tab Position: " + tab.getPosition());
                if (baseFragment != null) {
                    int position = tab.getPosition();
                    Log.i("HomePageActivity", "Reselected Tab Position: " + position);
                    if (position == 0) {
                        baseFragment = ReminderLogFragment.newInstance(user);
                    } else if (position == 1) {
                        baseFragment = HomePageFragment.newInstance(user);
                    } else if (position == 2) {
                        baseFragment = NewNotificationFragment.newInstance(user);
                    } /*else if (position == 3) {
                        baseFragment = GroupFragment.newInstance(user);
                    } */

                    //Save Tab Position
                    peditor.putInt("TabPosition", position);
                    peditor.commit();

                    inflateAndCommitBaseFragment();
                }
            }
        });

        int tabPosition = myPrefs.getInt("TabPosition", 0);
        tabLayout.getTabAt(tabPosition).select();

        initiateFragment();

        //replaces with lesson screen
        //fragmentManager.beginTransaction().replace(R.id.layout, new HomePageFragment()).commit();
    }

    private void initiateFragment() {
        String currentFragment = myPrefs.getString(CURRENTFRAGMENT, "0");
        Log.i("MainActivity", "Attempting to inflate: " + currentFragment);
        if(currentFragment.equals("0")){
            baseFragment = ReminderLogFragment.newInstance(user);
        }else if(currentFragment.equals(ReminderLogFragment.FRAGMENTNAME)){
            baseFragment = ReminderLogFragment.newInstance(user);
        } else if(currentFragment.equals(HomePageFragment.FRAGMENTNAME)){
            baseFragment = HomePageFragment.newInstance(user);
        } else if(currentFragment.equals(NewNotificationFragment.FRAGMENTNAME)){
            baseFragment = NewNotificationFragment.newInstance(user);
        } /*else if(currentFragment.equals(NotificationsFragment.FRAGMENTNAME)){
            baseFragment = NotificationsFragment.newInstance(user);
        } else if(currentFragment.equals(SettingsFragment.FRAGMENTNAME)){
            baseFragment = SettingsFragment.newInstance(user);
        }  */ else{
            Log.e("MainActivity", "InvalidFragmentNameFound: " + currentFragment);
        }

        inflateAndCommitBaseFragment();
    }

    private void inflateAndCommitBaseFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_main, baseFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        peditor.putString(CURRENTFRAGMENT, baseFragment.getFragmentName());
        peditor.commit();
    }

    private boolean isGpsOn() {
        // ContentResolver contentResolver = getBaseContext().getContentResolver();
        // boolean gpsStatus = Settings.Secure.LOCATION_MODE(contentResolver, LocationManager.GPS_PROVIDER);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        return gpsStatus;
    }

    @Override
    public void onPause(){
        super.onPause();

        if(baseFragment == null){
            peditor.putString(CURRENTFRAGMENT, "0");
        }else {
            Log.i("HomePageActivity", "onPause on fragment: "+baseFragment.getFragmentName());
            peditor.putString(CURRENTFRAGMENT, baseFragment.getFragmentName());
        }
        peditor.putInt("TabPosition", tabLayout.getSelectedTabPosition());
        peditor.commit();
    }

    //for maps permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("HomePageActivity", "onrequestpermissionsresult called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();

        if (fragments != null) {
            for (android.support.v4.app.Fragment fragment : fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}