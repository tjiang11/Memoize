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

# class UserViewSet(viewsets.ModelViewSet):
#     """
#     API endpoint that allows users to be viewed or edited.
#     """
#     queryset = User.objects.all().order_by('-date_joined')
#     serializer_class = UserSerializer


# class GroupViewSet(viewsets.ModelViewSet):
#     """
#     API endpoint that allows groups to be viewed or edited.
#     """
#     queryset = Group.objects.all()
#     serializer_class = GroupSerializer


class TestView(views.APIView): # pragma: no cover
    def get(self, request, format=None):
        return Response("Hello REST World")


#Consider using mix-ins http://www.django-rest-framework.org/tutorial/3-class-based-views/#using-mixins


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
        user = self.get_user(pk=pk)
        serializer = TimeReminderSerializer(user.time_reminders, many=True)
        
        if 'get_current' not in request.GET: #modify
        #     to_be_displayed = []
        #     for element in serializer.data:
        #         reminder_time = element['time']
        #         curr_time = datetime.datetime.now() 
        #         datetime_of_reminder = datetime.datetime.strptime(reminder_time, "%Y-%m-%dT%H:%M:00Z")
        #         time_diff = datetime_of_reminder - curr_time
               
        #         seconds_diff = time_diff.total_seconds()
        #         print str(time_diff)
        #         print str(seconds_diff)
        #         if (seconds_diff > 0):
        #             to_be_displayed.append(element)


            return Response(serializer.data)

        time_to_display = []
        for element in serializer.data:
            reminder_time = element['time']
            curr_time = datetime.datetime.now()

            datetime_of_reminder = datetime.datetime.strptime(reminder_time, "%Y-%m-%dT%H:%M:00Z")
            #print str(datetime_object)
            print "this is the time of the reminder: " + str(datetime_of_reminder)
            print "this is the current time: " + str(curr_time)
            test = datetime_of_reminder - curr_time
            print "total seconds of difference: " + str(test.total_seconds())
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
        #user = self.get_user(pk=pk)
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
        user = self.get_user(pk=pk)
        
        if 'longitude' in request.GET and 'latitude' in request.GET:
            #print request.GET['longitude']
            #print request.GET['latitude']

            current_lon = float(request.GET['longitude'])
            current_lat = float(request.GET['latitude'])

        else:
            serializer = LocationReminderSerializer(user.location_reminders, many=True)
            return Response(serializer.data)

        #print current_lat
        #print current_lon

        serializer = LocationReminderSerializer(user.location_reminders, many=True)
        length = len(serializer.data)
        nearby = []
        #print length
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
        user = self.get_user(pk=pk)
        


        if 'longitude' in request.GET and 'latitude' in request.GET:
            #print request.GET['longitude']
            #print request.GET['latitude']

            current_lon = float(request.GET['longitude'])
            current_lat = float(request.GET['latitude'])

        else:
            serializer = LastResortReminderSerializer(user.last_resort_reminders, many=True)
            return Response(serializer.data)
        #print current_lat
        #print current_lon

        serializer = LastResortReminderSerializer(user.last_resort_reminders, many=True)
        length = len(serializer.data)
        nearby = []
        for i in range(length):
            lat = float(serializer.data[i]['latitude'])
            lon = float(serializer.data[i]['longitude'])
            time = serializer.data[i]['time']

            distance_in_meters = calcDistance(current_lat, current_lon, lat, lon)
            print "serializer time: " + time
            event_time = datetime.datetime.strptime(time, "%Y-%m-%dT%H:%M:00Z")
            print "this is event time: " + str(event_time)
            current_time = datetime.datetime.now()
            tdelta =  event_time - current_time
            zero_tdelta = timedelta(days=0, seconds=0, microseconds=0)
            print "event time: " + str(event_time)
            print str(distance_in_meters) + ' meters'
            print tdelta.total_seconds()

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
                print "adding to return with the following info:"
                print "distance in meters: " + str(distance_in_meters) + ' meters'
                print "diffeence between event time and now: " + str(tdelta.total_seconds()) + " seconds"
                print "expected time to event: " + str(expected_time_to_event)
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