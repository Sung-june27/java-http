package org.apache.coyote.http11.response;

import java.util.Map;
import java.util.stream.Collectors;
import org.apache.coyote.http11.HttpStatus;

public class ResponseBuilder {

    public static String build(final HttpResponse response) {
        final HttpStatus httpStatus = response.getStatus();

        return String.join("\r\n",
                "HTTP/1.1 " + httpStatus.getStatusCode() + " " + httpStatus.getReasonPhrase(),
                parseHeaders(response),
                "",
                response.getBody());
    }

    private static String parseHeaders(final HttpResponse response) {
        final Map<String, String> headers = response.getHeaders();

        return headers.keySet()
                .stream()
                .map(key -> key + ": " + headers.get(key))
                .collect(Collectors.joining("\r\n"));
    }
}
