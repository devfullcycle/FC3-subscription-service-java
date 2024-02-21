package com.fullcycle.codeflix.subscription.domain.user;

import java.util.Optional;

public interface UserGateway {

    Optional<User> userOfId(UserId userId);

    User save(User user);

}
