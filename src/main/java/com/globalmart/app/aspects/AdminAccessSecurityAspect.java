package com.globalmart.app.aspects;

import com.globalmart.app.exception.AccessDeniedException;
import com.globalmart.app.services.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import static com.globalmart.app.models.constants.ApplicationConstants.ACCESS_DENIED;
import static com.globalmart.app.models.constants.ApplicationConstants.ADMIN_ACCESS_EXECUTION;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminAccessSecurityAspect {

    private final UserSecurityService userSecurityService;

    @Before(ADMIN_ACCESS_EXECUTION)
    public void checkAdminAccess(JoinPoint joinPoint) {
        String currentUsername = ((User) SecurityContextHolder.getContext()
                                                              .getAuthentication()
                                                              .getPrincipal()).getUsername();
        if (!userSecurityService.isAdmin(currentUsername)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }

}
