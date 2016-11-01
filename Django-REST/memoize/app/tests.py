from django.test import TestCase
import unittest
import models


# Create your tests here.
class tests(unittest.TestCase):

	def test_basic_user(self):
		user = make_test_user(self)
		self.failIf(False)     

	def test_basic_group(self):
		from models import User 
		#creating a user
		user = User.User2("test2", "test2@jhu.edu", "passwordTEST2")
		
		



def make_test_user(self):
	from models import User 
	#creating a user
	user = User.User2("test1", "test1@jhu.edu", "passwordTEST1")
	return user


def main():
    unittest.main()

if __name__ == '__main__':
    main() 

	