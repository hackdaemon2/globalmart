package com.globalmart.app.filter;

import com.globalmart.app.enums.ResponseCodes;
import com.globalmart.app.exception.AuthenticationException;
import com.globalmart.app.models.responses.ErrorResponse;
import com.globalmart.app.security.CustomUserDetailService;
import com.globalmart.app.security.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.globalmart.app.models.constants.ApplicationConstants.TOKEN_BEGIN_INDEX;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod()) || request.getRequestURL().toString().contains("token")) {
                final String allowedHeaders = "Authorization, Content-Type, Content-Length, Accept";
                response.addHeader("Access-Control-Allow-Origin", "http://localhost/");
                response.addHeader("Access-Control-Allow-Credentials", "true");
                response.addHeader("Access-Control-Max-Age", "3600");
                response.addHeader("Access-Control-Allow-Headers", allowedHeaders);
                response.addHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST, PUT");
                filterChain.doFilter(request, response);
                return;
            }

            String authorizationHeader = request.getHeader("Authorization");

            if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer")) {
                throw new AuthenticationException("authorization header with bearer token is required");
            }

            String jwtToken = authorizationHeader.substring(TOKEN_BEGIN_INDEX);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailService.loadUserByUsername(jwtUtility.extractUsername(jwtToken));
                boolean isValidToken = jwtUtility.isValidToken(jwtToken, userDetails);
                if (isValidToken) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            userDetails.getPassword(),
                            userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext()
                                         .setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (AuthenticationException exception) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter()
                    .write(new ErrorResponse(ResponseCodes.FAILURE.getResponseCode(), exception.getMessage()).toString());
        }
    }

}
