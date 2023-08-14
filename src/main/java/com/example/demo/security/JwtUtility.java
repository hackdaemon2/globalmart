package com.example.demo.security;

import com.example.demo.exception.AuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

final public class JwtUtility {

    private static final String signingKey = "eee";

    private JwtUtility() {
        throw new IllegalStateException();
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static String generateToken(UserDetails userDetails, Map<String, Object> claims) {
        return createToken(userDetails, claims);
    }

    private static String createToken(UserDetails userDetails, Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt((Date) claims.get("issuedAt"))
                .setExpiration((Date) claims.get("expiration"))
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .compact();
    }

    public static String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    static boolean isValidToken(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(extractUsername(token)) && !isExpiredToken(token));
    }

    private static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException exception) {
            throw new AuthenticationException(exception.getMessage());
        }
    }

    private static Boolean isExpiredToken(String token) {
        return extractExpiration(token).before(new Date());
    }
}
