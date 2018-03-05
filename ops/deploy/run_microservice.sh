#!/bin/sh
# Hook script for microservice.
# Pass in environment string which can be 'DEV', 'SIT', 'UAT' ,'PROD'
environment="$1"
listening_port="-1"

if [ "$environment" = "DEV" ] ; then
    listening_port=19000
elif [ "$environment" = "SIT" ] ; then
    listening_port=19010
elif [ "$environment" = "UAT" ] ; then
    listening_port=19015
#elif [ "$environment" = "PROD" ] ; then
#    listening_port=19095
else
    echo 'You must specify an environment as the 1st argument!'
    echo 'Usage: run_microservice.sh <DEV|SIT|UAT|PROD>'
fi

echo "Environment = ${environment}"
echo "Listening port = ${listening_port}"

docker run -d --restart=always -v "/app/mus/service/logs/${environment}":/app/mus/service/logs -p "8080:${listening_port}" -p 8081:8081 --name="service_${environment}" "morrisons/service:${environment}" "/app/mus/service/bin/runApp" "/app/mus/service/config/application_${environment}.yml"