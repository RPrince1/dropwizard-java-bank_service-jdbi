package com.morrisons.base.dropwizard.resources;

/**
 * Created by peter on 28/11/2016.
 */
class ApiConstants {
    static final String BASIC_AUTH = "basicAuth";
    static final String BEARER_AUTH = "apikey";
    static final String BASE_PATH = "/base-dropwizard/v1/v2";
    static final String APP_TITLE = "Base DW API";
    static final String APP_DESCRIPTION = "Base DW Service";
    static final String APP_VERSION = "2.0.0";

    static final String API_400 = "Invalid resource spec";
    static final String API_404 = "Resource ID not found";
    static final String API_422 = "JSON/XML Parsing Error";
    static final String API_500 = "Internal Server Error";
    static final String API_502 = "Bad Gateway";
    static final String API_202 = "Accepted";
    static final String API_200 = "OK";

}
