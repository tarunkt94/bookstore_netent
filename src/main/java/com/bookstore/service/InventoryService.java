package com.bookstore.service;

import com.bookstore.helpers.InventoryServiceHelper;
import com.bookstore.requests.InventoryAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @Autowired
    InventoryServiceHelper helper;

    public void addInventory(InventoryAddRequest inventoryAddRequest) {
        helper.validateRequest(inventoryAddRequest);
    }
}
