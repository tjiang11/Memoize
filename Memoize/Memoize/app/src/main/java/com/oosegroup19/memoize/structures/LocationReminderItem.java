package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class LocationReminderItem extends ReminderItem {

    private double latitude;
    private double longitude;
    private String startTime;
    private String endTime;


    public LocationReminderItem(String event, String location, String description, String startTime, String endTime, double latitude, double longitude) {
        super(event, location, description);
        this.latitude = latitude;
        this.longitude = longitude;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }
    public String getStartTime()    { return startTime; }
    public String getEndTime()      { return endTime; }

}
