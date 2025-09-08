package org.apache.coyote.handler;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class HelloWorldHandler implements Handler {

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.equals("/");
    }

    @Override
    public HttpResponse handle(
            final HttpRequest request,
            final HttpResponse response
    ) {
        final var responseBody = "Hello world!";
        response.setStatus(HttpStatus.OK);
        response.setHeaders(Map.of(
                "Content-Type", "text/html;charset=utf-8",
                "Content-Length", String.valueOf(responseBody.getBytes(StandardCharsets.UTF_8).length)
        ));
        response.setBody(responseBody);

        return response;
    }
}
