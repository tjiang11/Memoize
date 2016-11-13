package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class ReminderItem {
    private String event;
    private String location;
    private String description;

    //Constructor
    public ReminderItem(String event, String location, String description) {
        this.event = event;
        this.location = location;
        this.description = description;
    }

    //Accessor Methods
    public String getEvent()        { return event; }
    public String getLocation()     { return location; }
    public String getDescription()  { return description; }
}
