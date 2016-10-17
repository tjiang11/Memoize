package org.memoize.group19;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents an event, which is created by a user and contained within a group.
 * @author group 19
 *
 */
public class Event {
	/**The name of the event, chosen by the user.*/
	private String name;
	/**The start time of the event.*/
	private Date startime;
	/**The end time of the event.*/
	private Date endTime;
	/**The location of the event.*/
	private String location;
	/**A list of tags that the creator of the group has specified to be attached to this event.*/
	private ArrayList<String> tags;
	
}
