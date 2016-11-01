from django.contrib.auth.models import User

from Subscription import Subscription

class User2(): 

	def __init__(self, name, email, password):
		self.name = name
		self.email = email
		self.password = password
		self.location_reminders = []
		self.time_reminders = []
		self.my_groups = []
		self.subscriptions = Subscription() 
		self.admin = False

		User.objects.create_user(username = name, email = email, password = password)

	def login(self, email, password):
		return True

	def create_l_reminder(self, info):
		return True

	def create_t_reminder(self, info):
		return True

	def create_group(self, info):
		self.admin = True
		return True

	def create_group_event(self, group, info):
		return True;

	def subscribe_to_group(self, group):
		subscriptions.subscribe_to_group(group)

	def subscribe_to_tag(self, tag):
		subscriptions.subscribe_to_tag(tag)

	def return_info(self):
		info = [self.name, self.email, self.password]
		return info
