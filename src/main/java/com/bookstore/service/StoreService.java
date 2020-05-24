package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.StoreServiceHelper;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import org.hibernate.annotations.Synchronize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class StoreService {

    @Autowired
    StoreServiceHelper helper;

    @Autowired
    BooksService booksService;

    @Autowired
    InventoryService inventoryService;

    @Transactional
    public synchronized BuyBookResponse buyBook(BuyBookRequest buyBookRequest) throws ValidationException {

        Book bookInDB = helper.validateRequestAndGetBook(buyBookRequest);

        Inventory bookInventory = getBookInventory(bookInDB.getId());

        if(bookInventory.getNoOfCopies() < buyBookRequest.getNoOfCopies()) return helper.generateUnavailableResponse();

        decreaseInventory(bookInventory,buyBookRequest.getNoOfCopies());

        return helper.generateSuccessReponse();
    }

    private void decreaseInventory(Inventory bookInventory,Integer noOfCopies) {

        bookInventory.setNoOfCopies(bookInventory.getNoOfCopies()-noOfCopies);
        inventoryService.saveInventory(bookInventory);

    }

    private Inventory getBookInventory(Integer id) {
        return inventoryService.getInventoryByBookId(id);
    }
}
