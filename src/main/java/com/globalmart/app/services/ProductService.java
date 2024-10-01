package com.globalmart.app.services;

import com.globalmart.app.dto.ProductDTO;
import com.globalmart.app.dto.ProductFilterDTO;
import com.globalmart.app.models.requests.ProductRequest;
import com.globalmart.app.models.responses.AppResponse;

import java.util.List;

public interface ProductService {

    AppResponse<ProductDTO> createProduct(ProductRequest productRequest);

    AppResponse<ProductDTO> updateProduct(String productCode, ProductRequest productRequest);

    AppResponse<ProductDTO> getProduct(String productCode);

    AppResponse<List<ProductDTO>> getAllProduct(long page, long size, String sortOrder, ProductFilterDTO filter);

    void deleteProduct(String id);

}
