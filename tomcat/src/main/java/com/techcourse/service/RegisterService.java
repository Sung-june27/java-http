package com.techcourse.service;

import com.techcourse.db.InMemoryUserRepository;
import com.techcourse.dto.RegisterRequest;
import com.techcourse.model.User;

public class RegisterService {

    public void register(final RegisterRequest request) {
        final User user = new User(request.account(), request.password(), request.email());
        InMemoryUserRepository.save(user);
    }
}
