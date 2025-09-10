package org.apache.coyote.http11.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpCookie;

public class HttpResponse {

    private static final String CRLF = "\r\n";

    private HttpStatus status = HttpStatus.OK;
    private Map<String, String> headers = new HashMap<>();
    private HttpCookie cookie;
    private String body;

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

    public void send(final OutputStream outputStream) throws IOException {
        outputStream.write(formatResponse().getBytes());
        outputStream.flush();
    }

    private String formatResponse() {
        final String statusLine = "HTTP/1.1 " + this.status.getCode() + " " + this.status.getMessage();
        final String headerLines = parseHeaders();
        final String body = getBody();

        // response 연결
        final StringBuilder sb = new StringBuilder();
        sb.append(statusLine).append(CRLF);
        if (!headerLines.isEmpty()) {
            sb.append(headerLines).append(CRLF);
        }
        if (this.body != null) {
            sb.append(CRLF).append(body);
        }

        return sb.toString();
    }

    private String parseHeaders() {
        if (this.headers == null || this.headers.isEmpty()) {
            return "";
        }

        return headers.entrySet()
                .stream()
                .map(header -> header.getKey() + ": " + header.getValue())
                .collect(Collectors.joining(CRLF));
    }
}
