package com.globalmart.app.services;

import com.globalmart.app.dto.OrderDTO;
import com.globalmart.app.dto.OrderFilterDTO;
import com.globalmart.app.models.requests.OrderRequest;
import com.globalmart.app.models.responses.AppResponse;

import java.util.List;

public interface OrderService {

    AppResponse<OrderDTO> createOrder(OrderRequest order);

    AppResponse<List<OrderDTO>> getAllOrders(long page, long size, String sortOrder, OrderFilterDTO filter);

    AppResponse<OrderDTO> updateOrder(String id, OrderRequest order);

    void deleteOrder(String id);

    AppResponse<OrderDTO> getOrder(String id);

}
