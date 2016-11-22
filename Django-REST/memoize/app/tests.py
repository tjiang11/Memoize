from rest_framework.test import APIRequestFactory
from rest_framework import status
from django.test import TestCase
from rest_framework.test import APITestCase
import unittest
import models
from rest_framework.test import APIClient

client = APIClient()
count = 0


# Create your tests here.
class happy_path_tests(APITestCase):
	
	def test_creating_user(self):
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)


		#data2 = {"username": "test2@jhu.edu2", "password": "PaSsWoRd2",  "mem_groups": [], "sub_groups": []}
		#response = self.client.post('/users/', data2, format='json')
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		#client.login(username='test1@jhu.edu', password='PaSsWoRd1')
		#reponse = self.client.get('/users/', format='json')
		#print reponse
		#print response.status_code
		#data2 = {'password': 'PaSsWoRd'}
		#response = self.client.post('/users/1', data2, format='json')
		#print response.status_code
		

	def test_time_reminder(self):	
		#data = {"username": "test3@jhu.edu", "password": "PaSsWoRd3",  "mem_groups": [], "sub_groups": []}
		#response = self.client.post('/users/', data, format='json')
		response = make_test_user(self)
		data3 = {"time": "1996-12-05T06:32:00", "name": "buy food", "description": "descripto patronum", "location_descriptor": "Hogwarts school of oose"}
		response = self.client.post('/users/6/timereminders/', data3, format='json')

		response = self.client.get('/users/6/timereminders/', {}, format = 'json')
		self.assertEqual(response.content, '[{"name":"buy food","description":"descripto patronum","location_descriptor":"Hogwarts school of oose","time":"1996-12-05T06:32:00Z"}]')
		self.assertEqual(response.status_code, 200)

	def test_location_reminder(self):
		response = make_test_user(self)
		data = {"start_time": "10:45[:0[0]]", "name": "buy food", "description": "descripto patronum", "location_descriptor": "Hogwarts school of oose", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/5/locationreminders/', data, format='json')
		self.assertEqual(response.content, '{"name":"buy food","description":"descripto patronum","location_descriptor":"Hogwarts school of oose","start_time":"10:45:00","end_time":"11:45:00","latitude":"1.00000","longitude":"1.00000"}')
		self.assertEqual(response.status_code, 201) 

	def test_group(self): #will need to be modified when we add authentication!
		data = {
		    "name": "test group",
		    "description": "this is a test group",
		    "admins": [],
		    "subscribers": [],
		    "events": []
		}
		response = self.client.post('/groups/', data, format='json')
		self.assertEqual(response.status_code, 201)
		self.assertEqual(response.content, '{"id":3,"name":"test group","description":"this is a test group","admins":[],"subscribers":[],"events":[]}')


	#things to consider: when making a group I am not in the admins list for that group. Should probably be fixed
	def test_adding_subscription_to_group(self):
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
		self.assertEqual(response.status_code, 201)
		self.assertEqual(response.content, '{"id":2,"name":"group that should be added for subscription testing","description":"this is a test group","admins":[],"subscribers":[2],"events":[]}')

		response = self.client.get('/users/2/', {}, format='json')
		self.assertEqual(response.status_code, 200)
		self.assertEqual(response.content, '{"id":2,"username":"test_subscription@jhu.edu","mem_groups":[],"sub_groups":[2]}')

	def test_adding_event_to_group(self):
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
		self.assertEqual(response.status_code, 201)
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


class non_happy_path_tests(APITestCase):
	def test_making_user_without_username_password(self):
		#test creating a user without an email and without a password (name? I don't remember)
		data = {"username": "", "password": "PaSsWoRd2",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data, format='json')
		self.assertEquals(response.content, '{"username":["This field may not be blank."]}')
		self.assertEquals(response.status_code, 400)

		data2 = {"username": "test_without_password", "password": "",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data2, format='json')
		self.assertEquals(response.content, '{"password":["This field may not be blank."]}')
		self.assertEquals(response.status_code, 400)

	def test_reminders_without_descriptions(self):
		response = make_test_user(self)
		data3 = {"time": "1996-12-05T06:32:00", "name": "buy food", "description": "", "location_descriptor": "Hogwarts school of oose"}
		response = self.client.post('/users/7/timereminders/', data3, format='json')
		self.assertEquals(response.content, '{"description":["This field may not be blank."]}')
		#Now we test to make sure that the reminder really wasn't created
		response = self.client.get('/users/7/timereminders/', {}, format = 'json')
		self.assertEquals(response.content, '[]')

		data = {"start_time": "10:45[:0[0]]", "name": "buy food", "description": "", "location_descriptor": "Hogwarts school of oose", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/7/locationreminders/', data, format='json')
		self.assertEquals(response.content, '{"description":["This field may not be blank."]}')
		#Now we test to make sure that the reminder really wasn't created
		response = self.client.get('/users/7/locationreminders/', {}, format = 'json')
		self.assertEquals(response.content, '[]')

		#test making a user with incorrect credentials (do we have this implemented yet?)

		#test making reminder (both) without a description (this might be a happy path thing actually)

		#test making group without name (should not work)
	def test_z_making_group_without_name_and_description(self):
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
		    "name": "now test no description",
		    "description": "",
		    "admins": [],
		    "subscribers": [],
		    "events": []
		}
		response = self.client.post('/groups/', data, format='json')
		self.assertEquals(response.content, '{"description":["This field may not be blank."]}')


	def test_z_reminders_without_time(self):
		response = make_test_user(self)
		data = {"time": "", "name": "test without time", "description": "we are testing", "location_descriptor": "Hogwarts school of oose"}
		response = self.client.post('/users/8/timereminders/', data, format='json')
		self.assertEquals(response.content, '{"time":["Datetime has wrong format. Use one of these formats instead: YYYY-MM-DDThh:mm[:ss[.uuuuuu]][+HH:MM|-HH:MM|Z]."]}')\

		data = {"start_time": "", "name": "test without start time", "description": "we are testing", "location_descriptor": "Hogwarts school of oose", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/8/locationreminders/', data, format='json')
		self.assertEquals(response.content, '{"start_time":["Time has wrong format. Use one of these formats instead: hh:mm[:ss[.uuuuuu]]."]}')

		data = {"start_time": "11:45[:0[0]]", "name": "test without start time", "description": "we are testing", "location_descriptor": "Hogwarts school of oose", "end_time": "", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/8/locationreminders/', data, format='json')
		self.assertEquals(response.content, '{"end_time":["Time has wrong format. Use one of these formats instead: hh:mm[:ss[.uuuuuu]]."]}')


		#test making time reminder without a time (will fail)








	

def make_test_user(self):
	global count
	data = {"username": "test" + str(count) + "@jhu.edu", "password": "PaSsWoRd" + str(count),  "mem_groups": [], "sub_groups": []}
	response = self.client.post('/users/', data, format='json')	
	count = count + 1
	return response


def main():
    unittest.main()

if __name__ == '__main__':
    main() 

	