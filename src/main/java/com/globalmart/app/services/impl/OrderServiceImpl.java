package com.globalmart.app.services.impl;

import com.globalmart.app.dto.OrderDTO;
import com.globalmart.app.dto.OrderFilterDTO;
import com.globalmart.app.entity.OrderEntity;
import com.globalmart.app.entity.ProductEntity;
import com.globalmart.app.entity.UserEntity;
import com.globalmart.app.enums.ResponseCodes;
import com.globalmart.app.enums.SortOrder;
import com.globalmart.app.exception.BadRequestException;
import com.globalmart.app.exception.ResourceNotFoundException;
import com.globalmart.app.models.requests.OrderRequest;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.repository.OrderRepository;
import com.globalmart.app.repository.OrderSpecification;
import com.globalmart.app.repository.ProductRepository;
import com.globalmart.app.repository.UserRepository;
import com.globalmart.app.services.OrderService;
import com.globalmart.app.util.GenericUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.globalmart.app.models.constants.ApplicationConstants.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public AppResponse<OrderDTO> createOrder(OrderRequest orderRequest) {
        if (orderRepository.existsByOrderReference(orderRequest.orderReference())) {
            throw new ResourceNotFoundException("order with reference %s already exists".formatted(orderRequest.orderReference()));
        }
        UserEntity user = userRepository.findByGuidAndDeletedIsFalse(orderRequest.userID())
                                        .orElseThrow(() -> new ResourceNotFoundException(USER_ACCOUNT_EXISTS));
        Set<ProductEntity> products = orderRequest.products()
                                                  .stream()
                                                  .map(p -> productRepository.findByGuidAndDeletedIsFalse(p.guid())
                                                                             .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND)))
                                                  .collect(Collectors.toSet());
        // validate status

        BigDecimal totalPrice = getTotalPrice(orderRequest);
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(orderRequest.status());
        orderEntity.setTotalAmount(totalPrice);
        orderEntity.setCurrency(orderRequest.currency());
        orderEntity.setOrderReference(orderRequest.orderReference());
        orderEntity.setUser(user);
        orderEntity.setProducts(products);
        OrderEntity savedOrder = orderRepository.save(orderEntity);
        OrderDTO orderDTO = GenericUtil.mapToDTO(savedOrder, OrderDTO.class);
        return GenericUtil.formulateSuccessAppResponse(orderDTO);
    }

    @Override
    public AppResponse<List<OrderDTO>> getAllOrders(long page, long size, SortOrder sortOrder,
                                                    OrderFilterDTO orderFilterDTO) {
        Pageable pageable = PageRequest.of(
                (int) page,
                (int) size,
                Sort.Direction.fromString(sortOrder.name()),
                DATE_CREATED);
        Page<OrderEntity> orderPage = orderRepository.findAll(OrderSpecification.withFilters(orderFilterDTO), pageable);
        List<OrderDTO> orderDTOList = orderPage.stream()
                                               .map(p -> GenericUtil.mapToDTO(p, OrderDTO.class))
                                               .collect(Collectors.toList());
        return new AppResponse<>(
                (int) page,
                (int) size,
                (int) orderPage.getTotalElements(),
                orderDTOList,
                ResponseCodes.SUCCESS.getResponseCode(),
                ResponseCodes.SUCCESS.getResponseMessage());
    }

    @Override
    public AppResponse<OrderDTO> updateOrder(String id, OrderRequest orderRequest) {
        return orderRepository.findByGuidAndDeletedIsFalse(id)
                              .map(existingOrder -> this.updateOrderEntity(existingOrder, orderRequest))
                              .map(u -> GenericUtil.saveAndMapToDTO(
                                      u,
                                      GenericUtil.toDTOFunction(OrderDTO.class),
                                      orderRepository))
                              .map(GenericUtil::formulateSuccessAppResponse)
                              .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    @Override
    public void deleteOrder(String id) {
        orderRepository.findByGuidAndDeletedIsFalse(id)
                       .map(orderEntity -> {
                           orderEntity.setDeleted(true);
                           return orderRepository.save(orderEntity);
                       })
                       .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    @Override
    public AppResponse<OrderDTO> getOrder(String id) {
        return orderRepository.findByGuidAndDeletedIsFalse(id)
                              .map(u -> GenericUtil.mapToDTO(u, OrderDTO.class))
                              .map(GenericUtil::formulateSuccessAppResponse)
                              .orElseThrow(() -> new ResourceNotFoundException(ORDER_NOT_FOUND));
    }

    private OrderEntity updateOrderEntity(OrderEntity existingOrderEntity, OrderRequest orderRequest) {
        MODEL_MAPPER.map(orderRequest, existingOrderEntity);
        if (!existingOrderEntity.getOrderReference().equals(orderRequest.orderReference())) {
            throw new BadRequestException("orderReference mismatch");
        }
        BigDecimal totalPrice = getTotalPrice(orderRequest);
        existingOrderEntity.setTotalAmount(totalPrice);
        return existingOrderEntity;
    }

    private static BigDecimal getTotalPrice(OrderRequest orderRequest) {
        double totalPrice = orderRequest.products()
                                        .stream()
                                        .mapToDouble(productDTO -> productDTO.price().doubleValue())
                                        .sum();
        return BigDecimal.valueOf(totalPrice);
    }

}
