package org.apache.coyote.http11.response;

import java.util.Map;
import java.util.stream.Collectors;
import org.apache.coyote.http11.HttpStatus;

public class ResponseBuilder {

    private static final String CRLF = "\r\n";

    public static String build(final HttpResponse response) {
        final String statusLine = statusLine(response);
        final String headerLines = parseHeaders(response);
        final String body = response.getBody();

        // response 연결
        final StringBuilder sb = new StringBuilder();
        sb.append(statusLine).append(CRLF);
        if (!headerLines.isEmpty()) {
            sb.append(headerLines).append(CRLF);
        }
        if (body != null) {
            sb.append(CRLF).append(body);
        }

        return sb.toString();
    }

    private static String statusLine(final HttpResponse response) {
        final HttpStatus status = response.getStatus();

        return "HTTP/1.1 " + status.getCode() + " " + status.getMessage();
    }

    private static String parseHeaders(final HttpResponse response) {
        final Map<String, String> headers = response.getHeaders();
        if (headers == null || headers.isEmpty()) {
            return "";
        }

        return headers.entrySet()
                .stream()
                .map(header -> header.getKey() + ": " + header.getValue())
                .collect(Collectors.joining(CRLF));
    }
}
