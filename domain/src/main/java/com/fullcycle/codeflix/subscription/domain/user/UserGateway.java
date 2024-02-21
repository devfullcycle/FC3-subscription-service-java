package com.fullcycle.codeflix.subscription.domain.user;

import com.fullcycle.codeflix.subscription.domain.person.Email;

import java.util.Optional;

public interface UserGateway {

    UserId nextId();

    Optional<User> userOfId(UserId userId);

    boolean existsByEmail(Email email);

    User create(User user);

    User update(User user);

}
