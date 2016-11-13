package com.oosegroup19.memoize.structures;

/**
 * Created by smsukardi on 11/12/16.
 */

public class User {
    private String userId;
    private String username;
    private String email;

    /***
     * Default public constructor
     * Firebase requries an empty public constructor for
     * object representation
     */
    public User(){
    }

    public User(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public String getUserId(){return this.userId;}
    public String getUsername() { return this.username;}
    public void setUsername(String username){
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) { this.email = email;}

}
