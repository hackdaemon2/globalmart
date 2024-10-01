package com.globalmart.app.services;

import com.globalmart.app.dto.OrderFilterDTO;
import com.globalmart.app.models.responses.AppResponse;

import java.util.List;

public interface InventoryService {

    AppResponse<InventoryDTO> createInventory(InventoryRequest inventoryRequest);

    AppResponse<InventoryDTO> updateInventory(String id, InventoryRequest inventoryRequest);

    void deleteInventory(String id);

    AppResponse<InventoryDTO> getInventory(String id);

    AppResponse<List<InventoryDTO>> getAllInventories(long page, long size, String sortOrder, OrderFilterDTO filter);

}
