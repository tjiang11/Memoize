package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class LocationReminderItem extends ReminderItem {

    private double latitude;
    private double longitude;

    public LocationReminderItem(String event, String location, String description, double latitude, double longitude) {
        super(event, location, description);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }
}
