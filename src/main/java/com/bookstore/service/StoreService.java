package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
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
    BooksService booksService;

    @Autowired
    InventoryService inventoryService;

    public synchronized BuyBookResponse buyBook(BuyBookRequest buyBookRequest) throws ValidationException {

        Book bookInDB = helper.validateRequestAndGetBook(buyBookRequest);

        Inventory bookInventory = getBookInventory(bookInDB.getId());

        if(bookInventory.getNoOfCopies() < buyBookRequest.getNoOfCopies()) return helper.generateUnavailableResponse();

        decreaseInventory(bookInventory,buyBookRequest.getNoOfCopies());

        return helper.generateSuccessReponse();
    }

    private void decreaseInventory(Inventory bookInventory,Integer noOfCopiesToBuy) {

        int finalNumberOfCopies = getNoOfCopiesAfterBuying(bookInventory,noOfCopiesToBuy);
        bookInventory.setNoOfCopies(finalNumberOfCopies);
        inventoryService.saveInventory(bookInventory);

    }

    private int getNoOfCopiesAfterBuying(Inventory bookInventory, Integer noOfCopiesToBuy) {
        if(bookInventory.getNoOfCopies()==noOfCopiesToBuy) return 1;
        return bookInventory.getNoOfCopies()-noOfCopiesToBuy;
    }

    private Inventory getBookInventory(Integer id) {
        return inventoryService.getInventoryByBookId(id);
    }
}
