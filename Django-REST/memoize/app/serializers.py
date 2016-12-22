from django.contrib.auth.models import User # pragma: no cover
from rest_framework import serializers # pragma: no cover
from models import TimeReminder, LocationReminder, LastResortReminder # pragma: no cover

class UserSerializer(serializers.ModelSerializer):
    """
    Serializer for our users.
    """
    class Meta:
        model = User
        fields = ('id', 'username', 'password', 'location_reminders', 'time_reminders')
        extra_kwargs = {'password': {'write_only': True}}
        read_only_fields = ('location_reminders', 'time_reminders')

    def create(self, validated_data): 
        user = User.objects.create_user(username=validated_data['username'], password=validated_data['password'])
        return user

class UserUpdateSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username')
    def update(self, instance, validated_data): 
        instance.username = validated_data['username']
        instance.save()
        return instance

class IDSerializer(serializers.Serializer):
    """
    Serializer for the ID of users and reminders.
    """
    group_id = serializers.IntegerField(required=True)

class TimeReminderSerializer(serializers.ModelSerializer):
    """
    Serializer for time reminders.
    """
    class Meta:
        model = TimeReminder
        fields = ('name', 'description', 'location_descriptor', 'time', 'id')
        read_only_fields = ('id', )

class LocationReminderSerializer(serializers.ModelSerializer):
    """
    Serializer for location reminders.
    """
    class Meta:
        model = LocationReminder
        fields = ('name', 'description', 'location_descriptor', 'latitude', 'longitude','radius', 'id')
        read_only_fields = ('id', )

class LastResortReminderSerializer(serializers.ModelSerializer):
    """
    Serializer for last resort reminders.
    """
    class Meta:
        model = LastResortReminder
        fields = ('name', 'description', 'location_descriptor', 'time', 'latitude', 'longitude', 'id')
        read_only_fields = ('id', )