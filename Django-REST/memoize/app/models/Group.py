

# class Group():

# 	def __init__(self, name, description):
# 		self.name = name
# 		self.description = description
# 		self.subscribers = []
# 		self.admins = []
# 		self.events = []

# 	def add_event(info):
# 		return True

from django.db import models

class MemGroup(models.Model):
	"""Creates a group (triggered by user wanting to create a group).

	Args:
	  	name: the name of the group being created.
	  	description: an optional description of the group.
	  	admins: users that are admins to this group
	  	subscribers: users that are subscribed to this group.   
    """
	name = models.CharField(max_length=255)
	description = models.CharField(max_length=1000)
	admins = models.ManyToManyField('auth.User', related_name='mem_groups')
	subscribers = models.ManyToManyField('auth.User', related_name='sub_groups')