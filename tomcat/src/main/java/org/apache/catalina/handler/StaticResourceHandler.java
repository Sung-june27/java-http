package org.apache.catalina.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.catalina.handler.util.ContentTypeExtractor;
import org.apache.catalina.handler.util.StaticResourceLoader;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

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
        if (body.isBlank()) {
            response.redirect("/404.html");

            return response;
        }
        response.setStatus(HttpStatus.OK);
        response.setHeaders(Map.of(
                "Content-Type", ContentTypeExtractor.extract(request.getPath()),
                "Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length)
        ));
        response.setBody(body);

        return response;
    }
}
