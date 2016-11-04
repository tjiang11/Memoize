# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-11-04 18:16
from __future__ import unicode_literals

from django.conf import settings
from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0008_auto_20161103_1909'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='event',
            name='owner',
        ),
        migrations.AddField(
            model_name='event',
            name='group',
            field=models.ForeignKey(default=1, on_delete=django.db.models.deletion.CASCADE, related_name='events', to='app.MemGroup'),
        ),
        migrations.AlterField(
            model_name='memgroup',
            name='users',
            field=models.ManyToManyField(related_name='mem_groups', to=settings.AUTH_USER_MODEL),
        ),
    ]
