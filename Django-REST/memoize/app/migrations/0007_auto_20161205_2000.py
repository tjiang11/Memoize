# -*- coding: utf-8 -*-
# Generated by Django 1.10.2 on 2016-12-05 20:00
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('app', '0006_auto_20161116_2206'),
    ]

    operations = [
        migrations.AlterField(
            model_name='locationreminder',
            name='description',
            field=models.CharField(blank=True, max_length=1000, null=True),
        ),
        migrations.AlterField(
            model_name='locationreminder',
            name='latitude',
            field=models.DecimalField(decimal_places=8, max_digits=11),
        ),
        migrations.AlterField(
            model_name='locationreminder',
            name='longitude',
            field=models.DecimalField(decimal_places=8, max_digits=11),
        ),
        migrations.AlterField(
            model_name='memgroup',
            name='description',
            field=models.CharField(blank=True, max_length=1000, null=True),
        ),
        migrations.AlterField(
            model_name='timereminder',
            name='description',
            field=models.CharField(blank=True, max_length=1000, null=True),
        ),
    ]
