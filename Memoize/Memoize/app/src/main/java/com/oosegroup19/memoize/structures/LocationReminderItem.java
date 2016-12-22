package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */

/** The LocationReminderItem data structure.
 */
public class LocationReminderItem extends ReminderItem {

    //Instance fields
    private double latitude;
    private double longitude;

    /** The LocationReminderItem constructor.
     *
     * @param event The name of the event
     * @param location The location of the event
     * @param description The event description
     * @param latitude The latitude associated with the event location
     * @param longitude The longitude associated with the event location
     * @param id The ID of the LocationReminderItem
     */
    public LocationReminderItem(String event, String location, String description, double latitude, double longitude, int id) {
        super(event, location, description, id);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**Retrieves the LocationReminderItem latitude.
     * @return The latitude of the location
     */
    public double getLatitude()     { return latitude; }

    /**Retrieves the LocationReminderItem longitude.
     * @return The longitude of the location.
     */
    public double getLongitude()    { return longitude; }

    /** A toString method for the LocationReminderItem with latitude and longitude coordinates as a string.
     * @return The string containing information about the LocationReminderItem.
     */
    @Override
    public String toString() {
        return super.toString() + " Latitude: " + getLatitude() + " Longitude: " + getLongitude();
    }

}
