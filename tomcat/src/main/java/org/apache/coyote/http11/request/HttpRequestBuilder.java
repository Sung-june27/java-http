package org.apache.coyote.http11.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.coyote.http11.HttpMethod;
import org.apache.catalina.Manager;
import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;

public class HttpRequestBuilder {

    public static HttpRequest build(final InputStream inputStream) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        // Start Line (Method Path Protocol)
        final String startLine = bufferedReader.readLine();
        final String[] splitStartLine = startLine.split(" ");
        final HttpMethod method = HttpMethod.valueOf(splitStartLine[0]);
        final String path = splitStartLine[1];

        // Headers
        final Map<String, List<String>> headers = parseHeaders(bufferedReader);
        final HttpCookie httpCookie = parseCookie(headers);
        final Session session = parseSession(httpCookie);
        final String body = parseBody(bufferedReader, headers);

        return new HttpRequest(method, path, headers, httpCookie, body, session);
    }

    private static Map<String, List<String>> parseHeaders(final BufferedReader bufferedReader) throws IOException {
        final Map<String, List<String>> headers = new HashMap<>();
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
            final String[] pair = line.split(": ");
            final String key = pair[0];
            final String value = pair[1];
            final List<String> values = headers.getOrDefault(key, new ArrayList<>());
            values.add(value);
            headers.put(key, values);
        }

        return headers;
    }

    private static HttpCookie parseCookie(final Map<String, List<String>> headers) {
        final HttpCookie httpCookie = new HttpCookie();
        final List<String> cookies = headers.get("Cookie");
        for (String cookie : cookies) {
            final String[] pair = cookie.split("=");
            httpCookie.setCookie(pair[0], pair[1]);
        }

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
            final Map<String, List<String>> headers
    ) throws IOException {
        final String contentLengthHeader = headers.get("Content-Length").getFirst();
        if (contentLengthHeader == null) {
            return null;
        }
        int contentLength = Integer.parseInt(contentLengthHeader);
        char[] buffer = new char[contentLength];
        bufferedReader.read(buffer, 0, contentLength);

        return new String(buffer);
    }
}
