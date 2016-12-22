package com.oosegroup19.memoize.structures;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by smsukardi on 11/12/16.
 */

/** The TimeReminderItem data structure.
 */
public class TimeReminderItem extends ReminderItem {

    //The time instance field.
    private String time;

    /** The TimeReminderItem constructor.
     * @param event The name of the event.
     * @param location The location of the event.
     * @param description A description of the event.
     * @param time The time that the event is to occur.
     * @param id The ID of the TimeReminderItem.
     */
    public TimeReminderItem(String event, String location, String description, String time, int id) {
        super(event, location, description, id);
        this.time = time;
    }

    /** A method to convert the time to Greenwich Mean Time.
     */
    public void convertTime() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String date = this.time.substring(0, 10);
        String time = this.time.substring(11);
        String datetime = date + time;
        try {
            Date oldDate = df.parse(datetime);
            Log.i("tag", "original " + this.time.toString());
            Date newDate = new Date(oldDate.getTime() - 5 * 3600 * 1000);
            this.time = newDate.toString();
            Log.i("tag", newDate.toString());
        } catch (ParseException e) {
            Log.e("tag", e.getMessage());
        }
    }

    /**Accessor method to retrieve the time associated with the TimeReminderItem.
     * @return The time as a string.
     */
    public String getTime()   { return time; }

    /**Returns the information associated with the TimeReminderItem as a string.
     * @return The string with information.
     */
    @Override
    public String toString() {
        return super.toString() + " Time: " + String.valueOf(time);
    }
}
