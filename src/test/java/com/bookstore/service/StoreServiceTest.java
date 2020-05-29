package com.bookstore.service;

import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.StoreServiceHelper;
import com.bookstore.requests.BuyBookRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;


import static org.mockito.Mockito.when;

public class StoreServiceTest {

    @InjectMocks
    StoreService storeService;

    @Mock
    StoreServiceHelper helper;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testBuyBookValidations() throws ValidationException, InternalServerException {

        BuyBookRequest buyBookRequest = new BuyBookRequest();

        when(helper.validateRequestAndGetBook(buyBookRequest)).thenThrow(new ValidationException("bookId cannot be null in the request"));

        try{
            storeService.buyBook(buyBookRequest);
        }
        catch(Exception ex){
            Assert.assertEquals("bookId cannot be null in the request",ex.getMessage());
            Assert.assertEquals(ValidationException.class,ex.getClass());
        }

        Mockito.reset(helper);
        when(helper.validateRequestAndGetBook(buyBookRequest)).thenThrow(new ValidationException("Number of books to buy is invalid"));

        try{
            storeService.buyBook(buyBookRequest);
        }
        catch(Exception ex){
            Assert.assertEquals("Number of books to buy is invalid",ex.getMessage());
            Assert.assertEquals(ValidationException.class,ex.getClass());
        }

        Mockito.reset(helper);
        when(helper.validateRequestAndGetBook(buyBookRequest)).thenThrow(new ValidationException("Given bookId does not exist in our system"));

        try{
            storeService.buyBook(buyBookRequest);
        }
        catch(Exception ex){
            Assert.assertEquals("Given bookId does not exist in our system",ex.getMessage());
            Assert.assertEquals(ValidationException.class,ex.getClass());
        }

    }
}
