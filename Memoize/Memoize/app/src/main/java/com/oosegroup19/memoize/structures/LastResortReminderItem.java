package com.oosegroup19.memoize.structures;

/**
 * Created by Tony on 12/19/2016.
 */

public class LastResortReminderItem extends ReminderItem {

    private double latitude;
    private double longitude;
    private String time;

    public LastResortReminderItem(String event, String location, String description, double latitude, double longitude, String time) {
        super(event, location, description);
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }
}
