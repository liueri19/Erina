#!/bin/bash

# classpath, compiled stuff
CP="bin/"

# greenfoot lib
for dir in $(find lib -type d); do
	CP="$CP:$dir/*";
done;

# special java stuff (some javafx things, maybe)
for dir in $(find $JAVA_HOME -type d); do
	CP="$CP:$dir/*";
done;

java -cp "$CP" greenfoot.export.GreenfootScenarioMain $@
