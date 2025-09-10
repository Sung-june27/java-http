package com.techcourse.dto;

import java.util.Map;
import org.apache.coyote.http11.request.HttpRequest;

public record RegisterRequest(
        String account,
        String email,
        String password
) {

    public static RegisterRequest from(HttpRequest request) {
        final Map<String, String> body = request.getBody();

        return new RegisterRequest(
                body.get("account"),
                body.get("email"),
                body.get("password")
        );
    }
}
