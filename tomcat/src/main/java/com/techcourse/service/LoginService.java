package com.techcourse.service;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.dto.LoginRequest;
import com.techcourse.model.User;

public class LoginService {

    public User login(final LoginRequest request) {
        final User user = InMemoryUserRepository.findByAccount(request.account())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        validatePassword(user, request.password());
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
