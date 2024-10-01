package com.globalmart.app.security;

import com.globalmart.app.config.AppConfiguration;
import com.globalmart.app.exception.AuthenticationException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public final class JwtUtility {

    private final AppConfiguration appConfiguration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(userDetails, claims);
    }

    private String createToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(userDetails.getUsername())
                   .setIssuedAt((Date) claims.get("issuedAt"))
                   .setExpiration((Date) claims.get("expiration"))
                   .signWith(SignatureAlgorithm.HS256, appConfiguration.getJwt().getSigningKey())
                   .compact();
    }

    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(extractUsername(token)) && !isExpiredToken(token));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                       .setSigningKey(appConfiguration.getJwt().getSigningKey())
                       .parseClaimsJws(token)
                       .getBody();
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException exception) {
            throw new AuthenticationException(exception.getMessage());
        }
    }

    private boolean isExpiredToken(String token) {
        return extractExpiration(token).before(new Date());
    }

}
