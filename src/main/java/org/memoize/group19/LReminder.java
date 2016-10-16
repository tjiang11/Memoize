package org.memoize.group19;

import java.util.Date;

/**
 * This class represents a location based reminder.
 * @author group 19
 *
 */
public class LReminder extends Reminder{
	/**Represents whether the reminder should go off at the next time you're by this location*/
	private boolean nextTime;
	/**Option instead of nextTime; a start time for when the reminder can go off.*/
	private Date startTime;
	/**An end time for when the reminder can go off.*/
	private Date endTime;
	
}
