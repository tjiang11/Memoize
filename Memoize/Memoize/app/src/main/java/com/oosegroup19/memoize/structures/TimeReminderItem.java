package com.oosegroup19.memoize.structures;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by smsukardi on 11/12/16.
 */
public class TimeReminderItem extends ReminderItem {

    private String time;

    public TimeReminderItem(String event, String location, String description, String time, int id) {
        super(event, location, description, id);
        this.time = time;
    }

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

    public String getTime()   { return time; }

    @Override
    public String toString() {
        return super.toString() + " Time: " + String.valueOf(time);
    }
}
