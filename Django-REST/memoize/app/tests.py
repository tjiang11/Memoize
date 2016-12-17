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
class happy_path_tests(APITestCase):
	"""TEST SUITE (for happy path tests)."""
	
	def test_creating_user(self):
		"""Tests the basic creation of a user (successful creation)."""
		response = make_test_user(self)
		#Test response
		self.assertEquals(response.content, '{"id":3,"username":"test0@jhu.edu","mem_groups":[],"sub_groups":[],"location_reminders":[],"time_reminders":[]}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		#Test that user is actually in database
		response = self.client.get("/users/3/")
		self.assertEquals(response.content, '{"id":3,"username":"test0@jhu.edu","mem_groups":[],"sub_groups":[]}')
		#Test response
		response = make_test_user(self)
		self.assertEquals(response.content, '{"id":4,"username":"test1@jhu.edu","mem_groups":[],"sub_groups":[],"location_reminders":[],"time_reminders":[]}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		#Test that user is actually in database
		response = self.client.get("/users/4/")
		self.assertEquals(response.content, '{"id":4,"username":"test1@jhu.edu","mem_groups":[],"sub_groups":[]}')

		

	def test_time_reminder(self):	
		"""Tests successful creation of a time reminder."""
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data3 = {"time": "1996-12-05T06:32:00", "name": "make a time reminder", "description": "this is a test description", "location_descriptor": "TEST"}
		response = self.client.post('/users/6/timereminders/', data3, format='json')
		self.assertEquals(response.content, '{"name":"make a time reminder","description":"this is a test description","location_descriptor":"TEST","time":"1996-12-05T06:32:00Z","id":1}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		
		#test if actually stored
		response = self.client.get('/users/6/timereminders/', {}, format = 'json')
		self.assertEqual(response.content, '[{"name":"make a time reminder","description":"this is a test description","location_descriptor":"TEST","time":"1996-12-05T06:32:00Z","id":1}]')
		self.assertEqual(response.status_code, 200)

		#test creating reminder without a description
		data = {"time": "1996-12-05T06:32:00", "name": "this is a reminder with no description", "description": "", "location_descriptor": "TEST"}
		response = self.client.post('/users/6/timereminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

	def test_location_reminder(self):
		"""Tests successful creation of a location reminder."""
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		data = {"start_time": "10:45[:0[0]]", "name": "make a location reminder", "description": "this is a test description", "location_descriptor": "test location", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/5/locationreminders/', data, format='json')
		self.assertEqual(response.content, '{"name":"make a location reminder","description":"this is a test description","location_descriptor":"test location","start_time":"10:45:00","end_time":"11:45:00","latitude":"1.00000000","longitude":"1.00000000","id":1}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED) 

		#test if actually stored
		response = self.client.get('/users/5/locationreminders/', {}, format = 'json')
		self.assertEqual(response.content, '[{"name":"make a location reminder","description":"this is a test description","location_descriptor":"test location","start_time":"10:45:00","end_time":"11:45:00","latitude":"1.00000000","longitude":"1.00000000","id":1}]')
		self.assertEqual(response.status_code, 200)

		#test creating reminder without a description
		data = {"start_time": "10:45[:0[0]]", "name": "make a location reminder without description", "description": "", "location_descriptor": "test location", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/5/locationreminders/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

	def test_group(self): #will need to be modified when we add authentication!
		"""Tests successful creation of a group."""
		data = {
		    "name": "test group",
		    "description": "this is a test group",
		    "admins": [],
		    "subscribers": [],
		    "events": []
		}
		response = self.client.post('/groups/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(response.content, '{"id":3,"name":"test group","description":"this is a test group","admins":[],"subscribers":[],"events":[]}')


	#things to consider: when making a group I am not in the admins list for that group. Should probably be fixed
	def test_adding_subscription_to_group(self):
		"""Tests successful creation of a subsrcription to a group."""
		data = {"username": "test_subscription@jhu.edu", "password": "PaSsWoRdTest",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data, format='json')	
		data = {
		    "name": "group that should be added for subscription testing",
		    "description": "this is a test group",
		    "admins": [],
		    "subscribers": [2],
		    "events": []
		}
		response = self.client.post('/groups/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(response.content, '{"id":2,"name":"group that should be added for subscription testing","description":"this is a test group","admins":[],"subscribers":[2],"events":[]}')

		response = self.client.get('/users/2/', {}, format='json')
		self.assertEqual(response.status_code, 200)
		self.assertEqual(response.content, '{"id":2,"username":"test_subscription@jhu.edu","mem_groups":[],"sub_groups":[2]}')

		response = self.client.get('/users/2/subgroups/')
		self.assertEquals(response.content, '[{"id":2,"name":"group that should be added for subscription testing","description":"this is a test group","admins":[],"subscribers":[2],"events":[]}]')	

		response.client.get('/users/2/subgroups/1/')
		self.assertEquals(response.content, '[{"id":2,"name":"group that should be added for subscription testing","description":"this is a test group","admins":[],"subscribers":[2],"events":[]}]')

	def test_adding_event_to_group(self):
		"""Tests successful creation of an event."""
		"""Tests successful creation of a time reminder."""
		data = {"username": "test_subscription@jhu.edu", "password": "PaSsWoRdTest",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data, format='json')	
		data = {
		    "name": "group that should be added for subscription testing",
		    "description": "this is a test group",
		    "admins": [],
		    "subscribers": [1],
		    "events": []
		}
		response = self.client.post('/groups/', data, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)
		self.assertEqual(response.content, '{"id":1,"name":"group that should be added for subscription testing","description":"this is a test group","admins":[],"subscribers":[1],"events":[]}')

		response = self.client.get('/users/1/', {}, format='json')
		self.assertEqual(response.status_code, 200)
		self.assertEqual(response.content, '{"id":1,"username":"test_subscription@jhu.edu","mem_groups":[],"sub_groups":[1]}')

		data = {
		    "name": 
		        "This is a test event for group 1",
		    "tags": 
		        "test tag",
		    "start_time": 
		    	"1996-12-05T06:32:00",
		    "longitude": 
		        "1.000",
		    "location_descriptor": 
		        "This is a test location",
		    "end_time": 
		        "1996-12-05T06:32:00",
		    "latitude":
		        "1.000",
		    "description": 
		        "This is a test description"
		}

		response = self.client.post('/groups/1/events/', data, format='json')
		self.assertEqual(response.content, '{"name":"This is a test event for group 1","description":"This is a test description","location_descriptor":"This is a test location","start_time":"1996-12-05T06:32:00Z","end_time":"1996-12-05T06:32:00Z","longitude":"1.00000","latitude":"1.00000","tags":"test tag","group":1}')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		response = self.client.get('/events/', {}, format='json')
		self.assertEquals(response.content, '[{"name":"This is a test event for group 1","description":"This is a test description","location_descriptor":"This is a test location","start_time":"1996-12-05T06:32:00Z","end_time":"1996-12-05T06:32:00Z","longitude":"1.00000","latitude":"1.00000","tags":"test tag","group":1}]')

		response = self.client.get('/events/1/', {}, format='json')
		self.assertEquals(response.content, '{"name":"This is a test event for group 1","description":"This is a test description","location_descriptor":"This is a test location","start_time":"1996-12-05T06:32:00Z","end_time":"1996-12-05T06:32:00Z","longitude":"1.00000","latitude":"1.00000","tags":"test tag","group":1}')


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


	def test_z_making_group_without_name(self):
		"""Test that making a group without a name or description or both fails."""
		data = {
		    "name": "",
		    "description": "this is a test group",
		    "admins": [],
		    "subscribers": [],
		    "events": []
		}
		response = self.client.post('/groups/', data, format='json')
		self.assertEquals(response.content, '{"name":["This field may not be blank."]}')

		data = {
		    "name": "",
		    "description": "",
		    "admins": [],
		    "subscribers": [],
		    "events": []
		}
		response = self.client.post('/groups/', data, format='json')
		self.assertEquals(response.content, '{"name":["This field may not be blank."]}')

	def test_z_reminders_without_time(self):
		"""Test that making a time reminder without a time fails."""
		response = make_test_user(self)
		data = {"time": "", "name": "test without time", "description": "we are testing", "location_descriptor": "Hogwarts school of oose"}
		response = self.client.post('/users/7/timereminders/', data, format='json')
		self.assertEquals(response.content, '{"time":["Datetime has wrong format. Use one of these formats instead: YYYY-MM-DDThh:mm[:ss[.uuuuuu]][+HH:MM|-HH:MM|Z]."]}')\

		data = {"start_time": "", "name": "test without start time", "description": "we are testing", "location_descriptor": "Hogwarts school of oose", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/7/locationreminders/', data, format='json')
		self.assertEquals(response.content, '{"start_time":["Time has wrong format. Use one of these formats instead: hh:mm[:ss[.uuuuuu]]."]}')

		data = {"start_time": "11:45[:0[0]]", "name": "test without start time", "description": "we are testing", "location_descriptor": "Hogwarts school of oose", "end_time": "", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/7/locationreminders/', data, format='json')
		self.assertEquals(response.content, '{"end_time":["Time has wrong format. Use one of these formats instead: hh:mm[:ss[.uuuuuu]]."]}')


class nontrivial_feature_happy_path_tests(APITestCase):

	def test_last_resort_reminder_creation(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		time = datetime.datetime.now()
		new_time = time + datetime.timedelta(0,120) #2 minutes in the future

		time_str = str(new_time)
		passed_time = time_str[:-10]
		data3 = {"time": passed_time, "name": "make a last resort reminder", "description": "", "location_descriptor": "TEST", "latitude": "50.000", "longitude": "50.000"}

		response = self.client.post('/users/7/lastresortreminders/', data3, format='json')
		t = passed_time[:10] + "T" + passed_time[11:]
		temp = '{"name":"make a last resort reminder","description":"","location_descriptor":"TEST","time":"' + t + ':00Z","latitude":"50.00000000","longitude":"50.00000000","id":1}'
		self.assertEqual(response.content, temp)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)




def make_test_user(self):
	"""Method to make a user in the database that is used by various other methods."""
	global count
	data = {"username": "test" + str(count) + "@jhu.edu", "password": "PaSsWoRd" + str(count),  "mem_groups": [], "sub_groups": []}
	response = self.client.post('/users/', data, format='json')	
	count = count + 1
	return response


	
