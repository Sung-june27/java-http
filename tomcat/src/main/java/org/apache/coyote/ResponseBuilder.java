package org.apache.coyote;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.HttpStatus;

public class ResponseBuilder {

    public String buildStaticResponse(
            final HttpStatus httpStatus,
            final String requestTarget
    ) throws IOException {
        final String resourcePath = "static" + requestTarget;
        try (final InputStream resource = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (resource == null) {
                return buildStaticResponse(HttpStatus.NOT_FOUND, "/404.html");
            }
            final var responseBody = new String(resource.readAllBytes(), StandardCharsets.UTF_8);

            return String.join("\r\n",
                    "HTTP/1.1 " + httpStatus.getStatusCode() + " " + httpStatus.getReasonPhrase(),
                    createHeaders(httpStatus, resourcePath, responseBody, requestTarget),
                    "",
                    responseBody);
        }
    }

    private String getContentType(final String resourcePath) throws IOException {
        return Files.probeContentType(Paths.get(resourcePath)) + ";charset=utf-8";
    }

    public String createHeaders(
            final HttpStatus httpStatus,
            final String resourcePath,
            final String responseBody,
            final String requestTarget
    ) throws IOException {
        final Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", getContentType(resourcePath));
        headers.put("Content-Length", String.valueOf(responseBody.getBytes(StandardCharsets.UTF_8).length));
        if (HttpStatus.FOUND.equals(httpStatus)) {
            headers.put("Location", requestTarget);
        }

        return headers.keySet()
                .stream()
                .map(key -> key + ": " + headers.get(key))
                .collect(Collectors.joining("\r\n"));
    }
}
