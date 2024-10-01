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

@Aspect
@Component
@RequiredArgsConstructor
public class OrderControllerSecurityAspect {

    private final UserSecurityService userSecurityService;

    @Before("execution(* com.globalmart.app.controllers.OrderController.*(.., String)) && args(id,..)")
    public void checkOrderOwner(JoinPoint joinPoint, String id) {
        String currentUsername = ((User) SecurityContextHolder.getContext()
                                                              .getAuthentication()
                                                              .getPrincipal()).getUsername();
        if (!userSecurityService.isAdmin(currentUsername) && !userSecurityService.isOrderOwner(id)) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
    }

}

