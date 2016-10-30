class User(): 

	def _init_(self, name, email, password):
		self.name = name
		self.email = email
		self.password = password
		self.location_reminders = []
		self.time_reminders = []
		self.my_groups = []

	def login(email, password):
		return True

	def create_l_reminder(info):
		return True

	def create_t_reminder(info):
		return True

	def create_group(info):
		return True

	def create_group_event(info):
		return True;
