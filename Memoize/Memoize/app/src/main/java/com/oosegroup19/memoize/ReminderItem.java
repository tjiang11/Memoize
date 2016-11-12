package com.oosegroup19.memoize;

/**
 * Created by smsukardi on 11/12/16.
 */
public class ReminderItem {
    private String event;
    private String description;

    //Constructor
    public ReminderItem(String event, String description) {
        this.event = event;
        this.description = description;
    }

    //Accessor Methods
    public String getEvent()        { return event; }
    public String getDescription()  { return description; }
}
