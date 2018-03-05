#!/bin/sh

java -Duser.timezone=Europe/London -jar base-dropwizard.jar server app-config.yml
