from django.http import HttpResponse, Http404 # pragma: no cover
from django.views.decorators.csrf import csrf_exempt # pragma: no cover
from django.contrib.auth.models import User # pragma: no cover
from rest_framework import viewsets, views, status, generics, permissions # pragma: no cover
from rest_framework.response import Response # pragma: no cover
from rest_framework.authentication import TokenAuthentication # pragma: no cover
from memoize.app.models import TimeReminder, LocationReminder, LastResortReminder # pragma: no cover
from memoize.app.serializers import UserSerializer, UserUpdateSerializer, IDSerializer, TimeReminderSerializer, LocationReminderSerializer, LastResortReminderSerializer # pragma: no cover
from memoize.app.permissions import IsOwnerOrReadOnlyUser # pragma: no cover
from math import radians, cos, sin, asin, sqrt # pragma: no cover
from rest_framework.authtoken.views import ObtainAuthToken # pragma: no cover
from rest_framework.authtoken.models import Token # pragma: no cover
import datetime # pragma: no cover
from datetime import timedelta # pragma: no cover


class TestView(views.APIView): # pragma: no cover
    def get(self, request, format=None):
        return Response("Hello REST World")


class UserList(generics.ListCreateAPIView):
    """
    Used for getting a list of users.
    """   
    queryset = User.objects.all()
    serializer_class = UserSerializer

class UserDetail(generics.RetrieveUpdateAPIView):
    """
    Used for getting the detail of a single user.
    """
    authentication_classes = (TokenAuthentication, )
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnlyUser)
    queryset = User.objects.all()
    serializer_class = UserUpdateSerializer

    
class UserTimeReminders(views.APIView):
    """
    Calls dealing with getting and posting time reminders.
    """
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        """
        This call will return all of the time reminders if get_current is not specified in the url.
        If get_current is specified in the url then only the reminders that need to go off at that specific time will be returned.
        """
        user = self.get_user(pk=pk)
        serializer = TimeReminderSerializer(user.time_reminders, many=True)
        
        if 'get_current' not in request.GET: 
            return Response(serializer.data)

        time_to_display = []
        for element in serializer.data:
            reminder_time = element['time']
            curr_time = datetime.datetime.now()

            datetime_of_reminder = datetime.datetime.strptime(reminder_time, "%Y-%m-%dT%H:%M:00Z")
            test = datetime_of_reminder - curr_time
            seconds = test.total_seconds()
            if (seconds < 35):
                time_to_display.append(element)

        return Response(time_to_display)

    def post(self, request, pk, format=None):
        serializer = TimeReminderSerializer(data=request.data)
        if (serializer.is_valid()):
            user = self.get_user(pk=pk)
            tr = serializer.save()
            user.time_reminders.add(tr)
            return Response(serializer.data, status.HTTP_201_CREATED)
        return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)

class UserTimeRemindersDetail(views.APIView):
    """
    Calls that deal with getting, putting, and deleting a single time reminder.
    """
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get_reminder(self, pk):
        try:
            return TimeReminder.objects.get(pk=pk)
        except TimeReminder.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

    def put(self, request, pk, pk_reminder, format=None):
        reminder = self.get_reminder(pk=pk_reminder)
        serializer = TimeReminderSerializer(reminder, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, pk, pk_reminder, format=None):
        reminder = self.get_reminder(pk=pk_reminder)
        reminder.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class UserLocationReminders(views.APIView):
    """
    Class that deals with getting all of the location reminders.
    """
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        """
        If latitude and longitude coordinates are not specified in the url, return all location baed reminders.
        If they are specified, only return reminders within the specified radius.
        """
        user = self.get_user(pk=pk)
        
        if 'longitude' in request.GET and 'latitude' in request.GET:
            current_lon = float(request.GET['longitude'])
            current_lat = float(request.GET['latitude'])

        else:
            serializer = LocationReminderSerializer(user.location_reminders, many=True)
            return Response(serializer.data)


        serializer = LocationReminderSerializer(user.location_reminders, many=True)
        length = len(serializer.data)
        nearby = []
        for i in range(length):
            lat = float(serializer.data[i]['latitude'])
            lon = float(serializer.data[i]['longitude'])

            print lat
            print lon
            distance_in_meters = calcDistance(current_lat, current_lon, lat, lon)
            print distance_in_meters
            radius = serializer.data[i]['radius']
            print "this is radius: " + str(radius)
            if distance_in_meters < radius:
                nearby.append(serializer.data[i])

        return Response(nearby)

    def post(self, request, pk, format=None):
        serializer = LocationReminderSerializer(data=request.data)
        if (serializer.is_valid()):
            user = self.get_user(pk=pk)
            lr = serializer.save()
            user.location_reminders.add(lr)
            return Response(serializer.data, status.HTTP_201_CREATED)
        return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)

