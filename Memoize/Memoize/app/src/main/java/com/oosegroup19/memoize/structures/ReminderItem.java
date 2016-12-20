package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */
public class ReminderItem {
    private String name;
    private String location_descriptor;
    private String description;
    private int id;

    //Constructor
    public ReminderItem(String name, String location_descriptor, String description, int id) {
        this.name = name;
        this.location_descriptor = location_descriptor;
        this.description = description;
        this.id = id;
    }

    //Accessor Methods
    public String getName()        { return name; }
    public String getLocationDescriptor()     { return location_descriptor; }
    public String getDescription()  { return description; }
    public int getId() { return this.id; }

    @Override
    public String toString() {
        return "Name: " + getName() + " Location: " + getLocationDescriptor() + " Description: " + getDescription() + " ID: " + getId();
    }
}
