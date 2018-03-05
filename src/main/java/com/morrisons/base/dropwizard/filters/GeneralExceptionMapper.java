package com.morrisons.base.dropwizard.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morrisons.base.dropwizard.dto.ApiErrorResponse;
import com.morrisons.base.dropwizard.exceptions.ErrorSequences;
import com.morrisons.base.dropwizard.exceptions.InstanceException;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Slf4j
@Provider
public class GeneralExceptionMapper implements ExceptionMapper<RuntimeException> {

    public static final String API_INDEX = "1";

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(RuntimeException e) {
        if (e instanceof ClientErrorException) {
            ClientErrorException clientError = (ClientErrorException) e;
            log.error("Stack Trace: ", e);

            return clientError.getResponse();
        }

        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .errorCode("0")
                .httpResponseCode(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .errorMessage(e.getMessage()).build();


        try {
            log.error(new ObjectMapper().writeValueAsString(errorResponse));
            log.error("Stack Trace: ", e);
        } catch (JsonProcessingException ex) {
            throw new InstanceException(e, ErrorSequences.JSON_PARSING_ERROR);
        }

        Response.ResponseBuilder responseBuilder = Response
                .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .entity(errorResponse)
                .type(headers.getMediaType());

        return responseBuilder.build();
    }
}
