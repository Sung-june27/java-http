package org.apache.coyote;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
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
                    "Content-Type: " + getContentType(resourcePath) + ";charset=utf-8",
                    "Content-Length: " + responseBody.getBytes().length,
                    "",
                    responseBody);
        }
    }

    private String getContentType(final String resourcePath) throws IOException {
        return Files.probeContentType(Paths.get(resourcePath));
    }
}
