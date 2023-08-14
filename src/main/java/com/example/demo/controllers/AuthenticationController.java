package com.example.demo.controllers;

import com.example.demo.models.requests.AuthRequest;
import com.example.demo.models.requests.RefreshTokenRequest;
import com.example.demo.models.responses.AuthResponse;
import com.example.demo.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getAccessToken(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authenticationService.getAccessToken(authRequest));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthResponse> getRefreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.ok(authenticationService.getRefreshToken(refreshTokenRequest.getRefreshToken()));
    }
}
