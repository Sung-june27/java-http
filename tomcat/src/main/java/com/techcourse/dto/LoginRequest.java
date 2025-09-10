package com.techcourse.dto;

import java.util.Map;
import org.apache.coyote.http11.request.HttpRequest;

public record LoginRequest(
        String account,
        String password
) {

    public static LoginRequest from(final HttpRequest request) {
        final Map<String, String> body = request.getBody();

        return new LoginRequest(
                body.get("account"),
                body.get("password")
        );
    }
}
