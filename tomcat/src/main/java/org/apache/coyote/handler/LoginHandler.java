package org.apache.coyote.handler;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import java.io.IOException;
import java.util.Map;
import org.apache.HttpMethod;
import org.apache.HttpStatus;
import org.apache.coyote.HttpRequest;
import org.apache.coyote.ResponseBuilder;

public class LoginHandler implements Handler {

    private final ResponseBuilder responseBuilder;

    public LoginHandler(final ResponseBuilder responseBuilder) {
        this.responseBuilder = responseBuilder;
    }

    @Override
    public boolean canHandle(final String requestUri) {
        return requestUri.startsWith("/login");
    }

    @Override
    public String handle(final HttpRequest request) throws IOException {
        if (HttpMethod.GET.equals(request.getMethod())) {
            return responseBuilder.buildStaticResponse(HttpStatus.OK, getResource(request.getUri()));
        }
        if (HttpMethod.POST.equals(request.getMethod())) {
            return login(request);
        }

        // TODO: 405 처리
        return "";
    }

    private String getResource(final String resource) {
        if (!resource.endsWith(".html")) {
            return resource + ".html";
        }

        return resource;
    }

    // TODO: Service 분리 고려해보기 (Controller 도입 후)
    private String login(final HttpRequest request) throws IOException {
        final Map<String, String> params = request.getBody();
        try {
            final User user = InMemoryUserRepository.findByAccount(params.get("account"))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            validatePassword(user, params.get("password"));
            System.out.println("user: " + user);
        } catch (IllegalArgumentException e) {
            return responseBuilder.buildStaticResponse(HttpStatus.UNAUTHORIZED, "/401.html");
        }

        return responseBuilder.buildStaticResponse(HttpStatus.FOUND, "/index.html");
    }

    private void validatePassword(
            final User user,
            final String password
    ) {
        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
