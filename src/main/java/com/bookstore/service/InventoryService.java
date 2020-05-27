package com.bookstore.service;

import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.DBException;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.interfaces.InventoryDAOIFace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    InventoryDAOIFace inventoryDAO;

    public void saveInventory(Inventory inventory) throws InternalServerException {
        try{
            inventoryDAO.saveInventory(inventory);
        }
        catch(DBException dbEx){
            log.error("DBException while trying to save inventory : " + inventory,dbEx);
            throw new InternalServerException();
        }
    }

    public Inventory getInventoryByBookId(Integer bookId) throws InternalServerException{

        try{
            return inventoryDAO.getInventoryByBookId(bookId);
        }
        catch(DBException dbEx){
            log.error("Exception while trying to get inventory by bookId : " + bookId,dbEx);
            throw new InternalServerException();
        }

    }

    public void deleteInventoryOfBook(Integer id) throws InternalServerException {

        Inventory inventory = getInventoryByBookId(id);
        if(inventory==null) return;
        try{
            inventoryDAO.deleteInventory(inventory);
        }
        catch(DBException dBEx){
            log.error("Exception while trying to delete inventory :" + inventory,dBEx);
            throw new InternalServerException();
        }
    }

    public Inventory getBookInventoryForBuying(Integer bookId) throws InternalServerException {
        try{
            return inventoryDAO.getInventoryByBookIdWithLock(bookId);
        }
        catch(DBException dBEx){
            log.error("Exception while trying to get inventory for bookId : " + bookId,dBEx);
            throw new InternalServerException();
        }
    }
}
