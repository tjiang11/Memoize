"""
Django settings for tutorial project.

Generated by 'django-admin startproject' using Django 1.10.2.

For more information on this file, see
https://docs.djangoproject.com/en/1.10/topics/settings/

For the full list of settings and their values, see
https://docs.djangoproject.com/en/1.10/ref/settings/
"""

import os # pragma: no cover

# Build paths inside the project like this: os.path.join(BASE_DIR, ...)
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__))) # pragma: no cover


# Quick-start development settings - unsuitable for production
# See https://docs.djangoproject.com/en/1.10/howto/deployment/checklist/

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = 'xrgs&v=a63bi^_ak6)k=zg+p0@)molt-w6x(76#x!8n@i9j60(' # pragma: no cover

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True # pragma: no cover

ALLOWED_HOSTS = [] # pragma: no cover


# Application definition

INSTALLED_APPS = [ # pragma: no cover
    'django.contrib.admin',
    'django.contrib.auth',
    'django.contrib.contenttypes',
    'django.contrib.sessions',
    'django.contrib.messages',
    'django.contrib.staticfiles',
    'memoize.app',
    'rest_framework',
    'rest_framework.authtoken',
#    'django_nose',
]

MIDDLEWARE = [ # pragma: no cover
    'django.middleware.security.SecurityMiddleware',
    'django.contrib.sessions.middleware.SessionMiddleware',
    'django.middleware.common.CommonMiddleware',
    'django.middleware.csrf.CsrfViewMiddleware',
    'django.contrib.auth.middleware.AuthenticationMiddleware',
    'django.contrib.messages.middleware.MessageMiddleware',
    'django.middleware.clickjacking.XFrameOptionsMiddleware',
]

ROOT_URLCONF = 'memoize.urls' # pragma: no cover

TEMPLATES = [ # pragma: no cover
    {
        'BACKEND': 'django.template.backends.django.DjangoTemplates',
        'DIRS': [],
        'APP_DIRS': True,
        'OPTIONS': {
            'context_processors': [
                'django.template.context_processors.debug',
                'django.template.context_processors.request',
                'django.contrib.auth.context_processors.auth',
                'django.contrib.messages.context_processors.messages',
            ],
        },
    },
]

WSGI_APPLICATION = 'memoize.wsgi.application' # pragma: no cover


# Database
# https://docs.djangoproject.com/en/1.10/ref/settings/#databases

# DATABASES = {
#     'default': {
#         'ENGINE': 'django.db.backends.sqlite3',
#         'NAME': os.path.join(BASE_DIR, 'db.sqlite3'),
#     }
# }

#TEST_RUNNER = 'django_nose.NoseTestSuiteRunner' # pragma: no cover

#NOSE_ARGS = [ # pragma: no cover
 #   '--with-coverage',
  #  '--cover-package=memoize/app',
   #  '--verbosity=2',
#]


""" Reference for local postgresql setup:
https://www.digitalocean.com/community/tutorials/how-to-use-postgresql-with-your-django-application-on-ubuntu-14-04

Install postgresql and get into the shell using psql.
Make sure the default universal user 'postgres' has a password of 'password'

Make a local postgresql database called 'memoize_db' using:

CREATE DATABASE memoize_db;

"""

DATABASES = { # pragma: no cover
    'default': {
        'ENGINE': 'django.db.backends.postgresql_psycopg2',
        'NAME': 'memoize_db',
        'USER': 'postgres',
        'PASSWORD': 'password',
        'HOST': 'localhost',
        'PORT': '5432',
    }
}


# Password validation
# https://docs.djangoproject.com/en/1.10/ref/settings/#auth-password-validators

AUTH_PASSWORD_VALIDATORS = [ # pragma: no cover
    {
        'NAME': 'django.contrib.auth.password_validation.UserAttributeSimilarityValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.MinimumLengthValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.CommonPasswordValidator',
    },
    {
        'NAME': 'django.contrib.auth.password_validation.NumericPasswordValidator',
    },
]


# Internationalization
# https://docs.djangoproject.com/en/1.10/topics/i18n/

LANGUAGE_CODE = 'en-us' # pragma: no cover

TIME_ZONE = 'UTC' # pragma: no cover

USE_I18N = True # pragma: no cover

USE_L10N = True # pragma: no cover

USE_TZ = True # pragma: no cover


# Static files (CSS, JavaScript, Images)
# https://docs.djangoproject.com/en/1.10/howto/static-files/
 
STATIC_URL = '/static/' # pragma: no cover
 
REST_FRAMEWORK = { # pragma: no cover
    #'DEFAULT_PERMISSION_CLASSES': ('rest_framework.permissions.IsAdminUser',),
    'PAGE_SIZE': 10,
    # 'DEFAULT_AUTHENTICATION_CLASSES': (
    #     'rest_framework.authentication.TokenAuthentication',
    # )
}




