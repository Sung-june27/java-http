package org.apache;

import java.util.Arrays;

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
