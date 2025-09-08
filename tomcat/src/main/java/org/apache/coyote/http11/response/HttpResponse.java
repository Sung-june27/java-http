package org.apache.coyote.http11.response;

import java.util.HashMap;
import java.util.Map;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpCookie;

public class HttpResponse {

    private HttpStatus status = HttpStatus.OK;
    private Map<String, String> headers = new HashMap<>();
    private HttpCookie cookie;
    private String body = "";

    public HttpResponse() {
    }

    public void redirect(final String location) {
        this.status = HttpStatus.FOUND;
        this.headers.put("Location", location);
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    public void setCookie(final HttpCookie cookie) {
        final Map<String, String> cookies = cookie.getCookies();
        cookies.keySet()
                .stream()
                .map(key -> key + "=" + cookies.get(key))
                .forEach(cookieString -> headers.put("Set-Cookie", cookieString));

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
