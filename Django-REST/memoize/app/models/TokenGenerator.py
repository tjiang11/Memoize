from django.conf import settings # pragma: no cover
from django.db.models.signals import post_save # pragma: no cover
from django.dispatch import receiver # pragma: no cover
from rest_framework.authtoken.models import Token # pragma: no cover

@receiver(post_save, sender=settings.AUTH_USER_MODEL) # pragma: no cover
def create_auth_token(sender, instance=None, created=False, **kwargs): 
	"""
	Creates a token used to allow the user to login.
	"""
	if created:
		Token.objects.create(user=instance)