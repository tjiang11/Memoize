# class Event:

# 	def _init_(self, name, start_time, end_time, location, tags):
# 		self.name = name
# 		self.start_time - start_time
# 		self.end_time - end_time
# 		self.location - location
# 		self.tags = tags

from django.db import models
from Group import MemGroup

class Event(models.Model):
	"""Used when a user creates an event for a group.

   	Args:
   		start_time: when the event should start being tracked for.
   		end_time: when the event should no longer be tracked for.
   		location: where the event is taking place.
   		tags: the tags associated with this event. 
   		group: the group that this event belongs to.
   	"""
	name = models.CharField(max_length=255)
	description = models.CharField(max_length=1000)
	location_descriptor = models.CharField(max_length=255)
	start_time = models.DateTimeField(null=False)
	end_time = models.DateTimeField(null=False)

	#Change to PostGIS PointField
	latitude = models.DecimalField(max_digits=8, decimal_places=5)
	longitude = models.DecimalField(max_digits=8, decimal_places=5)
	
	tags = models.CharField(max_length=255)

	#Change to group later.
	group = models.ForeignKey(MemGroup, 
		related_name='events', 
		on_delete=models.CASCADE,
		default=1);