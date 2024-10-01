package com.globalmart.app.services;

public interface UserSecurityService {

    boolean isProductOwner(String productId);

    boolean isOrderOwner(String orderId);

    boolean isAdmin(String username);

    boolean isUserOwner(String username, String userId);
    
}
