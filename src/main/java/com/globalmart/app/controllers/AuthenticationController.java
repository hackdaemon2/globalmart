package com.globalmart.app.controllers;

import com.globalmart.app.models.requests.AuthRequest;
import com.globalmart.app.models.requests.RefreshTokenRequest;
import com.globalmart.app.models.responses.AuthResponse;
import com.globalmart.app.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        return ResponseEntity.status(HttpStatus.OK)
                             .body(authenticationService.getAccessToken(authRequest));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<AuthResponse> getRefreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(authenticationService.getRefreshToken(refreshTokenRequest.refreshToken()));
    }

}
