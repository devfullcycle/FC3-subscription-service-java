package com.fullcycle.codeflix.subscription.infrastructure.users;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @GetMapping(value = "authorization-callback")
    public ResponseEntity<?> authorizationCallback(final HttpServletRequest req) {
        return ResponseEntity.ok("OK");
    }
}
