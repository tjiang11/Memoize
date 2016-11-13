package com.oosegroup19.memoize;

/**
 * Created by smsukardi on 11/12/16.
 */
public class HopkinsLocationItem {
    private String locationName;
    private String imageRef;
    private double latitude;
    private double longitude;

    //Constructor
    public HopkinsLocationItem(String locationName, String imageRef, double latitude, double longitude) {
        this.locationName = locationName;
        this.imageRef = imageRef;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Accessor Methods
    public String getLocationName() { return locationName; }
    public String getImageRef()     { return imageRef; }
    public double getLatitude()     { return latitude; }
    public double getLongitude()    { return longitude; }

}
