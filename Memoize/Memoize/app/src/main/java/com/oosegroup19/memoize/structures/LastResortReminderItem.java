package com.oosegroup19.memoize.structures;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tony on 12/19/2016.
 */

/**
 * The LastResortReminderItem data structure.
 */
public class LastResortReminderItem extends ReminderItem {
    // Instance fields
    private double latitude;
    private double longitude;
    private String time;

    /**Constructor for a LastResortReminder item.
     *
     * @param event The name of the event
     * @param location The location of hte event
     * @param description The description of the event
     * @param latitude The latitude coordinates of the event
     * @param longitude The longitude coordinates of the event
     * @param time The time of the event
     * @param id The ID associated with the event.
     */
    public LastResortReminderItem(String event, String location, String description, double latitude, double longitude, String time, int id) {
        super(event, location, description, id);
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    /** A method to convert the time to Greenwich Mean Time.
     */
    public void convertTime() {
        //Separates the date into date and time components
        DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String date = this.time.substring(0, 10);
        String time = this.time.substring(11);
        String datetime = date + time;

        try {
            //Converts to Greenwich mean time
            Date oldDate = df.parse(datetime);
            Log.i("tag", "original " + this.time.toString());
            Date newDate = new Date(oldDate.getTime() - 5 * 3600 * 1000);
            this.time = newDate.toString();
            Log.i("tag", newDate.toString());
        } catch (ParseException e) {
            Log.e("tag", e.getMessage());
        }
    }
}
