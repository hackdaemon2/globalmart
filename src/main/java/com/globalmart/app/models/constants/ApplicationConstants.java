package com.globalmart.app.models.constants;

import org.modelmapper.ModelMapper;

public abstract class ApplicationConstants {

    private ApplicationConstants() {
    }

    public static final int TOKEN_BEGIN_INDEX = 7;

    public static final String ORDER_NOT_FOUND = "order not found";
    public static final String USER_ACCOUNT_NOT_FOUND = "user account not found";
    public static final String USER_ACCOUNT_EXISTS = "user account already exists";
    public static final String PRODUCT_NOT_FOUND = "product not found";
    public static final String PRODUCT_EXISTS = "product already exists";
    public static final String ACCESS_DENIED = "Access denied: You do not have permission to access this resource.";

    public static final String ADMIN_ACCESS_EXECUTION = """
            execution(* com.globalmart.app.controllers.ProductController.getAllProduct(..)) || \
            execution(* com.globalmart.app.controllers.ProductController.deleteUser(..)) || \
            execution(* com.globalmart.app.controllers.OrderController.getAllOrders(..)) || \
            execution(* com.globalmart.app.controllers.OrderController.deleteOrder(..)) || \
            execution(* com.globalmart.app.controllers.UserController.deleteUser(..)) || \
            execution(* com.globalmart.app.controllers.UserController.getAllUser(..))""";

    public static final ModelMapper MODEL_MAPPER = new ModelMapper();

}
