package com.bookstore.service;

import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.InventoryServiceHelper;
import com.bookstore.interfaces.InventoryDAOIFace;
import com.bookstore.requests.InventoryAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class InventoryService {

    @Autowired
    InventoryServiceHelper helper;

    @Autowired
    InventoryDAOIFace inventoryDAO;

    public void saveInventory(Inventory inventory){
        inventoryDAO.addInventory(inventory);
    }

    public Inventory getInventoryByBookId(Integer bookId){

        return inventoryDAO.getInventoryByBookId(bookId);

    }
}
