package org.apache.coyote.http11;

import com.techcourse.exception.UncheckedServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import org.apache.HttpCookie;
import org.apache.HttpMethod;
import org.apache.catalina.Manager;
import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;
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

    private HttpRequest getRequest(final InputStream inputStream) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // Start Line (Method Path Protocol)
        final String startLine = bufferedReader.readLine();
        final String[] splitStartLine = startLine.split(" ");
        final HttpMethod method = HttpMethod.valueOf(splitStartLine[0]);
        final String path = splitStartLine[1];

        // Headers
        final Map<String, String> headers = parseHeaders(bufferedReader);
        final HttpCookie httpCookie = parseCookie(headers);
        final Session session = parseSession(httpCookie);
        final String body = parseBody(bufferedReader, headers);

        return new HttpRequest(method, path, headers, httpCookie, body, session);
    }

    private static Map<String, String> parseHeaders(final BufferedReader bufferedReader) throws IOException {
        final Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
            final String[] pair = line.split(": ");
            headers.put(pair[0], pair[1]);
        }

        return headers;
    }

    private HttpCookie parseCookie(final Map<String, String> headers) {
        final HttpCookie httpCookie = new HttpCookie();
        final String cookie = headers.get("Cookie");
        httpCookie.setCookies(cookie);

        return httpCookie;
    }

    private Session parseSession(final HttpCookie httpCookie) throws IOException {
        final Manager manager = SessionManager.getInstance();
        final String jsessionid = httpCookie.getCookie("JSESSIONID");
        if (jsessionid == null) {
            return null;
        }

        return manager.findSession(jsessionid);
    }

    private String parseBody(
            final BufferedReader bufferedReader,
            final Map<String, String> headers
    ) throws IOException {
        final String contentLengthHeader = headers.get("Content-Length");
        if (contentLengthHeader == null) {
            return null;
        }
        int contentLength = Integer.parseInt(contentLengthHeader);
        char[] buffer = new char[contentLength];
        bufferedReader.read(buffer, 0, contentLength);

        return new String(buffer);
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
}
