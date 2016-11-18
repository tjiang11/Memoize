package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class TimeReminderItem extends ReminderItem {

    private int time;

    public TimeReminderItem(String event, String location, String description, int time) {
        super(event, location, description);
        this.time = time;
    }

    public int getTime()   { return time; }

    @Override
    public String toString() {
        return super.toString() + " Time: " + String.valueOf(time);
    }
}
