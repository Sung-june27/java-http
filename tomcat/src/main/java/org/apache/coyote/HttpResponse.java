package org.apache.coyote;

import java.util.HashMap;
import java.util.Map;
import org.apache.HttpCookie;
import org.apache.HttpStatus;

public class HttpResponse {

    private HttpStatus status;
    private Map<String, String> headers = new HashMap<>();
    private HttpCookie cookie;
    private String body;

    public HttpResponse() {
    }

    public void redirect(final String location) {
        this.status = HttpStatus.FOUND;
        this.headers.putAll(Map.of(
                "Content-Type", "text/html;charset=utf-8",
                "Content-Length", String.valueOf(0),
                "Location", location
        ));
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public void setCookie(final HttpCookie cookie) {
        headers.put("Set-Cookie", cookie.toString());
        this.cookie = cookie;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }
}
