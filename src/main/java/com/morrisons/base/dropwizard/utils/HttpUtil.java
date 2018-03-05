package com.morrisons.base.dropwizard.utils;

import com.diffplug.common.base.Errors;
import com.morrisons.base.dropwizard.exceptions.ApplicationException;
import com.morrisons.base.dropwizard.exceptions.ErrorSequences;
import lombok.experimental.UtilityClass;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.function.Supplier;

import static com.morrisons.base.dropwizard.exceptions.ErrorSequences.AN_UNEXPECTED_ERROR_HAS_OCCURRED;
import static org.apache.http.protocol.HTTP.USER_AGENT;

@UtilityClass
public class HttpUtil {
    public static final Header USER_AGENT_HEADER = new BasicHeader("User-Agent", USER_AGENT);
    public static final Header JSON_HEADER = new BasicHeader("Content-type", "application/json");

    public static HttpResponse httpCall(HttpUriRequest request) {
        final HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            return httpClient.execute(request);
        } catch (IOException e) {
            throw new ApplicationException(ErrorSequences.CONNECTION_FAILED);
        }
    }

    /**
     * Any 2xx code means success.
     *
     * @param response
     * @return true for any 2xx code, false otherwise
     */
    public static boolean isSuccess(HttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        return status == HttpStatus.SC_ACCEPTED || status == HttpStatus.SC_OK;
    }

    public static BasicHeader getCustomAuthHeader(String authString) {
        return new BasicHeader("Authorization", authString);
    }

    /**
     * Any 2xx code means success.
     *
     * @param status
     * @return true for any 2xx code, false otherwise
     */
    public static boolean isSuccess(StatusLine status) {
        return status.getStatusCode() / 100 == 2;
    }

    public static Response withExceptionHandler(Supplier<Response> supplier) {
        try {
            return supplier.get();
        } catch (ApplicationException e) {
            throw new ApplicationException(e.getErrorSequences());
        } catch (Throwable e) {
            throw new ApplicationException(e, AN_UNEXPECTED_ERROR_HAS_OCCURRED);
        }
    }

    public static String fromEntity(HttpEntity entity) {
        return Errors.rethrow().get(() -> EntityUtils.toString(entity));
    }

}
