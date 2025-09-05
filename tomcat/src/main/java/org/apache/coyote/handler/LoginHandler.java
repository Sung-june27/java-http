package org.apache.coyote.handler;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.HttpStatus;
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
    public String handle(final String requestUri) throws IOException {
        final String queryString = getQueryString(requestUri);
        if (queryString.isEmpty()) {
            return responseBuilder.buildStaticResponse(HttpStatus.OK, getResource(requestUri));
        }

        return login(queryString);
    }

    private String getQueryString(final String requestUri) {
        final int index = requestUri.indexOf('?');
        if (index == -1) {
            return "";
        }

        return requestUri.substring(index + 1);
    }

    private String getResource(final String resource) {
        if (!resource.endsWith(".html")) {
            return resource + ".html";
        }

        return resource;
    }

    // TODO: Service 분리 고려해보기 (Controller 도입 후)
    private String login(final String queryString) throws IOException {
        final Map<String, String> params = getParams(queryString);
        try {
            final User user = InMemoryUserRepository.findByAccount(params.get("account"))
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            validateLogin(user, params.get("password"));
            System.out.println("user: " + user);
        } catch (IllegalArgumentException e) {
            return responseBuilder.buildStaticResponse(HttpStatus.UNAUTHORIZED, "/401.html");
        }

        return responseBuilder.buildStaticResponse(HttpStatus.FOUND, "/index.html");
    }

    private Map<String, String> getParams(final String queryString) {
        final Map<String, String> params = new HashMap<>();
        final String[] queries = queryString.split("&");
        for (String query : queries) {
            final String[] pair = query.split("=");
            params.put(pair[0], pair[1]);
        }

        return params;
    }

    private void validateLogin(
            final User user,
            final String password
    ) {
        if (!user.checkPassword(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
