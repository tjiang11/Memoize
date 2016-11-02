from django.contrib.auth.models import User, Group
from rest_framework import serializers
from models import Event


# class UserSerializer(serializers.HyperlinkedModelSerializer):
#     class Meta:
#         model = User
#         fields = ('url', 'username', 'email', 'groups')


class UserSerializer(serializers.ModelSerializer):
    events = serializers.PrimaryKeyRelatedField(many=True, queryset=Event.objects.all())
    class Meta:
        model = User
        fields = ('id', 'username', 'events')


class GroupSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Group
        fields = ('url', 'name')

class EventSerializer(serializers.Serializer):
	name = serializers.CharField(required=True, allow_blank=False, max_length=255)
	start_time = serializers.DateTimeField()
	end_time = serializers.DateTimeField()
	location = serializers.IntegerField()
	tags = serializers.CharField(required=True, allow_blank=True)

	def create(self, validated_data):
		return Event.objects.create(**validated_data)

	def update(self, instance, validated_data):
		instance.name = validated_data.get('name', instance.name)
		instance.start_time = validated_data.get('start_time', instance.start_time)
		instance.end_time = validated_data.get('end_time', instance.end_time)
		instance.location = validated_data.get('location', instance.location)
		instance.tags = validated_data.get('tags', instance.tags)
		instance.save()
		return instance
