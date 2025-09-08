package org.apache.coyote.handler;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.catalina.session.Session;
import org.apache.coyote.http11.HttpMethod;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpCookie;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class LoginHandler implements Handler {

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.startsWith("/login");
    }

    @Override
    public HttpResponse handle(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException {
        if (HttpMethod.GET.equals(request.getMethod())) {
            return doGet(request, response);
        }
        if (HttpMethod.POST.equals(request.getMethod())) {
            return doPost(request, response);
        }

        // TODO: 405 처리 (Controller 이후)
        return response;
    }

    private static HttpResponse doGet(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException {
        // Session이 존재한다면, redirect
        if (request.getSession(false) != null) {
            response.redirect("/index.html");

            return response;
        }
        final String body = StaticResourceLoader.load(request.getPath() + ".html");
        response.setStatus(HttpStatus.OK);
        response.setHeaders(Map.of(
                "Content-Type", "text/html;charset=utf-8",
                "Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length)
        ));
        response.setBody(body);

        return response;
    }

    private HttpResponse doPost(
            final HttpRequest request,
            final HttpResponse response
    ) {
        try {
            final User user = login(request);
            
            // 로그인에 성공한다면, Session 설정
            setSession(request, response, user);
            response.redirect("/index.html");

            return response;
        } catch (IllegalArgumentException e) {
            response.redirect("/401.html");

            return response;
        }
    }

    // TODO: Service 분리 고려해보기 (Controller 도입 후)
    private User login(final HttpRequest request) {
        final Map<String, String> params = request.getBody();
        final User user = InMemoryUserRepository.findByAccount(params.get("account"))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        validatePassword(user, params.get("password"));
        System.out.println("user: " + user);

        return user;
    }
    private void validatePassword(
            final User user,
            final String password
    ) {
        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
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
