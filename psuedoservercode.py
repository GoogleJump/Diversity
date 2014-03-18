from django.db import models

class Level(models.Model):
    levelNum = models.IntegerField(default = 1)
    stage = models.IntegerField(default = 0) 
	# 0 = riddle_given, 1 = riddle_answered, 2 = location_provided
    def __unicode__(self):
        return self.levelNum

class User(models.Model):
    id = models.CharField(max_length = 20)
    points = models.IntegerField(default = 0)
    categories = models.ManyToManyField(Category)
    levelAt = models.ForeignKey(Level)
    def __unicode__(self):
        return self.id
