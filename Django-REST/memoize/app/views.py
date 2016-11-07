
from django.http import HttpResponse, Http404
from django.views.decorators.csrf import csrf_exempt
from django.contrib.auth.models import User
from rest_framework import viewsets, views, status, generics, permissions
from rest_framework.response import Response
from memoize.app.models import Event, MemGroup
from memoize.app.serializers import UserSerializer, UserUpdateSerializer, MemGroupSerializer, EventSerializer, IDSerializer
from memoize.app.permissions import IsOwnerOrReadOnlyEvent, IsOwnerOrReadOnlyGroup, IsOwnerOrReadOnlyUser


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
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,)
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
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnlyEvent)
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
    permission_classes = (permissions.IsAuthenticatedOrReadOnly,)
    queryset = MemGroup.objects.all()
    serializer_class = MemGroupSerializer


class GroupDetail(generics.RetrieveUpdateDestroyAPIView):
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnlyGroup)
    queryset = MemGroup.objects.all()
    serializer_class = MemGroupSerializer


class UserList(generics.ListCreateAPIView):
    queryset = User.objects.all()
    serializer_class = UserSerializer

class UserDetail(generics.RetrieveUpdateAPIView):
    permission_classes = (permissions.IsAuthenticatedOrReadOnly, IsOwnerOrReadOnlyUser)
    queryset = User.objects.all()
    serializer_class = UserUpdateSerializer

class UserGroups(views.APIView):
    def get_user(self, pk):
        try:
            return User.objects.get(pk=pk)
        except User.DoesNotExist:
            raise Http404

    def get(self, request, pk, format=None):
        user = self.get_user(pk=pk)
        serializer = MemGroupSerializer(user.sub_groups, many=True)
        return Response(serializer.data)

    def post(self, request, pk, format=None):
        serializer = IDSerializer(data=request.data)
        if (serializer.is_valid()):
            group = MemGroup.objects.get(pk=request.data['group_id'])
            user = self.get_user(pk=pk)
            user.sub_groups.add(group)
            return Response(serializer.data, status.HTTP_201_CREATED)
        return Response(serializer.errors, status.HTTP_400_BAD_REQUEST)
    