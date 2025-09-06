package org.apache.coyote.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.apache.HttpStatus;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.HttpResponse;
import org.apache.coyote.StaticResourceLoader;

public class StaticResourceHandler implements Handler {

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.endsWith(".html") ||
               requestUri.endsWith(".css") ||
               requestUri.endsWith(".js") ||
               requestUri.endsWith(".svg");
    }

    @Override
    public HttpResponse handle(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException {
        final String body = StaticResourceLoader.load(request.getPath());
        response.setStatus(HttpStatus.OK);
        response.setHeaders(Map.of(
                "Content-Type", getContentType(request.getPath()),
                "Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length)
        ));
        response.setBody(body);

        return response;
    }

    private String getContentType(final String uri) throws IOException {
        final String resourcePath = "static" + uri;

        return Files.probeContentType(Paths.get(resourcePath)) + ";charset=utf-8";
    }
}
