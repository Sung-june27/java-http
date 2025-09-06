package org.apache.coyote.handler;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import java.io.IOException;
import java.util.Map;
import org.apache.HttpMethod;
import org.apache.HttpStatus;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.ResponseBuilder;

public class RegisterHandler implements Handler {

    private final ResponseBuilder responseBuilder;

    public RegisterHandler(ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.startsWith("/register");
    }

    @Override
    public String handle(final HttpRequest request) throws IOException {
        if (HttpMethod.GET.equals(request.getMethod())) {
            return doGet(request);
        }
        if (HttpMethod.POST.equals(request.getMethod())) {
            return doPost(request);
        }

        // TODO: 405 처리
        return "";
    }

    private String doGet(final HttpRequest request) throws IOException {
        return responseBuilder.buildStaticResponse(HttpStatus.OK, getResource(request.getUri()));
    }

    private String doPost(final HttpRequest request) throws IOException {
        final Map<String, String> body = request.getBody();
        final String account = body.get("account");
        final String password = body.get("password");
        final String email = body.get("email");
        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);

        return responseBuilder.buildStaticResponse(HttpStatus.OK, "/index.html");
    }

    private String getResource(final String resource) {
        if (!resource.endsWith(".html")) {
            return resource + ".html";
        }

        return resource;
    }
}
