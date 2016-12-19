package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class TimeReminderItem extends ReminderItem {

    private String time;

    public TimeReminderItem(String event, String location, String description, String time) {
        super(event, location, description);
        this.time = time;
    }

    public String getTime()   { return time; }

    @Override
    public String toString() {
        return super.toString() + " Time: " + String.valueOf(time);
    }
}
