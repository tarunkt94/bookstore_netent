package com.bookstore.interfaces;

import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.DBException;

public interface InventoryDAOIFace {

    public void saveInventory(Inventory inventory) throws DBException;

    public Inventory getInventoryByBookId(Integer bookId) throws DBException;

    public Inventory getInventoryByBookIdWithLock(Integer bookId) throws DBException;

    public void deleteInventory(Inventory inventory) throws DBException;

}
