package com.bookstore.controllers;

import com.bookstore.entity.Book;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import com.bookstore.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @RequestMapping(method = RequestMethod.POST,value = "/buyBook")
    public BuyBookResponse buyBook(@RequestBody BuyBookRequest buyBookRequest) throws ValidationException {
        return storeService.buyBook(buyBookRequest);
    }

}
