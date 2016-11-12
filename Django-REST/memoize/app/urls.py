from django.conf.urls import url
from memoize.app import views

urlpatterns = [
	url(r'^events/$', views.event_list.as_view()),
	url(r'^events/(?P<pk>[0-9]+)/$', views.event_detail.as_view()),
	url(r'^users/$', views.UserList.as_view()),
	url(r'^users/(?P<pk>[0-9]+)/$', views.UserDetail.as_view()),
	url(r'^users/(?P<pk>[0-9]+)/subgroups/$', views.UserGroups.as_view()),
	url(r'^users/(?P<pk_user>[0-9]+)/subgroups/(?P<pk_group>[0-9]+)$', views.UserGroupsDetail.as_view()),
	url(r'^users/(?P<pk>[0-9]+)/timereminders/$', views.UserTimeReminders.as_view()), 
	url(r'^users/(?P<pk>[0-9]+)/locationreminders/$', views.UserLocationReminders.as_view()), 
	url(r'^groups/$', views.GroupList.as_view()),
	url(r'^groups/(?P<pk>[0-9]+)/$', views.GroupDetail.as_view()),
]