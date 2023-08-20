package com.example.demo.services.impl;

import com.example.demo.configurations.ApplicationContextProvider;
import com.example.demo.enums.ResponseCodes;
import com.example.demo.exception.AuthenticationException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.models.dto.AccessTokenDTO;
import com.example.demo.models.entities.AccessTokenEntity;
import com.example.demo.models.requests.AuthRequest;
import com.example.demo.models.responses.AuthResponse;
import com.example.demo.repository.AccessTokenRepository;
import com.example.demo.security.ApplicationUserDetailService;
import com.example.demo.security.JwtUtility;
import com.example.demo.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AccessTokenRepository accessTokenRepository;
    private final ApplicationUserDetailService userDetailService;

    @Override
    public AuthResponse getAccessToken(AuthRequest authRequest) {
        UserDetails userDetail = userDetailService.loadUserByUsername(authRequest.getUsername());

        if (!passwordEncoder.matches(authRequest.getPassword(), userDetail.getPassword())) {
            throw new AuthenticationException("invalid user credentials");
        }

        ResponseCodes responseCodes = ResponseCodes.SUCCESS;
        AccessTokenDTO accessTokenDTO = saveAuthenticationToken(userDetail);

        return new AuthResponse(
                accessTokenDTO.getAccessToken(),
                accessTokenDTO.getRefreshToken(),
                responseCodes.getResponseCode(),
                responseCodes.getResponseMessage());
    }

    private AccessTokenDTO saveAuthenticationToken(UserDetails userDetail) {
        Date issuedAt = new Date(System.currentTimeMillis());
        String tokenExpiry = ApplicationContextProvider.getEnvironment().getProperty("token.expiry");
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Long.parseLong(tokenExpiry != null ? tokenExpiry : "5")));
        String refreshTokenExpiry = ApplicationContextProvider.getEnvironment().getProperty("refresh.token.expiry");

        LocalDateTime refreshTokenLocalDateTime = expiration
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .plusMinutes(Long.parseLong(refreshTokenExpiry != null ? refreshTokenExpiry : "55L"));

        Date refreshTokenExpiration = Date.from(refreshTokenLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        Map<String, Object> claims = Map.of(
                "issuedAt", issuedAt,
                "expiration", expiration,
                "authorities", userDetail.getAuthorities());

        String accessToken = JwtUtility.generateToken(userDetail, claims);
        String refreshToken = JwtUtility.generateRefreshToken();

        AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
        accessTokenEntity.setAccessToken(accessToken);
        accessTokenEntity.setRefreshToken(refreshToken);
        accessTokenEntity.setExpiration(Timestamp.from(expiration.toInstant()));
        accessTokenEntity.setIssuedAt(Timestamp.from(issuedAt.toInstant()));
        accessTokenEntity.setRefreshTokenExpiration(Timestamp.from(refreshTokenExpiration.toInstant()));
        accessTokenRepository.save(accessTokenEntity);

        return new AccessTokenDTO(accessToken, refreshToken);
    }

    @Override
    public AuthResponse getRefreshToken(String refreshToken) {
        AccessTokenEntity accessTokenEntity = accessTokenRepository
                .findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("refresh token not found"));

        boolean refreshTokenHasExpired = accessTokenEntity
                .getRefreshTokenExpiration()
                .toLocalDateTime()
                .isAfter(LocalDateTime.now());

        ResponseCodes responseCodes = ResponseCodes.SUCCESS;

        if (refreshTokenHasExpired) {
            responseCodes = ResponseCodes.INVALID_REFRESH_TOKEN;
            return new AuthResponse(
                    null,
                    null,
                    responseCodes.getResponseCode(),
                    responseCodes.getResponseMessage());
        }

        String username = JwtUtility.extractUsername(accessTokenEntity.getAccessToken());
        UserDetails userDetail = userDetailService.loadUserByUsername(username);
        AccessTokenDTO accessTokenDTO = saveAuthenticationToken(userDetail);

        return new AuthResponse(
                accessTokenDTO.getAccessToken(),
                accessTokenDTO.getRefreshToken(),
                responseCodes.getResponseCode(),
                responseCodes.getResponseMessage());
    }
}
