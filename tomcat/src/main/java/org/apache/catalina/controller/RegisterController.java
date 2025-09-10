package org.apache.catalina.controller;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.catalina.controller.util.StaticResourceLoader;
import org.apache.coyote.http11.HttpStatus;
import org.apache.coyote.http11.request.HttpRequest;
import org.apache.coyote.http11.response.HttpResponse;

public class RegisterController extends AbstractController {

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
        register(request);
        response.redirect("/index.html");
    }

    private void register(final HttpRequest request) {
        final Map<String, String> body = request.getBody();
        final String account = body.get("account");
        final String password = body.get("password");
        final String email = body.get("email");
        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);
    }
}
