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
	start_time = models.DateTimeField(auto_now_add=True)
	end_time = models.DateTimeField(null=True)
	location = models.IntegerField(null=True)
	tags = models.CharField(max_length=255)

	#Change to group later.
	group = models.ForeignKey(MemGroup, 
		related_name='events', 
		on_delete=models.CASCADE,
		default=1);