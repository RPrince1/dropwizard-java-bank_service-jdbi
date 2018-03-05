Example JDBI Dropwizard application
====================================

Running the application
-----------------------
To run the application from within your IDE, start from **BaseDWApplication**.java with the following command line arguments:
**server** *path_to_config_file*.yml

About the application
---------------------
TO BE COMPLETED


Test page
----------------------
This  application starts up a Dropwizard instance on **localhost port 8080** with two resources. The endpoint can be accessed at two resources (when the application is running):
- **GET** *http://localhost:8080/example-dropwizard/v1/customers/{accountId}*
- **POST** *http://localhost:8080/example-dropwizard/v1/customers/{accountId}*

How to build the app
-----------------------
gradlew clean build jarIt
