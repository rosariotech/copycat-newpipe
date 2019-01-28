#!/bin/bash

javac CheckTranslations.java
find app/srv -name "*.xml" | grep values | xargs java CheckTranslations -r