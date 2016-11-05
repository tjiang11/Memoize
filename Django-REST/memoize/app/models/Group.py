

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
	name = models.CharField(max_length=255)
	description = models.CharField(max_length=1000)
	admins = models.ManyToManyField('auth.User', related_name='mem_groups')