class UserLocationRemindersDetail(views.APIView):
    """
    Calls that deal with getting, putting, and deleting a single location reminder.
    """
    def get_reminder(self, pk):
        try:
            return LocationReminder.objects.get(pk=pk)
        except LocationReminder.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

    def delete(self, request, pk, pk_reminder, format=None):
        reminder = self.get_reminder(pk=pk_reminder)
        reminder.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

class UserLastResortReminders(views.APIView):
    serializer_class = LastResortReminderSerializer
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        """
        If latitude and longitude are not specified in the url, return all last resort reminders.
        If they are specified, calculate the estimated travel distance and return reminders only if the estimated time is larger than the difference in time.
        """
        user = self.get_user(pk=pk)

        if 'longitude' in request.GET and 'latitude' in request.GET:

            current_lon = float(request.GET['longitude'])
            current_lat = float(request.GET['latitude'])

        else:
            serializer = LastResortReminderSerializer(user.last_resort_reminders, many=True)
            return Response(serializer.data)

        serializer = LastResortReminderSerializer(user.last_resort_reminders, many=True)
        length = len(serializer.data)
        nearby = []
        for i in range(length):
            lat = float(serializer.data[i]['latitude'])
            lon = float(serializer.data[i]['longitude'])
            time = serializer.data[i]['time']

            distance_in_meters = calcDistance(current_lat, current_lon, lat, lon)
            event_time = datetime.datetime.strptime(time, "%Y-%m-%dT%H:%M:00Z")
            current_time = datetime.datetime.now()
            tdelta =  event_time - current_time
            zero_tdelta = timedelta(days=0, seconds=0, microseconds=0)
        
            if distance_in_meters < 800:
                expected_time_to_event = 10
                expected_time_to_event += distance_in_meters * (1.0 / 80.0)
            elif distance_in_meters < 32186.9:
                expected_time_to_event = 10
                expected_time_to_event += (distance_in_meters / 1609.34) * 4
            else:
                expected_time_to_event = 10
                expected_time_to_event += (distance_in_meters / 1609.34) * 2

            if tdelta.total_seconds() < expected_time_to_event * 60:
                nearby.append(serializer.data[i])


        return Response(nearby)

    def post(self, request, pk, format=None):
        serializer = LastResortReminderSerializer(data=request.data)
        if (serializer.is_valid()):
            user = self.get_user(pk=pk)
            lr = serializer.save()
            user.last_resort_reminders.add(lr)
            return Response(serializer.data, status.HTTP_201_CREATED)
        return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)


class UserLastResortRemindersDetail(views.APIView):
    """
    Calls that deal with getting, putting, and deleting a single last resort reminder.
    """
    def get_reminder(self, pk):
        try:
            return LastResortReminder.objects.get(pk=pk)
        except LastResortReminder.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
    
    def delete(self, request, pk, pk_reminder, format=None):
        reminder = self.get_reminder(pk=pk_reminder)
        reminder.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)

def calcDistance(lat1, lon1, lat2, lon2):
    """
    Calculate the great circle distance between two points 
    on the earth (specified in decimal degrees)
    """
    # convert decimal degrees to radians 
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    # haversine formula 
    dlon = lon2 - lon1 
    dlat = lat2 - lat1 
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a)) 
    km = 6367 * c
    return km * 1000


class CustomObtainAuthToken(ObtainAuthToken):
    """
    Class for getting a token used for login.
    """
    def post(self, request, *args, **kwargs):
        response = super(CustomObtainAuthToken, self).post(request, *args, **kwargs)
        token = Token.objects.get(key=response.data['token'])
        return Response({'token': token.key, 'id': token.user_id})