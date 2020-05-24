package com.bookstore.interfaces;

import com.bookstore.entity.Inventory;

public interface InventoryDAOIFace {

    public void addInventory(Inventory inventory);

    public void updateInventory(Inventory inventory);

    public Inventory getInventoryById(Integer id);

    public Inventory getInventoryByBookId(Integer bookId);

}
