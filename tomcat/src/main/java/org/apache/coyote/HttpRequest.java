package org.apache.coyote;

import java.util.HashMap;
import java.util.Map;
import org.apache.HttpMethod;

public class HttpRequest {

    private final HttpMethod method;
    private final String uri;
    private String body;

    public HttpRequest(
            final HttpMethod method,
            final String uri
    ) {
        this.method = method;
        this.uri = uri;
    }

    public HttpRequest(
            final HttpMethod method,
            final String uri,
            final String body
    ) {
        this.method = method;
        this.uri = uri;
        this.body = body;
    }

    public String getQueryString() {
        final int index = uri.indexOf('?');
        if (index == -1) {
            return "";
        }

        return uri.substring(index + 1);
    }

    public Map<String, String> getParams() {
        final Map<String, String> params = new HashMap<>();
        final String[] queries = getQueryString().split("&");
        for (String query : queries) {
            final String[] pair = query.split("=");
            params.put(pair[0], pair[1]);
        }

        return params;
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

    public String getUri() {
        return uri;
    }
}
