#!/bin/bash

# compile
javac -Xlint:unchecked -cp lib/:lib/* -d bin/ $(find src/ -name "*.java")

# then copy over resources and properties
cp -r resources/* bin/

for file in $(find src/ -not -name "*.java"); do cp $file bin/; done;
#cp src/standalone.properties bin/
#cp src/project.greenfoot bin/
