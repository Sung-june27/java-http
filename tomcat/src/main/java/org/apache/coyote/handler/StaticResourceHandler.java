package org.apache.coyote.handler;

import java.io.IOException;
import org.apache.HttpStatus;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.ResponseBuilder;

public class StaticResourceHandler implements Handler {

    private final ResponseBuilder responseBuilder;

    public StaticResourceHandler(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.endsWith(".html") ||
               requestUri.endsWith(".css") ||
               requestUri.endsWith(".js");
    }

    @Override
    public String handle(final HttpRequest request) throws IOException {
        return responseBuilder.buildStaticResponse(HttpStatus.OK, request.getUri());
    }
}
