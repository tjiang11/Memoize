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
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		#data2 = {"username": "test2@jhu.edu2", "password": "PaSsWoRd2",  "mem_groups": [], "sub_groups": []}
		#response = self.client.post('/users/', data2, format='json')
		response = make_test_user(self)
		self.assertEqual(response.status_code, status.HTTP_201_CREATED)

		reponse = self.client.get('/users/', format='json')
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
		response = self.client.post('/users/4/timereminders/', data3, format='json')

		response = self.client.get('/users/4/timereminders/', {}, format = 'json')
		self.assertEqual(response.content, '[{"name":"buy food","description":"descripto patronum","location_descriptor":"Hogwarts school of oose","time":"1996-12-05T06:32:00Z"}]')
		self.assertEqual(response.status_code, 200)

	def test_location_reminder(self):
		response = make_test_user(self)
		data = {"start_time": "10:45[:0[0]]", "name": "buy food", "description": "descripto patronum", "location_descriptor": "Hogwarts school of oose", "end_time": "11:45[:0[0]]", "latitude": "1.00", "longitude": "1.00"}
		response = self.client.post('/users/3/locationreminders/', data, format='json')
		self.assertEqual(response.content, '{"name":"buy food","description":"descripto patronum","location_descriptor":"Hogwarts school of oose","start_time":"10:45:00","end_time":"11:45:00","latitude":"1.00000","longitude":"1.00000"}')
		self.assertEqual(response.status_code, 201) 

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

	