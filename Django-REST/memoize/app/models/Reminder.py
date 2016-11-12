from django.db import models
from django.contrib.auth.models import User

# class Reminder(object):

# 	def __init__(self, name, description):
# 		self.name = name;
# 		self.description = description


# class L_reminder(Reminder):

# 	def __init__(self, name, description, start_time, end_time, location):
# 		super(L_reminder, self)._init_(name, description)
# 		self.start_time = start_time
# 		self.end_time = end_time
# 		self.location = location


# class T_reminder(Reminder):

# 	def __init__(self, name, description, time):
# 		super(L_reminder, self)._init_(name, description)
# 		self.time - time

class Reminder(models.Model):
	name = models.CharField(max_length=255)

	class Meta:
		abstract = True

class LocationReminder(Reminder):
	start_time = models.DateTimeField(null=True)
	end_time = models.DateTimeField(null=True)
	user = models.ForeignKey(User,
		related_name='location_reminders',
		on_delete=models.CASCADE,
		default=1)

class TimeReminder(Reminder):
	time = models.DateTimeField(null=True)
	user = models.ForeignKey(User,
		related_name='time_reminders',
		on_delete=models.CASCADE,
		default=1)