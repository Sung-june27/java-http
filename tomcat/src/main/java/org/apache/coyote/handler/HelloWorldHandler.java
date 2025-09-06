package org.apache.coyote.handler;

import org.apache.coyote.HttpRequest;

public class HelloWorldHandler implements Handler {

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.equals("/");
    }

    @Override
    public String handle(final HttpRequest request) {
        final var responseBody = "Hello world!";

        return String.join("\r\n",
                "HTTP/1.1 200 OK ",
                "Content-Type: text/html;charset=utf-8 ",
                "Content-Length: 12 ",
                "",
                responseBody);
    }
}
