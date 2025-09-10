package com.techcourse.controller;

import com.techcourse.dto.RegisterRequest;
import com.techcourse.service.RegisterService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.catalina.handler.AbstractController;
import org.apache.catalina.handler.util.StaticResourceLoader;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class RegisterController extends AbstractController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @Override
    public boolean canHandle(final String path) {
        return path.startsWith("/register");
    }

    @Override
    protected void doGet(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException {
        final String body = StaticResourceLoader.load(request.getPath() + ".html");
        response.setStatus(HttpStatus.OK);
        response.setHeaders(Map.of(
                "Content-Type", "text/html;charset=utf-8",
                "Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length)
        ));
        response.setBody(body);
    }

    @Override
    protected void doPost(
            final HttpRequest request,
            final HttpResponse response
    ) {
        final RegisterRequest registerRequest = RegisterRequest.from(request);
        registerService.register(registerRequest);
        response.redirect("/index.html");
    }
}
