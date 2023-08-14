package com.example.demo.services;

import com.example.demo.models.requests.AuthRequest;
import com.example.demo.models.responses.AuthResponse;

public interface AuthenticationService {

    AuthResponse getAccessToken(AuthRequest authRequest);

    AuthResponse getRefreshToken(String accessToken);
}
