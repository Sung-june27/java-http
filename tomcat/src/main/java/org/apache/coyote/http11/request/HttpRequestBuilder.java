package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.coyote.http11.HttpMethod;
import org.apache.catalina.Manager;
import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;

public class HttpRequestBuilder {

    public static HttpRequest build(final InputStream inputStream) throws IOException {
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

    private static HttpCookie parseCookie(final Map<String, String> headers) {
        final HttpCookie httpCookie = new HttpCookie();
        final String cookie = headers.get("Cookie");
        httpCookie.setCookies(cookie);

        return httpCookie;
    }

    private static Session parseSession(final HttpCookie httpCookie) throws IOException {
        final Manager manager = SessionManager.getInstance();
        final String jsessionid = httpCookie.getCookie("JSESSIONID");
        if (jsessionid == null) {
            return null;
        }

        return manager.findSession(jsessionid);
    }

    private static String parseBody(
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
}
