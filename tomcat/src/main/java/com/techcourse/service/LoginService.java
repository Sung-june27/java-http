package com.techcourse.service;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.model.User;
import java.util.Map;
import org.apache.coyote.http11.request.HttpRequest;

public class LoginService {

    public User login(final HttpRequest request) {
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
}
