package com.example.demo.security;

import com.example.demo.exception.AuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final ApplicationUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if ("options".equals(request.getMethod().toLowerCase())) {
                response.setHeader("", "");
                filterChain.doFilter(request, response);
                return;
            }

            var authorizationHeader = request.getHeader("Authorization");

            if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
                throw new AuthenticationException("authorization header with bearer token is required");
            }

            var jwtToken = authorizationHeader.substring(7);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                var userDetails = userDetailService.loadUserByUsername(JwtUtility.extractUsername(jwtToken));
                var isValidToken = JwtUtility.isValidToken(jwtToken, userDetails);

                if (isValidToken) {
                    var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            var errorResponse = Map.of("responseCode", "01", "responseMessage", exception.getMessage());
            response.getWriter().write(new JSONObject(errorResponse).toString());
        }
    }
}
