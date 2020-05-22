package com.bookstore.controllers;

import com.bookstore.requests.InventoryAddRequest;
import com.bookstore.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    @Autowired
    InventoryService inventoryService;


    public void addInventory(InventoryAddRequest inventoryAddRequest){
        inventoryService.addInventory(inventoryAddRequest);
    }
}
