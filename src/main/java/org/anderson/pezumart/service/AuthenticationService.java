package org.anderson.pezumart.service;

import org.anderson.pezumart.controllers.request.LoginRequest;
import org.anderson.pezumart.controllers.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);
    boolean validateToken(String jwt);
}
