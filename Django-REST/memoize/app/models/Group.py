from django.db import models

class Group():

	def __init__(self, name, description):
		self.name = name
		self.description = description
		self.subscribers = []
		self.admins = []
		self.events = []

	def add_event(info):
		return True