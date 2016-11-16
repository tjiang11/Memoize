# MEMOIZE [![Build Status](https://travis-ci.com/jhu-oose/2016-group-19.svg?token=pxuwgYspjAfqr5xt35A6&branch=master)](https://travis-ci.com/jhu-oose/2016-group-19)



## Premise:

## To Install:

* Navigate to the Django-REST folder
* Create and activate virtual environment.
* pip install -r "requirements.txt"
* Setup postgresql. Create database with name "memoize_db". Set superuser as "postgres" and password as "password".
	* CREATE DATABASE memoize_db;
	* CREATE USER postgres WITH PASSWORD "password";
	* GRANT ALL PRIVELEGES ON DATABASE "memoize_db" to postgres;
* Perform initial migrations.
	* python manage.py makemigrations
	* python manage.py migrate
* Run the server (python manage.py runserver).

* We are currently working on an automated build setup.

## Components:
* Frontend:
    * Java
    * Android Studio
* Backend:
    * Python
    * Django
    * PostgreSQL
       
## Built By:
Tony Jiang (tjiang11@jhu.edu)  
Mariya Kazachkova (mkazach1@jhu.edu)  
Sarah Sukardi (smsukardi@jhu.edu)    
