package com.oosegroup19.memoize.structures;

/**
 * Created by Tony on 12/11/2016.
 */

/** A GroupItem data structure.
 */
public class GroupItem {
    // The name of the group.
    private String name;
    //The description of the group.
    private String description;

    /**A GroupItem constructor.
     *
     * @param name The name of the group
     * @param description The description of the group
     */
    public GroupItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Retrieves the name of the group.
     * @return The group name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the description associate with a group.
     * @return The group description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * A toString method to return group information as a string.
     * @return The string with the name and description of the group.
     */
    public String toString() {
        return "Name: " + this.name + " Description: " + this.description;
    }
}
