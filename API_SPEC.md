### API Specification

Rough api spec for debugging

POST: /users

Create a new user.

Body:  
'username': The email of the user to be created  
'password': The password associated with the account  

GET: /users/:id/timereminders  

Get all time reminders for specific user.  

POST: /users/:id/timereminders  

Create a time reminder for specific user.  

Body:  
'name': Name of the reminder  
'description': Description of the reminder  
'location_descriptor': Description of the location  
'time': A time for the reminder (yyyy-mm-ddThh:mm)  

GET: /users/:id/locationreminders  

Get all location reminders for specific user.  

POST: /users/:id/locationreminders  

Create a location reminder for specific user.  

Body:  
'name': Name of the reminder  
'description': Description of the reminder  
'location_descriptor': Description of the location  
'start_time': A start time of day for the reminder (hh:mm:ss)  
'end_time': An end time of day for the reminder (hh:mm:ss)  
'latitude': Latitude of the location  
'longitude': Longitude of the location  
  
GET: /events  

List all events from all groups.  

POST: /events  

Body:  
'name': Name of the event  
'description': Description of the event  
'location_descriptor': A description of the location of the event.  
'start_time': Start time of the event (yyyy-mm-ddThh:mm)  
'end_time': End time of the event (yyyy-mm-ddThh:mm)  
'longitude': Longitude of the event location  
'latitude': Latitude of the event location  
'tags': Tags for the event  


