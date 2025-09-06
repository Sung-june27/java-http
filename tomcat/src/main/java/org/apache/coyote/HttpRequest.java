package org.apache.coyote;

import java.util.HashMap;
import java.util.Map;
import org.apache.HttpCookie;
import org.apache.HttpMethod;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> headers;
    private final HttpCookie cookie;
    private final String body;

    public HttpRequest(
            final HttpMethod method,
            final String path,
            final Map<String, String> headers,
            final HttpCookie cookie,
            final String body
    ) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.cookie = cookie;
        this.body = body;
    }

    public HttpRequest(
            final HttpMethod method,
            final String path,
            final Map<String, String> headers,
            final HttpCookie cookie
    ) {
        this(method, path, headers, cookie, null);
    }

    public Map<String, String> getBody() {
        final Map<String, String> params = new HashMap<>();
        final String[] queries = body.split("&");
        for (String query : queries) {
            final String[] pair = query.split("=");
            params.put(pair[0], pair[1]);
        }

        return params;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpCookie getCookie() {
        return cookie;
    }
}
