package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class TimeReminderItem extends ReminderItem {

    private int startTime;

    public TimeReminderItem(String event, String location, String description, int startTime) {
        super(event, location, description);
        this.startTime = startTime;
    }

    public int getStartTime()   { return startTime; }
}
