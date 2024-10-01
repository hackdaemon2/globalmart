package com.globalmart.app.services;

import com.globalmart.app.models.requests.AuthRequest;
import com.globalmart.app.models.responses.AuthResponse;

public interface AuthenticationService {

    AuthResponse getAccessToken(AuthRequest authRequest);

    AuthResponse getRefreshToken(String accessToken);
}
