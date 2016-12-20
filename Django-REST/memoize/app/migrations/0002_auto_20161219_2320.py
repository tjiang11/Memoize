# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-12-19 23:20
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0001_initial'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='locationreminder',
            name='end_time',
        ),
        migrations.RemoveField(
            model_name='locationreminder',
            name='start_time',
        ),
        migrations.AddField(
            model_name='locationreminder',
            name='radius',
            field=models.IntegerField(default=100),
        ),
    ]