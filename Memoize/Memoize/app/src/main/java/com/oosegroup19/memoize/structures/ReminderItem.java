package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */

/**
 * The ReminderItem data structure.
 */
public class ReminderItem {
    private String name;
    private String location_descriptor;
    private String description;
    private int id;

    /** The Constructor for the generic ReminderItem, which all reminder types extend.
     *
     * @param name The name of the reminder
     * @param location_descriptor The location description
     * @param description The description
     * @param id The ID of the reminder
     */
    public ReminderItem(String name, String location_descriptor, String description, int id) {
        this.name = name;
        this.location_descriptor = location_descriptor;
        this.description = description;
        this.id = id;
    }

    //Accessor Methods

    /**
     * Retrieves the reminder name.
     * @return The reminder name.
     */
    public String getName()        { return name; }

    /**
     * Retrieves the location description of the reminder.
     * @return The location descriptor
     */
    public String getLocationDescriptor()     { return location_descriptor; }

    /**
     * Retrieves the description of the reminder.
     * @return The reminder description.
     */
    public String getDescription()  { return description; }

    /**
     * Retrieves the ID associated with the reminder.
     * @return The ID
     */
    public int getId() { return this.id; }

    /**
     * A toString method for the ReminderItem which gives relevant information
     * about the ReminderItem as a string.
     * @return The string with information about the ReminderItem.
     */
    @Override
    public String toString() {
        return "Name: " + getName() + " Location: " + getLocationDescriptor() + " Description: " + getDescription() + " ID: " + getId();
    }
}
