import datetime
from datetime import timedelta
from rest_framework.test import APIRequestFactory
from rest_framework import status
from django.test import TestCase
from rest_framework.test import APITestCase
import unittest
#import models
from rest_framework.test import APIClient


client = APIClient()
count = 0


# Create your tests here.
class basic_creation_tests(APITestCase):
	"""TEST SUITE (for happy path tests)."""
	
	def test_creating_user(self):
		"""Tests the basic creation of a user (successful creation)."""
		response = make_test_user(self)
		#Test response
		self.assertEquals(response.content, '{"id":1,"username":"test0@jhu.edu","location_reminders":[],"time_reminders":[]}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		#Test that user is actually in database
		response = self.client.get("/users/1/")
		self.assertEquals(response.content, '{"id":1,"username":"test0@jhu.edu"}')
		#Test response
		response = make_test_user(self)
		self.assertEquals(response.content, '{"id":2,"username":"test1@jhu.edu","location_reminders":[],"time_reminders":[]}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		#Test that user is actually in database
		response = self.client.get("/users/2/")
		self.assertEquals(response.content, '{"id":2,"username":"test1@jhu.edu"}')

		

	def test_time_reminder(self):	
		"""Tests successful creation of a time reminder."""
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data3 = {"time": "1996-12-05T06:32:00", "name": "make a time reminder", "description": "this is a test description", "location_descriptor": "TEST"}
		response = self.client.post('/users/4/timereminders/', data3, format='json')
		self.assertEquals(response.content, '{"name":"make a time reminder","description":"this is a test description","location_descriptor":"TEST","time":"1996-12-05T06:32:00Z","id":1}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		
		#test if actually stored
		#response = self.client.get('/users/6/timereminders/', {}, format = 'json')
		#self.assertEqual(response.content, '[{"name":"make a time reminder","description":"this is a test description","location_descriptor":"TEST","time":"1996-12-05T06:32:00Z","id":1}]')
		#self.assertEqual(response.status_code, 200)

		#test creating reminder without a description
		data = {"time": "1996-12-05T06:32:00", "name": "this is a reminder with no description", "description": "", "location_descriptor": "TEST"}
		response = self.client.post('/users/4/timereminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

	def test_location_reminder(self):
		"""Tests successful creation of a location reminder."""
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data = {"start_time": "10:45[:0[0]]", "name": "make a location reminder", "description": "this is a test description", "location_descriptor": "test location", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/3/locationreminders/', data, format='json')
		self.assertEqual(response.content, '{"name":"make a location reminder","description":"this is a test description","location_descriptor":"test location","latitude":"1.00000000","longitude":"1.00000000","radius":100,"id":1}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED) 

		#test if actually stored
		#response = self.client.get('/users/5/locationreminders/', {}, format = 'json')
		#self.assertEqual(response.content, '[{"name":"make a location reminder","description":"this is a test description","location_descriptor":"test location","start_time":"10:45:00","end_time":"11:45:00","latitude":"1.00000000","longitude":"1.00000000","id":1}]')
		#self.assertEqual(response.status_code, 200)

		#test creating reminder without a description
		data = {"start_time": "10:45[:0[0]]", "name": "make a location reminder without description", "description": "", "location_descriptor": "test location", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/3/locationreminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		#test creating reminder with a radius
		data = {"start_time": "10:45[:0[0]]", "name": "make a location reminder without description", "description": "", "location_descriptor": "test location", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00", "radius": "50"}
		response = self.client.post('/users/3/locationreminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(response.content, '{"name":"make a location reminder without description","description":"","location_descriptor":"test location","latitude":"1.00000000","longitude":"1.00000000","radius":50,"id":3}')		


class unhappy_path_tests(APITestCase):
	"""TEST SUITE (for unhappy path tests)"""
	def test_making_user_without_username_password(self):
		"""Tests that creating au user wothout a username or a password fails."""

		#test creating a user without a username (email)
		data = {"username": "", "password": "PaSsWoRd2",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data, format='json')
		self.assertEquals(response.content, '{"username":["This field may not be blank."]}')
		self.assertEquals(response.status_code, 400)

		#test creating a user without a password
		data2 = {"username": "test_without_password", "password": "",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data2, format='json')
		self.assertEquals(response.content, '{"password":["This field may not be blank."]}')
		self.assertEquals(response.status_code, 400)

		#test creating a user without a username (email) or password
		data3 = {"username": "", "password": "",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data, format='json')
		self.assertEquals(response.content, '{"username":["This field may not be blank."]}') #so we may want to change this 
		self.assertEquals(response.status_code, 400)

	def test_z_reminders_without_time(self):
		"""Test that making a time reminder without a time fails."""
		response = make_test_user(self)
		data = {"time": "", "name": "test without time", "description": "we are testing", "location_descriptor": "Hogwarts school of oose"}
		response = self.client.post('/users/7/timereminders/', data, format='json')
		self.assertEquals(response.content, '{"time":["Datetime has wrong format. Use one of these formats instead: YYYY-MM-DDThh:mm[:ss[.uuuuuu]][+HH:MM|-HH:MM|Z]."]}')\


class nontrivial_feature_tests(APITestCase):

	def test_last_resort_reminder_creation(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0,120) #2 minutes in the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		data3 = {"time": passed_time, "name": "make a last resort reminder", "description": "", "location_descriptor": "TEST", "latitude": "50.000", "longitude": "50.000"}

		response = self.client.post('/users/5/lastresortreminders/', data3, format='json')
		t = passed_time[:10] + "T" + passed_time[11:]
		temp = '{"name":"make a last resort reminder","description":"","location_descriptor":"TEST","time":"' + t + ':00Z","latitude":"50.00000000","longitude":"50.00000000","id":1}'
		self.assertEqual(response.content, temp)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)


		response = self.client.get('/users/5/lastresortreminders/?latitude=50&longitude=50', {}, format='json')
		#should find the notification because at the correct location and 2 minutes before event
		self.assertEqual(response.content, '[{"name":"make a last resort reminder","description":"","location_descriptor":"TEST","time":"' + t + ':00Z","latitude":"50.00000000","longitude":"50.00000000","id":1}]')

	def test_not_displaying_reminders_if_nearby(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0, 3600) #hour in the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		data3 = {"time": passed_time, "name": "make a last resort reminder", "description": "", "location_descriptor": "TEST", "latitude": "50.000", "longitude": "50.000"}

		response = self.client.post('/users/6/lastresortreminders/', data3, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)


		response = self.client.get('/users/6/lastresortreminders/?latitude=50.000001&longitude=50.000001', {}, format='json')
		self.assertEqual(response.content,'[]') #should be empty because hour left and still very close to our location

		response = self.client.get('/users/6/lastresortreminders/?latitude=50.0001&longitude=50.0001', {}, format='json')
		self.assertEqual(response.content,'[]') #should be empty because hour left and still very close to our location


	def test_not_displaying_z_reminder_ahead_of_time(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0, 7200) #2 hours in the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		t = passed_time[:10] + "T" + passed_time[11:]
		data3 = {"time": passed_time, "name": "make a last resort reminder", "description": "", "location_descriptor": "TEST", "latitude": "50.000", "longitude": "50.000"}

		response = self.client.post('/users/7/lastresortreminders/', data3, format='json')
		self.assertEquals(response.status_code, status.HTTP_201_CREATED)

		response = self.client.get('/users/7/lastresortreminders/?latitude=50.5&longitude=50.5', {}, format='json')
		self.assertEquals(response.content,'[]') 
		#should be empty because this reminder is 2 hours into the future and only 40 miles away (which would mean notifying the user 92 minutes before the reminder time)

		response = self.client.get('/users/7/lastresortreminders/?latitude=50.7&longitude=50.7', {}, format='json')
		self.assertEquals(response.content, '[{"name":"make a last resort reminder","description":"","location_descriptor":"TEST","time":"' + t + ':00Z","latitude":"50.00000000","longitude":"50.00000000","id":3}]')
		self.assertEquals(response.status_code, 200)

		#now the travel time to the location of the event is estimated to be 114 minutes, and with the 10 minute grace period
		#it becomes time to notify the user of his/her event, thus the notification is displayed"""


	def test_reminders_at_location_under_800_meters(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0, 900) #15 minutes into the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		t = passed_time[:10] + "T" + passed_time[11:]
		data = {"time": passed_time, "name": "make a last resort reminder within 800 meters", "description": "", "location_descriptor": "TEST", "latitude": "50.000", "longitude": "50.000"}

		response = self.client.post('/users/8/lastresortreminders/', data, format='json')
		self.assertEquals(response.status_code, status.HTTP_201_CREATED)

		response = self.client.get('/users/8/lastresortreminders/?latitude=50.0035&longitude=50.0035', {}, format='json')
		#this distance calls for an estimated travel time of 5.7 minutes, and we add on our 10 minute security period, 
		#so our response should contain the notification because the reminder is for 15 minutes from now
		self.assertEquals(response.content, '[{"name":"make a last resort reminder within 800 meters","description":"","location_descriptor":"TEST","time":"' + t + ':00Z","latitude":"50.00000000","longitude":"50.00000000","id":4}]')


		"""Now we want to make sure that if we are closer the notification is not shown"""
		response = self.client.get('/users/8/lastresortreminders/?latitude=50.0004&longitude=50.00004', {}, format='json')
		#now the travel time is about half a minute, so with the 10 minute security period the user only needs to be notified 
		#11.5 minutes before the reminder is scheduled. Thus, the user should not see the reminder as it is 15 minutes away
		self.assertEquals(response.content,'[]') 
		self.assertEquals(response.status_code, 200)

	def test_reminders_at_location_under__32186_meters(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0, 2400) #40 minutes into the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		t = passed_time[:10] + "T" + passed_time[11:]
		data = {"time": passed_time, "name": "make a last resort reminder within 32186 meters", "description": "", "location_descriptor": "TEST", "latitude": "50.000", "longitude": "50.000"}

		response = self.client.post('/users/9/lastresortreminders/', data, format='json')
		self.assertEquals(response.status_code, status.HTTP_201_CREATED)

		response = self.client.get('/users/9/lastresortreminders/?latitude=50.095&longitude=50.095', {}, format='json')
		#this means we are about 7.795 miles away from our event. This calls for a travel time of about 31.18 minutes. 
		#with the 10 minute security period this means the the estimated total travel time is slightly more than 40 minutes
		#this means our get request should return our notification to display to the user
		self.assertEquals(response.content, '[{"name":"make a last resort reminder within 32186 meters","description":"","location_descriptor":"TEST","time":"' + t + ':00Z","latitude":"50.00000000","longitude":"50.00000000","id":5}]')
		self.assertEquals(response.status_code, 200)

		#now we check to make sure that if you're closer the notification does not display
		response = self.client.get('/users/9/lastresortreminders/?latitude=50.05&longitude=50.05', {}, format='json')
		#only 4.1 miles away, which means about 16 minutes -- 26 minutes estimated time with the 10 minute security period
		#notification should not display 
		self.assertEquals(response.content, '[]')
		self.assertEquals(response.status_code, 200)


	def test_reminders_at_very_far_locations(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0, 3540) #59 minutes into the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		t = passed_time[:10] + "T" + passed_time[11:]
		data = {"time": passed_time, "name": "make a last resort reminder over 32186 meters", "description": "", "location_descriptor": "TEST", "latitude": "50.000", "longitude": "50.000"}

		response = self.client.post('/users/10/lastresortreminders/', data, format='json')
		self.assertEquals(response.status_code, status.HTTP_201_CREATED)

		response = self.client.get('/users/10/lastresortreminders/?latitude=50.3&longitude=50.3', {}, format='json')
		#this means that we are currently 24.6 miles away from where our event should be. This is an estimated travel time
		#of about 49.2 minutes, and adding the addtional 10 minute grace period causes an estimated travel time of 59.2 minutes
		self.assertEquals(response.content, '[{"name":"make a last resort reminder over 32186 meters","description":"","location_descriptor":"TEST","time":"' + t + ':00Z","latitude":"50.00000000","longitude":"50.00000000","id":6}]')
		self.assertEquals(response.status_code, 200)

		#if we are closer then the notificiation will not go off
		response = self.client.get('/users/10/lastresortreminders/?latitude=50.1&longitude=50.1', {}, format='json')
		self.assertEquals(response.content, '[]')
		self.assertEquals(response.status_code, 200)


class test_delete_calls(APITestCase):

	def test_last_resort_delete(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data = {"time": "1996-12-05T06:32:00", "name": "make a time reminder", "description": "this is a test description", "location_descriptor": "TEST","latitude": "50.000", "longitude": "50.000"}
		response = self.client.post('/users/11/lastresortreminders/', data, format='json')
 		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		
		response = self.client.delete('/users/11/lastresortremindersdetail/7', {}, format='json')
		self.assertEquals(response.status_code, 204)

		#now check that it's actually not there anymore
		response  = self.client.get('/users/11/lastresortreminders/', {}, format='json')
		self.assertEquals(response.content, '[]')
		self.assertEquals(response.status_code, 200)


	def test_location_reminder_delete(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data = {"name": "make a location reminder", "description": "this is a test description", "location_descriptor": "test location", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/12/locationreminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		response = self.client.delete('/users/12/locationremindersdetail/4', {}, format='json')
		self.assertEquals(response.status_code, 204)

		#now we make sure it was actually deleted
		response  = self.client.get('/users/12/locationreminders/', {}, format='json')
		self.assertEquals(response.content, '[]')
		self.assertEquals(response.status_code, 200)


	def test_time_reminder_delete(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data = {"time": "1996-12-05T06:32:00", "name": "make a time reminder", "description": "this is a test description", "location_descriptor": "TEST"}
		response = self.client.post('/users/13/timereminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)


		response = self.client.delete('/users/13/timeremindersdetail/3', {}, format='json')
		self.assertEquals(response.status_code, 204)

		#now we make sure it was actually deleted
		response  = self.client.get('/users/13/timereminders/', {}, format='json')
		self.assertEquals(response.content, '[]')
		self.assertEquals(response.status_code, 200)



class test_returning_specific_reminders(APITestCase):

	def test_get_current(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0, 15) #14 seconds into the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		t = passed_time[:10] + "T" + passed_time[11:]

		data = {"time": passed_time, "name": "make a time reminder", "description": "this is a test description", "location_descriptor": "TEST"}
		response = self.client.post('/users/14/timereminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		response = self.client.get('/users/14/timereminders/?get_current', data, format='json')
		self.assertEquals(response.content, '[{"name":"make a time reminder","description":"this is a test description","location_descriptor":"TEST","time":"' + t + ':00Z","id":4}]')
		self.assertEquals(response.status_code, 200)

	def test_get_reminder_at_location(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data = {"name": "make a location reminder", "description": "this is a test description", "location_descriptor": "test location", "end_time": "11:45[:0[0]]", "latitude": "50.00", "longitude": "50.00", "radius": "150"}
		response = self.client.post('/users/15/locationreminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		response = self.client.get('/users/15/locationreminders/?latitude=50.00005&longitude=50.00005', {}, format='json')
		#since we are within the radius we will get the notification back
		self.assertEquals(response.content, '[{"name":"make a location reminder","description":"this is a test description","location_descriptor":"test location","latitude":"50.00000000","longitude":"50.00000000","radius":150,"id":5}]')
		self.assertEquals(response.status_code, 200)

		#Now we check to make sure that if we are far away nothing is returned
		response = self.client.get('/users/15/locationreminders/?latitude=50.2&longitude=50.2', {}, format='json')
		self.assertEquals(response.content, '[]')
		self.assertEquals(response.status_code, 200)


def make_test_user(self): 
	"""Method to make a user in the database that is used by various other methods."""
	global count
	data = {"username": "test" + str(count) + "@jhu.edu", "password": "PaSsWoRd" + str(count)}
	response = self.client.post('/users/', data, format='json')	
	count = count + 1
	return response

