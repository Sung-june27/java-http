package com.techcourse.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.catalina.handler.AbstractController;
import org.apache.catalina.handler.util.ContentTypeExtractor;
import org.apache.catalina.handler.util.StaticResourceLoader;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class StaticResourceController extends AbstractController {

    @Override
    public boolean canHandle(final String path) {
        return path.endsWith(".html") ||
               path.endsWith(".css") ||
               path.endsWith(".js") ||
               path.endsWith(".svg");
    }

    @Override
    protected void doGet(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException {
        final String body = StaticResourceLoader.load(request.getPath());
        if (body.isBlank()) {
            response.redirect("/404.html");
            return;
        }
        response.setStatus(HttpStatus.OK);
        response.setHeaders(Map.of(
                "Content-Type", ContentTypeExtractor.extract(request.getPath()),
                "Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length)
        ));
        response.setBody(body);
    }
}
