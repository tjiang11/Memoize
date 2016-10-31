from django.conf.urls import url
from memoize.app import views

urlpatterns = [
	url(r'^events/$', views.event_list),
	url(r'^events/(?P<pk>[0-9]+)/$', views.event_detail),
]