package org.apache.coyote;

import java.util.Map;
import java.util.stream.Collectors;
import org.apache.HttpStatus;

public class ResponseBuilder {

    public String build(final HttpResponse response) {
        final HttpStatus httpStatus = response.getStatus();

        return String.join("\r\n",
                "HTTP/1.1 " + httpStatus.getStatusCode() + " " + httpStatus.getReasonPhrase(),
                parseHeaders(response),
                "",
                response.getBody());
    }

    private String parseHeaders(final HttpResponse response) {
        final Map<String, String> headers = response.getHeaders();

        return headers.keySet()
                .stream()
                .map(key -> key + ": " + headers.get(key))
                .collect(Collectors.joining("\r\n"));
    }
}
