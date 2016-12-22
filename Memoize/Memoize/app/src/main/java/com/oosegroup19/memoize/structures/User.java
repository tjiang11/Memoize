package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */

/** The User data structure.
 */
public class User {
    private String userId;
    private String username;
    private String email;

    /**
     * Default public constructor
     */
    public User(){
    }

    /** The user contructor.
     *
     * @param userId The ID of the user
     * @param username The user's username
     * @param email The email of the user
     */
    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    /**
     *  Retrieves the User ID.
     * @return The ID
     */
    public String getUserId(){return this.userId;}

    /**
     * Retrieves the username of the user.
     * @return The username.
     */
    public String getUsername() { return this.username;}

    /**
     * Sets the username of the user.
     * @param username The username to set.
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Retrieves the email of the user.
     * @return The email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     * @param email The new email.
     */
    public void setEmail(String email) { this.email = email;}

}
