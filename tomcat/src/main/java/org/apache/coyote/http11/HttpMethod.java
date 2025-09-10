package org.apache.coyote.http11;

public enum HttpMethod {

    GET("GET"),
    POST("POST"),
    ;

    private final String method;

    HttpMethod(final String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
