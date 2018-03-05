package com.morrisons.base.dropwizard.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morrisons.base.dropwizard.dto.ApiErrorResponse;
import com.morrisons.base.dropwizard.exceptions.ApplicationException;
import com.morrisons.base.dropwizard.exceptions.ErrorSequences;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.morrisons.base.dropwizard.filters.GeneralExceptionMapper.API_INDEX;
import static org.apache.commons.lang3.StringUtils.leftPad;

@Slf4j
@Provider
public class ApplicationExceptionMapper implements ExceptionMapper<ApplicationException> {

    @Context
    private HttpHeaders headers;

    @Override
    public Response toResponse(ApplicationException e) {
        ErrorSequences error = e.getErrorSequences();
        Integer returnCode = error.getReturnCode();
        Integer errorSequence = error.getErrorSequence();
        String errorCode = String.format("%s.%s.%s",
                leftPad(String.valueOf(returnCode), 3, '0'),
                leftPad(String.valueOf(API_INDEX), 2, '0'),
                leftPad(String.valueOf(errorSequence), 3, '0'));

        String errorMessage = error.getMessage();

        ApiErrorResponse errorResponse = new ApiErrorResponse(returnCode, errorCode, errorMessage);

        try {
            log.error("EX: {}, caused by: ", new ObjectMapper().writeValueAsString(errorResponse), e.getCause());
        } catch (JsonProcessingException ex) {
            throw new ApplicationException(e, ErrorSequences.JSON_PARSING_ERROR);
        }

        Response.ResponseBuilder responseBuilder = Response
                .status(returnCode)
                .entity(errorResponse)
                .type(headers.getMediaType());

        return responseBuilder.build();
    }
}
