package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class TimeReminderItem extends ReminderItem {

    private int startTime;
    private int endTime;

    public TimeReminderItem(String event, String description, int startTime, int endTime) {
        super(event, description);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getStartTime()   { return startTime; }
    public int getEndTime()     { return endTime; }
}
