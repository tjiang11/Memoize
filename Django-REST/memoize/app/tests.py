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
class tests(APITestCase):
	
	def test_creating_user(self):
		#data = {"username": "test1@jhu.edu", "password": "PaSsWoRd",  "mem_groups": [], "sub_groups": []}
		#response = self.client.post('/users/', data, format='json')
		#print response.status_code
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		data2 = {"username": "test2@jhu.edu2", "password": "PaSsWoRd2",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data2, format='json')
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		reponse = self.client.get('/users/', format='json')
		#print reponse
		print response.status_code
		#data2 = {'password': 'PaSsWoRd'}
		#response = self.client.post('/users/1', data2, format='json')
		#print response.status_code
		

	def test_time_reminder(self):	
		#client = APIClient()

		data = {"username": "test3@jhu.edu", "password": "PaSsWoRd3",  "mem_groups": [], "sub_groups": []}
		response = self.client.post('/users/', data, format='json')

		print response.status_code

		data3 = {"time": "1996-12-05T06:32:00", "name": "3"}
		response = self.client.post('/users/3/timereminders/', data3, format='json')
		print response.status_code
		#print reponse

		response = self.client.get('/users/3/timereminders/', {}, format = 'json')
		print response.content
		#comp = str(response)
		print "\n"
		#print comp
		self.assertEqual(response.content, '[{"name":"3","time":"1996-12-05T06:32:00Z"}]')
		#print response
		self.assertEqual(response.status_code, 200)
	#def test_basic_user(self):
	#	user = make_test_user(self)
	#	self.failIf(False)     

	#def test_basic_group(self):
	#	from models import User 
	#	#creating a user
	#	user = User.User2("test2", "test2@jhu.edu", "passwordTEST2")
		
		



def make_test_user(self):
	data = {"username": "test" + str(count) + "@jhu.edu", "password": "PaSsWoRd" + str(count),  "mem_groups": [], "sub_groups": []}
	response = self.client.post('/users/', data, format='json')	
	#count = count + 1
	return response


def main():
    unittest.main()

if __name__ == '__main__':
    main() 

	