class Reminder(object):

	def _init_(self, name, description):
		self.name = name;
		self.description = description


class L_reminder(Reminder):

	def _init_(self, name, description, start_time, end_time, location):
		super(L_reminder, self)._init_(name, description)
		self.start_time = start_time
		self.end_time = end_time
		self.location = location


class T_reminder(Reminder):

	def _init_(self, name, description, time):
		super(L_reminder, self)._init_(name, description)
		self.time - time

