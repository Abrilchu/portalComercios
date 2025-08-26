package com.uxdual.portal.controller;

import com.uxdual.portal.dto.LoginRequest;
import com.uxdual.portal.dto.LoginResponse;
import com.uxdual.portal.service.AuthService;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Controller("/api/auth")
@Secured(SecurityRule.IS_ANONYMOUS)
public class AuthController {
    
    private final AuthService authService;
    
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    @Post("/login")
    public LoginResponse login(@Body LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }
}
