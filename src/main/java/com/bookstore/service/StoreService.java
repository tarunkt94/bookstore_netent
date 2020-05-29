package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.StoreServiceHelper;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StoreService {

    @Autowired
    StoreServiceHelper helper;

    @Autowired
    InventoryService inventoryService;

    public BuyBookResponse buyBook(BuyBookRequest buyBookRequest) throws ValidationException, InternalServerException {

        Book bookInDB = helper.validateRequestAndGetBook(buyBookRequest);

        return helper.checkInventoryAndAct(bookInDB,buyBookRequest);
    }

}
