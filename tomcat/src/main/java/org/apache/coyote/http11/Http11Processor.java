package org.apache.coyote.http11;

import com.techcourse.exception.UncheckedServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.apache.HttpMethod;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.HttpResponse;
import org.apache.coyote.Processor;
import org.apache.coyote.ResponseBuilder;
import org.apache.coyote.handler.Handler;
import org.apache.coyote.handler.HandlerMapper;
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
            final ResponseBuilder responseBuilder = new ResponseBuilder();

            final HttpRequest httpRequest = getRequest(inputStream);
            final HttpResponse httpResponse = getResponse(httpRequest);
            final String response = responseBuilder.build(httpResponse);

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private HttpResponse getResponse(final HttpRequest request) throws IOException {
        final HandlerMapper handlerMapper = HandlerMapper.getInstance();
        final Handler handler = handlerMapper.getHandler(request);
        final HttpResponse response = new HttpResponse();
        if (handler == null) {
            response.redirect("/404.html");

            return response;
        }

        return handler.handle(request, response);
    }

    // TODO: 래픽터링 (Request 객체에게 옮길지 / Factory 클래스를 만들지 고민)
    private HttpRequest getRequest(final InputStream inputStream) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        final List<String> headers = new ArrayList<>();
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
            headers.add(line);
        }
        final String startLine = headers.getFirst();
        final String[] splitStartLine = startLine.split(" ");
        final HttpMethod httpMethod = HttpMethod.valueOf(splitStartLine[0]);
        final String requestUri = splitStartLine[1];
        final String contentLengthHeader = headers.stream()
                .filter(str -> str.startsWith("Content-Length"))
                .findFirst()
                .orElse(null);
        if (contentLengthHeader == null) {
            return new HttpRequest(httpMethod, requestUri);
        }

        final int contentLength = Integer.parseInt(contentLengthHeader.split(":")[1].trim());
        final char[] body = new char[contentLength];
        bufferedReader.read(body, 0, contentLength);

        return new HttpRequest(httpMethod, requestUri, new String(body));
    }
}
