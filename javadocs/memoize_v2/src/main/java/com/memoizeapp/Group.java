package org.memoize.group19;

import java.util.ArrayList;

/**
* This class represents a group created by a user.
* @author group 19
*
*/
public class Group {
	/**The name of the group, input by the user.*/
	private String name;
	/**A description of the group, input by the user.*/
	private String description;
	/**A list of users subscribed to this group.*/
	private ArrayList<User> subscribers;
	/**A list of users with admin rights to this group.*/
	private ArrayList<User> admins;
	/**A list of events for this group*/
	private ArrayList<Event> events;
	
	/**
	 * Method for a group to create a new event within itself.
	 * @param info The info for the creation of the event.
	 */
	public void addEvent(String info) {
		
	}
}
