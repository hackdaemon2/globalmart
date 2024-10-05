package com.globalmart.app.services.impl;

import com.globalmart.app.dto.ProductDTO;
import com.globalmart.app.dto.ProductFilterDTO;
import com.globalmart.app.entity.ProductEntity;
import com.globalmart.app.entity.UserEntity;
import com.globalmart.app.enums.ResponseCodes;
import com.globalmart.app.exception.ResourceConflictException;
import com.globalmart.app.exception.ResourceNotFoundException;
import com.globalmart.app.models.requests.ProductRequest;
import com.globalmart.app.models.responses.AppResponse;
import com.globalmart.app.repository.ProductRepository;
import com.globalmart.app.repository.ProductSpecification;
import com.globalmart.app.repository.UserRepository;
import com.globalmart.app.services.ProductService;
import com.globalmart.app.util.GenericUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.globalmart.app.models.constants.ApplicationConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public AppResponse<ProductDTO> createProduct(ProductRequest productRequest) {
        productRepository.findByProductCode(productRequest.productCode())
                         .ifPresent(existingProduct -> {
                             throw new ResourceConflictException(PRODUCT_EXISTS);
                         });
        ProductEntity productEntity = GenericUtil.mapToEntity(productRequest, ProductEntity.class, (request, entity) -> {
            entity.setSeller(getSeller(request.userID()));
            entity.setOrders(Collections.emptySet());
        });
        ProductEntity savedProduct = productRepository.save(productEntity);
        ProductDTO productDTO = GenericUtil.mapToDTO(savedProduct, ProductDTO.class);
        return GenericUtil.formulateSuccessAppResponse(productDTO);

    }

    @Override
    public AppResponse<ProductDTO> updateProduct(String id, ProductRequest productRequest) {
        return productRepository.findByGuidAndDeletedIsFalse(id)
                                .map(existingProduct -> this.updateProductEntity(existingProduct, productRequest))
                                .map(user -> GenericUtil.saveAndMapToDTO(
                                        user,
                                        GenericUtil.toDTOFunction(ProductDTO.class),
                                        productRepository))
                                .map(GenericUtil::formulateSuccessAppResponse)
                                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public AppResponse<ProductDTO> getProduct(String id) {
        return productRepository.findByGuidAndDeletedIsFalse(id)
                                .map(existingProduct -> GenericUtil.mapToDTO(existingProduct, ProductDTO.class))
                                .map(GenericUtil::formulateSuccessAppResponse)
                                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    @Override
    public AppResponse<List<ProductDTO>> getAllProduct(long page, long size, String sortOrder,
                                                       ProductFilterDTO productFilterDTO) {
        Pageable pageable = PageRequest.of((int) page,
                (int) size,
                Sort.Direction.fromString(sortOrder.toUpperCase()),
                DATE_CREATED);
        Page<ProductEntity> productPage = productRepository.findAll(ProductSpecification.withFilters(productFilterDTO), pageable);
        List<ProductDTO> productDTOList = productPage.stream()
                                                     .map(product -> GenericUtil.mapToDTO(product, ProductDTO.class))
                                                     .collect(Collectors.toList());
        return new AppResponse<>(
                (int) page,
                (int) size,
                (int) productPage.getTotalElements(),
                productDTOList,
                ResponseCodes.SUCCESS.getResponseCode(),
                ResponseCodes.SUCCESS.getResponseMessage());
    }

    @Override
    public void deleteProduct(String id) {
        productRepository.findByGuidAndDeletedIsFalse(id)
                         .map(productEntity -> {
                             productEntity.setDeleted(true);
                             return productRepository.save(productEntity);
                         })
                         .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
    }

    private ProductEntity updateProductEntity(ProductEntity existingProductEntity, ProductRequest productRequest) {
        MODEL_MAPPER.map(productRequest, existingProductEntity);
        existingProductEntity.setSeller(getSeller(productRequest.userID()));
        return existingProductEntity;
    }

    private UserEntity getSeller(String userId) {
        return userRepository.findByGuidAndDeletedIsFalse(userId)
                             .orElseThrow(() -> new ResourceNotFoundException(USER_ACCOUNT_NOT_FOUND));
    }

}
