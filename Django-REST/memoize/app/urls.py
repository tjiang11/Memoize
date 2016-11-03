from django.conf.urls import url
from memoize.app import views

urlpatterns = [
	url(r'^events/$', views.event_list.as_view()),
	url(r'^events/(?P<pk>[0-9]+)/$', views.event_detail.as_view()),
	url(r'^users/$', views.UserList.as_view()),
	url(r'^users/(?P<pk>[0-9]+)/$', views.UserDetail.as_view()),
	url(r'^groups/$', views.GroupList.as_view()),
	url(r'^groups/(?P<pk>[0-9]+)/$', views.GroupDetail.as_view()),
]