# MEMOIZE [![Build Status](https://travis-ci.com/jhu-oose/2016-group-19.svg?token=pxuwgYspjAfqr5xt35A6&branch=master)](https://travis-ci.com/jhu-oose/2016-group-19)

## Premise:
Memoize is a Android application built for event notification and discovery. It contains extensive support for location-based notifications, as well as support for time-based notifications in an intuitive and secure setting. Memoize is built on a modern stack of native Android, Django, and PostgreSQL. 

## To Install:

* Navigate to the Django-REST folder
* Create and activate virtual environment.
* `brew install postgresql`
* `pip install -r "requirements.txt"`
* Install postgres.app (for mac), available at http://postgresapp.com/; a blue elephant should show up on the top of your toolbar.
* Setup postgresql. Create database with name "memoize_db". Set superuser as "postgres" and password as "password".
	* `CREATE DATABASE memoize_db;`
	* `GRANT ALL PRIVILEGES ON DATABASE "memoize_db" to postgres;`
* Perform initial migrations.
	* `python manage.py migrate`
* Run the server.
	* `python manage.py runserver`
* We are currently working on an automated build setup.

## To Run:

* Run the server (as shown in the installation instructions using `python manage.py runserver`). If you are using mac, make sure you have executed `postgres.app` before running the server.
* Run the Android application by opening the `Memoize` file in Android Studio and pressing the play button (this performs an automated build, executing the gradle build program and running the application on the generic android adb emulator). If you are using the android emulator with the server on, change the string in `baseURL` from `"http://10.0.3.2:"` to `"http://10.0.2.2:"`. If you are using genymotion, leave the baseURL as is.

* NOTE: if one is using genymotion or another non-native-to-android-studio virtual machine, they MUST reference a different server in the baseURL. This was explained further above.
	
## Components:

* Frontend:
    * Java  
    * Android Studio  
    * Various libraries  
       * Fast-Android-Networking  
       * Google Maps  
* Backend:
    * Python
    * Django 1.10
    * Django REST Framework
    * PostgreSQL
       
## Built By:

Tony Jiang (tjiang11@jhu.edu)  
Mariya Kazachkova (mkazach1@jhu.edu)  
Sarah Sukardi (smsukardi@jhu.edu)    
