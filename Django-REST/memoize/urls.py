from django.conf.urls import url, include
from rest_framework import routers
from rest_framework.urlpatterns import format_suffix_patterns
from memoize.app import views

router = routers.DefaultRouter()
urlpatterns = [
    url(r'^', include(router.urls)),
    url(r'^api-auth/', include('rest_framework.urls', namespace='rest_framework')),
    url(r'^hello/', views.TestView.as_view(), name='test-view'),
    url(r'^', include('memoize.app.urls')),
]
