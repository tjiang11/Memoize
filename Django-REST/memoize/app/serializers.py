from django.contrib.auth.models import User
from rest_framework import serializers
from models import Event, MemGroup, TimeReminder, LocationReminder, LastResortReminder

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'password', 'mem_groups', 'sub_groups', 'location_reminders', 'time_reminders')
        extra_kwargs = {'password': {'write_only': True}}
        read_only_fields = ('location_reminders', 'time_reminders', 'mem_groups' , 'sub_groups', )

    def create(self, validated_data):
        user = User.objects.create_user(username=validated_data['username'], password=validated_data['password'])
        return user

    # def update(self, instance, validated_data):
    # 	instance.username = validated_data['username']
    # 	instance.set_password(validated_data['password'])
    # 	instance.mem_groups = validated_data['mem_groups']
    #     instance.sub_groups = validated_data['sub_groups'] 
    # 	instance.save()
    # 	return instance

class UserUpdateSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'username', 'mem_groups', 'sub_groups')
    def update(self, instance, validated_data):
    	instance.username = validated_data['username']
    	instance.mem_groups = validated_data['mem_groups']
        instance.sub_groups = validated_data['sub_groups'] 
    	instance.save()
    	return instance

class MemGroupSerializer(serializers.ModelSerializer):
	admins = serializers.PrimaryKeyRelatedField(many=True, queryset=User.objects.all())
	subscribers = serializers.PrimaryKeyRelatedField(many=True, queryset=User.objects.all())
	events = serializers.PrimaryKeyRelatedField(many=True, queryset=Event.objects.all())
	class Meta:
		model = MemGroup
		fields = ('id', 'name', 'description', 'admins', 'subscribers', 'events')

class EventSerializer(serializers.ModelSerializer):
	group = serializers.ReadOnlyField(source='group.id')
	class Meta:
		model = Event
		fields = ('name', 'description', 'location_descriptor', 'start_time', 'end_time', 'longitude', 'latitude', 'tags', 'group')

class IDSerializer(serializers.Serializer):
 	group_id = serializers.IntegerField(required=True)

class TimeReminderSerializer(serializers.ModelSerializer):
    class Meta:
        model = TimeReminder
        fields = ('name', 'description', 'location_descriptor', 'time', 'id')
        read_only_fields = ('id', )

class LocationReminderSerializer(serializers.ModelSerializer):
    class Meta:
        model = LocationReminder
        fields = ('name', 'description', 'location_descriptor', 'start_time', 'end_time', 'latitude', 'longitude', 'id')
        read_only_fields = ('id', )

class LastResortReminderSerializer(serializers.ModelSerializer):
    class Meta:
        model = LastResortReminder
        fields = ('name', 'description', 'location_descriptor', 'time', 'latitude', 'longitude', 'id')
        read_only_fields = ('id', )
# class EventSerializer(serializers.Serializer):
# 	name = serializers.CharField(required=True, allow_blank=False, max_length=255)
# 	start_time = serializers.DateTimeField()
# 	end_time = serializers.DateTimeField()
# 	location = serializers.IntegerField()
# 	tags = serializers.CharField(required=True, allow_blank=True)
# 	group = serializers.ReadOnlyField(source='group.name')

# 	def create(self, validated_data):
# 		return Event.objects.create(**validated_data)

# 	def update(self, instance, validated_data):
# 		instance.name = validated_data.get('name', instance.name)
# 		instance.start_time = validated_data.get('start_time', instance.start_time)
# 		instance.end_time = validated_data.get('end_time', instance.end_time)
# 		instance.location = validated_data.get('location', instance.location)
# 		instance.tags = validated_data.get('tags', instance.tags)
# 		instance.save()
# 		return instance