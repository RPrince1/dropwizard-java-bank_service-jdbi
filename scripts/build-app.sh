#!/bin/sh
set -e
./gradlew clean build jarIt
if [ "$?" -ne 0 ]; then
    echo -e '\E[30;41m'
    cat scripts/buildflags/dos_failed.txt
    echo -e '\E[0m'
    exit 1
else
    echo -e '\E[30;42m'
    cat scripts/buildflags/dos_passed.txt
    echo -e '\E[0m'
    exit 0
fi