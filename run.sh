#!/bin/bash
CP="bin/:lib/:lib/*:lib/unpacked_greefoot/"
java -cp "$CP" greenfoot.export.GreenfootScenarioMain $@