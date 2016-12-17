from django.http import HttpResponse, Http404
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth.models import User
from rest_framework import viewsets, views, status, generics, permissions
from rest_framework.response import Response
from rest_framework.authentication import TokenAuthentication
from memoize.app.models import Event, MemGroup, TimeReminder, LastResortReminder
from memoize.app.serializers import UserSerializer, UserUpdateSerializer, MemGroupSerializer, EventSerializer, IDSerializer, TimeReminderSerializer, LocationReminderSerializer, LastResortReminderSerializer
from memoize.app.permissions import IsOwnerOrReadOnlyEvent, IsOwnerOrReadOnlyGroup, IsOwnerOrReadOnlyUser
from math import radians, cos, sin, asin, sqrt
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.authtoken.models import Token
import datetime
from datetime import timedelta

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


class TestView(views.APIView):
    def get(self, request, format=None):
        return Response("Hello REST World")


#Consider using mix-ins http://www.django-rest-framework.org/tutorial/3-class-based-views/#using-mixins

class event_list(views.APIView):
   # permission_classes = (permissions.IsAuthenticatedOrReadOnly,)
    def get(self, request, format=None):
        events = Event.objects.all()
        serializer = EventSerializer(events, many=True)
        return Response(serializer.data)

    def post(self, request, format=None):
        serializer = EventSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class event_detail(views.APIView):
   # permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnlyEvent)
    def get_object(self, pk):
        try:
            return Event.objects.get(pk=pk)
        except Event.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        event = self.get_object(pk)
        serializer = EventSerializer(event)
        return Response(serializer.data)

    def put(self, request, pk, format=None):
        event = self.get_object(pk)
        serializer = EventSerializer(event, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data)

    def delete(self, request, pk, format=None):
        event = self.get_object(pk)
        event.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class GroupList(generics.ListCreateAPIView):
   # permission_classes = (permissions.IsAuthenticatedOrReadOnly,)
    queryset = MemGroup.objects.all()
    serializer_class = MemGroupSerializer


class GroupDetail(generics.RetrieveUpdateDestroyAPIView):
    #permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnlyGroup)
    queryset = MemGroup.objects.all()
    serializer_class = MemGroupSerializer

class GroupEvents(views.APIView):
    serializer_class = EventSerializer

    def get_group(self, request, pk):
        try:
            return MemGroup.objects.get(pk=pk)
        except MemGroup.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        group = self.get_group(request, pk)
        serializer = EventSerializer(group.events, many=True)
        return Response(serializer.data)

    def post(self, request, pk, format=None):
        group = self.get_group(request, pk)
        serializer = EventSerializer(data=request.data)
        if (serializer.is_valid()):
            event = serializer.save()
            group.events.add(event)
            return Response(serializer.data, status.HTTP_201_CREATED)
        return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)
        

class UserList(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer

class UserDetail(generics.RetrieveUpdateAPIView):
    authentication_classes = (TokenAuthentication, )
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnlyUser)
    queryset = User.objects.all()
    serializer_class = UserUpdateSerializer

class UserGroups(views.APIView):
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get_group(self, request, pk):
        try:
            return MemGroup.objects.get(pk=request.data['group_id'])
        except MemGroup.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        user = self.get_user(pk=pk)
        serializer = MemGroupSerializer(user.sub_groups, many=True)
        return Response(serializer.data)

    def post(self, request, pk, format=None):
        serializer = IDSerializer(data=request.data)
        if (serializer.is_valid()):
            group = self.get_group(request, pk)
            user = self.get_user(pk=pk)
            user.sub_groups.add(group)
            return Response(serializer.data, status.HTTP_201_CREATED)
        return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)

class UserGroupsDetail(views.APIView):
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get_group(self, pk):
        try:
            return MemGroup.objects.get(pk=pk)
        except MemGroup.DoesNotExist:
            raise Http404

    #Not necessary. Make a request to groups/:id instead.
    # def get(self, request, pk_user, pk_group, format=None):
    #     user = self.get_user(pk=pk_user)
    #     group = self.get_group(pk=pk_group)
    #     serializer = MemGroupSerializer(group)
    #     if group in user.sub_groups.all():
    #         return Response(serializer.data)
    #     return Response(status=status.HTTP_404_NOT_FOUND)

    def delete(self, request, pk_user, pk_group, format=None):
        user = self.get_user(pk=pk_user)
        group = self.get_group(pk=pk_group)
        user.sub_groups.remove(group)
        return Response()

    
class UserTimeReminders(views.APIView):
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        user = self.get_user(pk=pk)
        serializer = TimeReminderSerializer(user.time_reminders, many=True)
        return Response(serializer.data)

    def post(self, request, pk, format=None):
        serializer = TimeReminderSerializer(data=request.data)
        if (serializer.is_valid()):
            user = self.get_user(pk=pk)
            tr = serializer.save()
            user.time_reminders.add(tr)
            return Response(serializer.data, status.HTTP_201_CREATED)
        return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)

class UserTimeRemindersDetail(views.APIView):
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

class UserLocationReminders(views.APIView):
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
        print length
        for i in range(length):
            lat = float(serializer.data[i]['latitude'])
            lon = float(serializer.data[i]['longitude'])

            print lat
            print lon
            distance_in_meters = calcDistance(current_lat, current_lon, lat, lon)
            print distance_in_meters
            if distance_in_meters < 100.0:
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
            event_time = datetime.datetime.strptime(time, "%Y-%m-%dT%H:%M:00Z")
            #print event_time
            current_time = datetime.datetime.now()
            tdelta =  event_time - current_time
            zero_tdelta = timedelta(days=0, seconds=0, microseconds=0)
            print distance_in_meters
            print tdelta

            if distance_in_meters < 800:
                expected_time_to_event = 10
                expected_time_to_event += distance_in_meters * (1.0 / 80.0)
            elif distance_in_meters < 32186.9:
                expected_time_to_event = 10
                expected_time_to_event += (distance_in_meters / 1609.34) * 4
            else:
                expected_time_to_event = 10
                expected_time_to_event += (distance_in_meters / 1609.34) * 2

            if tdelta.total_seconds() < expected_time_to_event * 60 and tdelta > zero_tdelta:
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
    def post(self, request, *args, **kwargs):
        response = super(CustomObtainAuthToken, self).post(request, *args, **kwargs)
        token = Token.objects.get(key=response.data['token'])
        return Response({'token': token.key, 'id': token.user_id})