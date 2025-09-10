package com.techcourse.controller;

import com.techcourse.model.User;
import com.techcourse.service.LoginService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.catalina.handler.AbstractController;
import org.apache.catalina.handler.util.StaticResourceLoader;
import org.apache.catalina.session.Session;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpCookie;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class LoginController extends AbstractController {

    private final LoginService loginService;

    public LoginController(final LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean canHandle(final String path) {
        return path.startsWith("/login");
    }

    @Override
    protected void doGet(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException {
        // Session이 존재한다면, redirect
        if (alreadyLogin(request)) {
            response.redirect("/index.html");

            return;
        }
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
        try {
            final User user = loginService.login(request);

            // 로그인에 성공한다면, Session 설정
            setSession(request, response, user);
            response.redirect("/index.html");
        } catch (IllegalArgumentException e) {
            response.redirect("/401.html");
        }
    }

    private boolean alreadyLogin(final HttpRequest request) {
        final Session session = request.getSession(false);

        return session != null && session.getAttribute("user") != null;
    }

    private void setSession(
            final HttpRequest request,
            final HttpResponse response,
            final User user
    ) {
        final HttpCookie cookie = request.getCookie();
        final Session session = request.getSession(true);
        session.setAttribute("user", user);
        cookie.setCookie("JSESSIONID", session.getId());
        response.setCookie(cookie);
    }
}
