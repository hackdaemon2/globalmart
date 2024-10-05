package com.globalmart.app.services.impl;

import com.globalmart.app.repository.OrderRepository;
import com.globalmart.app.repository.ProductRepository;
import com.globalmart.app.repository.UserRepository;
import com.globalmart.app.services.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.globalmart.app.models.constants.ApplicationConstants.ADMIN;

@Service
@RequiredArgsConstructor
public class UserSecurityServiceImpl implements UserSecurityService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public boolean isProductOwner(String productId) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .filter(Authentication::isAuthenticated)
                       .map(Authentication::getPrincipal)
                       .filter(User.class::isInstance)
                       .map(User.class::cast)
                       .map(User::getUsername)
                       .flatMap(userRepository::findByUsername)
                       .flatMap(user -> productRepository.findByGuid(productId)
                                                         .filter(product -> product.getSeller()
                                                                                   .getGuid()
                                                                                   .equals(user.getGuid())))
                       .isPresent();
    }

    @Override
    public boolean isOrderOwner(String orderId) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .filter(Authentication::isAuthenticated)
                       .map(Authentication::getPrincipal)
                       .filter(User.class::isInstance)
                       .map(User.class::cast)
                       .map(User::getUsername)
                       .flatMap(userRepository::findByUsername)
                       .flatMap(user -> orderRepository.findByGuid(orderId)
                                                       .filter(order -> order.getUser()
                                                                             .getGuid()
                                                                             .equals(user.getGuid())))
                       .isPresent();
    }

    @Override
    public boolean isAdmin(String username) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .filter(Authentication::isAuthenticated)
                       .map(Authentication::getPrincipal)
                       .filter(User.class::isInstance)
                       .map(User.class::cast)
                       .map(User::getUsername)
                       .flatMap(userRepository::findByUsername)
                       .map(user -> user.getRoles()
                                        .stream()
                                        .anyMatch(roles -> roles.getName()
                                                                .equals(ADMIN)))
                       .isPresent();
    }

    @Override
    public boolean isUserOwner(String username, String userId) {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                       .filter(Authentication::isAuthenticated)
                       .map(Authentication::getPrincipal)
                       .filter(User.class::isInstance)
                       .map(User.class::cast)
                       .map(User::getUsername)
                       .flatMap(userRepository::findByUsername)
                       .map(user -> user.getGuid().equals(userId))
                       .isPresent();
    }

}
