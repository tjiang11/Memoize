package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class ReminderItem {
    private String name;
    private String location_descriptor;
    private String description;

    //Constructor
    public ReminderItem(String name, String location_descriptor, String description) {
        this.name = name;
        this.location_descriptor = location_descriptor;
        this.description = description;
    }

    //Accessor Methods
    public String getName()        { return name; }
    public String getLocationDescriptor()     { return location_descriptor; }
    public String getDescription()  { return description; }

    @Override
    public String toString() {
        return "Name: " + getName() + " Location: " + getLocationDescriptor() + " Description: " + getDescription();
    }
}
