from django.contrib.auth.models import User

from Subscription import Subscription

class User2(): 

  def __init__(self, name, email, password):
    """Create a new User Object.

    Args:
        name: the name of the user.
        email: email specified by the user.
        password: password specified by the user.
    """
    self.name = name
    self.email = email
    self.password = password
    self.location_reminders = []
    self.time_reminders = []
    self.my_groups = []
    self.subscriptions = Subscription() 
    self.admin = False

  #User.objects.create_user(username = name, email = email, password = password)

  def login(self, email, password):
    """Allows used to login to his/her account.

     Args:
     	  email: the email being used for the login.
     	  password: the password being used for the login.   
     Returns:
      	True or False, depending on if the action is successful.
     """
    return True

  def create_l_reminder(self, info):
    """Allows user to create a location based reminder.

     Args:
     	  info: the info needed for the reminder.  
     Returns:
      	True or False, depending on if the action is successful.
     """
    return True

  def create_t_reminder(self, info):
    """Allows user to create a time based reminder.

     Args:
      	info: the info needed for the reminder.  
     Returns:
      	True or False, depending on if the action is successful.
     """
    return True

  def create_group(self, info):
    """Allows user to create a group.

     Args:
      	info: the info needed for the group.  
     Returns:
     	  True or False, depending on if the action is successful.
     """
    self.admin = True
    return True

  def create_group_event(self, group, info):
    """Allows user to create an event for a group.

     Args:
      	info: the info needed for the event.
     	  group: the group associated with the event.   
     Returns:
     	  True or False, depending on if the action is successful.
     """
    return True;

  def subscribe_to_group(self, group):
    """Allows user to subscribe to a group.

     Args:
     	  group: the group the user is trying to subscribe to.  
     Returns:
      	True or False, depending on if the action is successful.
     """
    subscriptions.subscribe_to_group(group)

  def subscribe_to_tag(self, tag):
    """Allows user to subscribe to a tag.

     Args:
      	tag: the tag the user is trying to subscribe to.  
     Returns:
      	True or False, depending on if the action is successful.
     """
    subscriptions.subscribe_to_tag(tag)

  def return_info(self):
    """Returns the info of a user. 
     Returns:
      	the info of the user. 
     """
    info = [self.name, self.email, self.password]
    return info
