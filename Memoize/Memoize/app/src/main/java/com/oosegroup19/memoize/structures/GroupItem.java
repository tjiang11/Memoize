package com.oosegroup19.memoize.structures;

/**
 * Created by Tony on 12/11/2016.
 */

public class GroupItem {
    private String name;
    private String description;

    public GroupItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return "Name: " + this.name + " Description: " + this.description;
    }
}
