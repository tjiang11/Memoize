package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */

/**
 * The HopkinsLocationItem data structure.
 */
public class HopkinsLocationItem {

    //Instance fields
    private String locationName;
    private String imageRef;
    private double latitude;
    private double longitude;

    /**
     * The HopkinsLocationItem constructor.
     * @param locationName The name of the location
     * @param imageRef The image reference
     * @param latitude The latitude of the location
     * @param longitude The longitude of the location
     */
    public HopkinsLocationItem(String locationName, String imageRef, double latitude, double longitude) {
        this.locationName = locationName;
        this.imageRef = imageRef;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Accessor Methods

    /** Retrieves the location name
     * @return the name of the location.
     */
    public String getLocationName() { return locationName; }

    /** Retrieves the image reference name.
     * @return The image reference.
     */
    public String getImageRef()     { return imageRef; }

    /**Retrieves the latitude.
     * @return The hopkins location latitude
     */
    public double getLatitude()     { return latitude; }

    /** Retrieves the longitude.
     * @return The hopkins location longitude
     */
    public double getLongitude()    { return longitude; }

}
