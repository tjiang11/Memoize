package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class LocationReminderItem extends ReminderItem {

    private double latitude;
    private double longitude;
    private String start_time;
    private String end_time;


    public LocationReminderItem(String event, String location, String description, String start_time, String end_time, double latitude, double longitude, int id) {
        super(event, location, description, id);
        this.latitude = latitude;
        this.longitude = longitude;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }
    public String getStartTime()    { return start_time; }
    public String getEndTime()      { return end_time; }

    @Override
    public String toString() {
        return super.toString() + " Latitude: " + getLatitude() + " Longitude: " + getLongitude()
                + " Start Time: " + getStartTime() + " End Time:" + getEndTime();
    }

}
