package com.bookstore.daos;

import com.bookstore.entity.Inventory;
import com.bookstore.interfaces.InventoryDAOIFace;
import com.bookstore.repos.InventoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryDAO implements InventoryDAOIFace {

    @Autowired
    InventoryRepo inventoryRepo;

    @Override
    public void addInventory(Inventory inventory) {
        inventoryRepo.save(inventory);
    }

    @Override
    public void updateInventory(Inventory inventory) {
        inventoryRepo.save(inventory);
    }

    @Override
    public Inventory getInventoryById(Integer id) {
        return null;
    }

    @Override
    public Inventory getInventoryByBookId(Integer bookId) {
        return inventoryRepo.findByBookId(bookId);
    }
}
