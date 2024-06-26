package org.anderson.pezumart.controllers.auth;

import jakarta.validation.Valid;
import org.anderson.pezumart.controllers.request.LoginRequest;
import org.anderson.pezumart.controllers.response.LoginResponse;
import org.anderson.pezumart.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {

        LoginResponse loginResponse = authenticationService.login(loginRequest);

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateJwt(@RequestParam String token) {
        boolean isTokenValid = authenticationService.validateToken(token);

        return ResponseEntity.ok(isTokenValid);
    }
}
