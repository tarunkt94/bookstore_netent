package com.bookstore.daos;

import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.DBException;
import com.bookstore.interfaces.InventoryDAOIFace;
import com.bookstore.repos.InventoryRepo;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.lock.PessimisticEntityLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.LockTimeoutException;
import javax.persistence.PessimisticLockException;
import java.sql.SQLException;

@Service
@Slf4j
public class InventoryDAO implements InventoryDAOIFace {

    @Autowired
    InventoryRepo inventoryRepo;

    @Override
    public void saveInventory(Inventory inventory) throws DBException {
        try{
            inventoryRepo.save(inventory);
        }
        catch(Exception ex){
            log.error("Exception while trying to add inventory : " + inventory,ex);
            throw new DBException();
        }

    }

    @Override
    public Inventory getInventoryByBookId(Integer bookId) throws DBException{
        try{
            return inventoryRepo.findByBookId(bookId);
        }
        catch(Exception ex){
            log.error("Exception while trying to get inventory by bookId : " + bookId,ex);
            throw new DBException();
        }
    }

    @Override
    public Inventory getInventoryByBookIdWithLock(Integer bookId) throws DBException {
        try{
            return inventoryRepo.findByBookIdWithLock(bookId);
        }
        catch(PessimisticLockException | LockTimeoutException ex){
            log.error("Exception while trying to obtain lock on the required row, bookId : " + bookId,ex);
            throw new DBException();
        }
        catch(Exception ex){
            log.error("Exception while trying to get inventory for bookId : " + bookId,ex);
            throw new DBException();
        }
    }

    public void deleteInventory(Inventory inventory) throws DBException{

        try{
            inventoryRepo.delete(inventory);
        }
        catch(Exception ex){
            log.error("exception while deleting inventory");
            throw new DBException();
        }
    }
}
