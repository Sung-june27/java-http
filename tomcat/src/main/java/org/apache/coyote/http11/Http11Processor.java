package org.apache.coyote.http11;

import com.techcourse.exception.UncheckedServletException;
import java.io.IOException;
import java.net.Socket;
import org.apache.catalina.handler.Controller;
import org.apache.catalina.handler.RequestMapping;
import org.apache.coyote.Processor;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.request.HttpRequestBuilder;
import org.apache.coyote.http11.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = connection.getInputStream();
             final var outputStream = connection.getOutputStream()) {

            final HttpRequest httpRequest = HttpRequestBuilder.build(inputStream);
            final HttpResponse httpResponse = getHttpResponse(httpRequest);
            httpResponse.send(outputStream);
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private HttpResponse getHttpResponse(final HttpRequest request) {
        final RequestMapping requestMapping = RequestMapping.getInstance();
        final Controller controller = requestMapping.getController(request);
        final HttpResponse response = new HttpResponse();
        if (controller == null) {
            response.redirect("/404.html");

            return response;
        }
        try {
            controller.service(request, response);
        } catch (Exception e) {
            response.redirect("/500.html");
        }

        return response;
    }
}
