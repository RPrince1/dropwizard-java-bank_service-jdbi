Club Dropwizard application
====================================

Running the application
-----------------------
To run the application from within your IDE, start from **ClubsApplication**.java with the following command line arguments:
**server** *path_to_config_file*.yml

About the application
---------------------
TO BE COMPLETED


Test page
----------------------
This  application starts up a Dropwizard instance on **localhost port 8090** with two resources serving the text
*Hello, world!*. We expect one to disappear during development. The endpoint can be accessed at two resources (when the application is running):
*http://localhost:8090/test/helloworld*
*http://localhost:8090/clubType/helloworld*

How to build the app
-----------------------
gradlew clean build will create a jar
