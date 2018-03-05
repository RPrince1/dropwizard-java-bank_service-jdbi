package com.morrisons.base.dropwizard.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.eclipse.jetty.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorSequences {
    AN_UNEXPECTED_ERROR_HAS_OCCURRED("An unexpected error has occurred", INTERNAL_SERVER_ERROR_500, 1),
    NO_TRANSACTIONS_FOUND("No transactions were found for account ID", NOT_FOUND_404, 2),
    JSON_PARSING_ERROR("A JSON parsing error occurred", UNPROCESSABLE_ENTITY_422, 3),
    CONNECTION_FAILED("The HTTP(S) Connection could not be established", INTERNAL_SERVER_ERROR_500, 17),
    SPECIFIED_CONFIGURATION_FILE_COULD_NOT_BE_LOADED("Failed to load the specified configuration file", INTERNAL_SERVER_ERROR_500, 19),
    ;


    private final String message;   // friendly display message
    private final Integer returnCode; //major return code
    private final Integer errorSequence; //minor return code
}
