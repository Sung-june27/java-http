package org.apache.coyote.handler;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.HttpMethod;
import org.apache.HttpStatus;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.HttpResponse;
import org.apache.coyote.StaticResourceLoader;

public class RegisterHandler implements Handler {

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.startsWith("/register");
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

        // TODO: 405 처리
        return response;
    }

    private HttpResponse doGet(
            final HttpRequest request,
            final HttpResponse response
    ) throws IOException {
        final String body = StaticResourceLoader.load(request.getUri() + ".html");
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
        register(request);
        response.redirect("/index.html");

        return response;
    }

    private static void register(final HttpRequest request) {
        final Map<String, String> body = request.getBody();
        final String account = body.get("account");
        final String password = body.get("password");
        final String email = body.get("email");
        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);
    }
}
