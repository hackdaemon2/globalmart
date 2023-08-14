package com.example.demo.services.impl;

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
        var userDetail = userDetailService.loadUserByUsername(authRequest.getUsername());

        if (!passwordEncoder.matches(authRequest.getPassword(), userDetail.getPassword())) {
            throw new AuthenticationException("invalid user credentials");
        }

        var responseCodes = ResponseCodes.SUCCESS;
        var accessTokenDTO = saveAuthenticationToken(userDetail);

        return new AuthResponse(
                accessTokenDTO.getAccessToken(),
                accessTokenDTO.getRefreshToken(),
                responseCodes.getResponseCode(),
                responseCodes.getResponseMessage());
    }

    private AccessTokenDTO saveAuthenticationToken(UserDetails userDetail) {
        var issuedAt = new Date(System.currentTimeMillis());
        var expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5));
        var refreshTokenLocalDateTime = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().plusMinutes(55L);
        var refreshTokenExpiration = Date.from(refreshTokenLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        var claims = Map.of(
                "issuedAt", issuedAt,
                "expiration", expiration,
                "authorities", userDetail.getAuthorities());

        var accessToken = JwtUtility.generateToken(userDetail, claims);
        var refreshToken = JwtUtility.generateRefreshToken();

        var accessTokenEntity = new AccessTokenEntity();
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
        var accessTokenEntity = accessTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new ResourceNotFoundException("refresh token not found"));
        var refreshTokenHasExpired = accessTokenEntity.getRefreshTokenExpiration().toLocalDateTime().isAfter(LocalDateTime.now());
        var responseCodes = ResponseCodes.SUCCESS;

        if (refreshTokenHasExpired) {
            responseCodes = ResponseCodes.INVALID_REFRESH_TOKEN;
            return new AuthResponse(
                    null,
                    null,
                    responseCodes.getResponseCode(),
                    responseCodes.getResponseMessage());
        }

        var username = JwtUtility.extractUsername(accessTokenEntity.getAccessToken());
        var userDetail = userDetailService.loadUserByUsername(username);
        var accessTokenDTO = saveAuthenticationToken(userDetail);

        return new AuthResponse(
                accessTokenDTO.getAccessToken(),
                accessTokenDTO.getRefreshToken(),
                responseCodes.getResponseCode(),
                responseCodes.getResponseMessage());
    }
}
