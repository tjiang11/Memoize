from django.db import models # pragma: no cover
from django.contrib.auth.models import User # pragma: no cover

class Reminder(models.Model): # pragma: no cover
	"""Allows user to create a reminder.

   	Args:
   		name: the name of the reminder.
   	"""
	name = models.CharField(max_length=255)
	description = models.CharField(null=True, blank=True, max_length=1000)
	location_descriptor = models.CharField(max_length=255)

	class Meta:
		abstract = True

class LocationReminder(Reminder): # pragma: no cover
	"""Represents a location based reminder.

   	Args:
   		latitude: used for location of reminder.
   		longitude: used for location of reminder.
   		user: the user that created the reminder.
   		radius: the radius that the user iputs with the desired radius for being reminded.
   	"""
	latitude = models.DecimalField(max_digits=11, decimal_places=8)
	longitude = models.DecimalField(max_digits=11, decimal_places=8)
	user = models.ForeignKey(User,
		related_name='location_reminders',
		on_delete=models.CASCADE,
		default=1)
	radius = models.IntegerField(default = 100)

class TimeReminder(Reminder): # pragma: no cover
	"""Represents a time based reminder.

   	Args:
   		time: when the reminder should go off.
   		user: the user that created the reminder.
   	"""
	time = models.DateTimeField(null=False)
	user = models.ForeignKey(User,
		related_name='time_reminders',
		on_delete=models.CASCADE,
		default=1)

class LastResortReminder(Reminder): # pragma: no cover
	"""Represents a time based reminder.

   	Args:
   		time: when the event is.
   		user: the user that created the reminder.
   		latitude: used for location of reminder.
   		longitude: used for location of reminder.
   	"""
   	time = models.DateTimeField(null=False)
   	latitude = models.DecimalField(max_digits=11, decimal_places=8)
	longitude = models.DecimalField(max_digits=11, decimal_places=8)
	user = models.ForeignKey(User,
        related_name='last_resort_reminders',
        on_delete=models.CASCADE,
        default=1)