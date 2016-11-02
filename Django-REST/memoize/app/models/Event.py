# class Event:

# 	def _init_(self, name, start_time, end_time, location, tags):
# 		self.name = name
# 		self.start_time - start_time
# 		self.end_time - end_time
# 		self.location - location
# 		self.tags = tags

from django.db import models

class Event(models.Model):
	name = models.CharField(max_length=255)
	start_time = models.DateTimeField(auto_now_add=True)
	end_time = models.DateTimeField(null=True)
	location = models.IntegerField(null=True)
	tags = models.CharField(max_length=255)

	#Change to group later.
	owner = models.ForeignKey('auth.User', 
		related_name='events', 
		on_delete=models.CASCADE,
		default=1);