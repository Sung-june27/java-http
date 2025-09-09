package org.apache.coyote.http11.request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.catalina.Manager;
import org.apache.catalina.session.Session;
import org.apache.catalina.session.SessionManager;
import org.apache.coyote.http11.HttpMethod;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private final Map<String, String> headers;
    private final HttpCookie cookie;
    private final String body;
    private Session session;

    public HttpRequest(
            final HttpMethod method,
            final String path,
            final Map<String, String> headers,
            final HttpCookie cookie,
            final String body,
            final Session session
    ) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.cookie = cookie;
        this.body = body;
        this.session = session;
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

    public Session getSession(final boolean create) {
        // Session이 존재하지 않는다면, 생성 및 등록
        if (this.session == null && create) {
            final String id = UUID.randomUUID().toString();
            this.session = new Session(id);
            final Manager manager = SessionManager.getInstance();
            manager.add(this.session);
        }

        return this.session;
    }
}
