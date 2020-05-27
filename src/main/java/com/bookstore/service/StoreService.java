package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.StoreServiceHelper;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    @Autowired
    StoreServiceHelper helper;

    @Autowired
    InventoryService inventoryService;

    public synchronized BuyBookResponse buyBook(BuyBookRequest buyBookRequest) throws ValidationException, InternalServerException {

        Book bookInDB = helper.validateRequestAndGetBook(buyBookRequest);

        Inventory bookInventory = getBookInventoryForBuying(bookInDB.getId());

        if(bookInventory.getNoOfCopies() < buyBookRequest.getNoOfCopies()) return helper.generateUnavailableResponse();

        decreaseInventory(bookInventory,buyBookRequest.getNoOfCopies());

        return helper.generateSuccessReponse();
    }

    private Inventory getBookInventoryForBuying(Integer id) throws InternalServerException{
        return inventoryService.getBookInventoryForBuying(id);
    }

    private void decreaseInventory(Inventory bookInventory,Integer noOfCopiesToBuy) throws InternalServerException {

        int finalNumberOfCopies = getNoOfCopiesAfterBuying(bookInventory,noOfCopiesToBuy);
        bookInventory.setNoOfCopies(finalNumberOfCopies);
        inventoryService.saveInventory(bookInventory);

    }

    private int getNoOfCopiesAfterBuying(Inventory bookInventory, Integer noOfCopiesToBuy) {
        if(bookInventory.getNoOfCopies()==noOfCopiesToBuy) return 1;
        return bookInventory.getNoOfCopies()-noOfCopiesToBuy;
    }
}
