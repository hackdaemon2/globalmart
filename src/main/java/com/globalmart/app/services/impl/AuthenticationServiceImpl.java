package com.globalmart.app.services.impl;

import com.globalmart.app.config.AppConfiguration;
import com.globalmart.app.dto.AccessTokenDTO;
import com.globalmart.app.entity.AccessTokenEntity;
import com.globalmart.app.enums.ResponseCodes;
import com.globalmart.app.exception.AuthenticationException;
import com.globalmart.app.exception.ResourceNotFoundException;
import com.globalmart.app.models.requests.AuthRequest;
import com.globalmart.app.models.responses.AuthResponse;
import com.globalmart.app.repository.AccessTokenRepository;
import com.globalmart.app.security.CustomUserDetailService;
import com.globalmart.app.security.JwtUtility;
import com.globalmart.app.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final AccessTokenRepository accessTokenRepository;
    private final CustomUserDetailService userDetailService;
    private final JwtUtility jwtUtility;
    private final AppConfiguration appConfiguration;

    @Override
    public AuthResponse getRefreshToken(String refreshToken) {
        AccessTokenEntity accessTokenEntity = accessTokenRepository.findByRefreshToken(refreshToken)
                                                                   .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));

        if (accessTokenEntity.getRefreshTokenExpiration().toLocalDateTime().isBefore(LocalDateTime.now())) {
            return new AuthResponse(
                    null,
                    null,
                    ResponseCodes.INVALID_REFRESH_TOKEN.getResponseCode(),
                    ResponseCodes.INVALID_REFRESH_TOKEN.getResponseMessage());
        }

        String username = jwtUtility.extractUsername(accessTokenEntity.getAccessToken());
        UserDetails userDetail = userDetailService.loadUserByUsername(username);
        AccessTokenDTO accessTokenDTO = saveAuthenticationToken(userDetail);

        return new AuthResponse(
                accessTokenDTO.accessToken(),
                accessTokenDTO.refreshToken(),
                ResponseCodes.SUCCESS.getResponseCode(),
                ResponseCodes.SUCCESS.getResponseMessage());
    }

    @Override
    public AuthResponse getAccessToken(AuthRequest authRequest) {
        UserDetails userDetail = userDetailService.loadUserByUsername(authRequest.username());

        if (!passwordEncoder.matches(authRequest.password(), userDetail.getPassword())) {
            throw new AuthenticationException("Invalid user credentials");
        }

        AccessTokenDTO accessTokenDTO = saveAuthenticationToken(userDetail);
        ResponseCodes responseCodes = ResponseCodes.SUCCESS;

        return new AuthResponse(
                accessTokenDTO.accessToken(),
                accessTokenDTO.refreshToken(),
                responseCodes.getResponseCode(),
                responseCodes.getResponseMessage());
    }

    private AccessTokenDTO saveAuthenticationToken(UserDetails userDetail) {
        Date issuedAt = new Date();
        long expiry = Long.parseLong(appConfiguration.getJwt().getTokenExpiry());
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(expiry));

        Date refreshTokenExpiration = calculateRefreshTokenExpiration(expiration);

        String accessToken = generateAccessToken(userDetail, issuedAt, expiration);
        String refreshToken = jwtUtility.generateRefreshToken();

        AccessTokenEntity accessTokenEntity = createAccessTokenEntity(
                accessToken,
                refreshToken,
                issuedAt,
                expiration,
                refreshTokenExpiration);

        accessTokenRepository.save(accessTokenEntity);
        return new AccessTokenDTO(accessToken, refreshToken);
    }

    private Date calculateRefreshTokenExpiration(Date expiration) {
        String tokenExpiryDuration = appConfiguration.getJwt().getRefreshTokenExpiry();
        long refreshTokenExpiry = Long.parseLong(Optional.ofNullable(tokenExpiryDuration)
                                                         .orElse("55"));
        return Date.from(expiration.toInstant()
                                   .plusSeconds(TimeUnit.MINUTES.toSeconds(refreshTokenExpiry)));
    }

    private String generateAccessToken(UserDetails userDetail, Date issuedAt, Date expiration) {
        Map<String, Object> claims = Map.of(
                "issuedAt", issuedAt,
                "expiration", expiration,
                "authorities", userDetail.getAuthorities());
        return jwtUtility.generateToken(userDetail, claims);
    }

    private AccessTokenEntity createAccessTokenEntity(String accessToken, String refreshToken, Date issuedAt,
                                                      Date expiration, Date refreshTokenExpiration) {
        AccessTokenEntity accessTokenEntity = new AccessTokenEntity();
        accessTokenEntity.setAccessToken(accessToken);
        accessTokenEntity.setRefreshToken(refreshToken);
        accessTokenEntity.setExpiration(Timestamp.from(expiration.toInstant()));
        accessTokenEntity.setIssuedAt(Timestamp.from(issuedAt.toInstant()));
        accessTokenEntity.setRefreshTokenExpiration(Timestamp.from(refreshTokenExpiration.toInstant()));
        return accessTokenEntity;
    }

}
