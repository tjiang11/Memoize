from django.conf.urls import url
from memoize.app import views
from rest_framework.authtoken import views as authtoken_views

urlpatterns = [
	url(r'^users/$', views.UserList.as_view()),
	url(r'^users/(?P<pk>[0-9]+)/$', views.UserDetail.as_view()),
	url(r'^users/(?P<pk>[0-9]+)/timereminders/$', views.UserTimeReminders.as_view()), 
	url(r'^users/(?P<pk>[0-9]+)/timereminders/(?P<pk_reminder>[0-9]+)$', views.UserTimeRemindersDetail.as_view()), 
	url(r'^users/(?P<pk>[0-9]+)/timeremindersdetail/(?P<pk_reminder>[0-9]+)$', views.UserTimeRemindersDetail.as_view()), 
	url(r'^users/(?P<pk>[0-9]+)/locationreminders/$', views.UserLocationReminders.as_view()), 
	url(r'^users/(?P<pk>[0-9]+)/locationremindersdetail/(?P<pk_reminder>[0-9]+)$', views.UserLocationRemindersDetail.as_view()), 
	url(r'^users/(?P<pk>[0-9]+)/lastresortreminders/$', views.UserLastResortReminders.as_view()), 
	url(r'^users/(?P<pk>[0-9]+)/lastresortremindersdetail/(?P<pk_reminder>[0-9]+)$', views.UserLastResortRemindersDetail.as_view()), 
	url(r'^api-token-auth/', authtoken_views.obtain_auth_token),
	url(r'^api-token-auth-id/', views.CustomObtainAuthToken.as_view()),
]