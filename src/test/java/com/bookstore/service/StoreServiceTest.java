package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.StoreServiceHelper;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StoreServiceTest {

    @InjectMocks
    StoreService storeService;

    @Spy
    StoreServiceHelper helper;

    @Mock
    BooksService bookService;

    @Mock
    InventoryService inventoryService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationException.class)
    public void testBuyBookValidations() throws ValidationException, InternalServerException, InterruptedException {

        BuyBookRequest requestMock = new BuyBookRequest();

        requestMock.setBookId(null);

        try{
            storeService.buyBook(requestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("bookId cannot be null in the request",ex.getMessage());
            throw ex;
        }

        requestMock.setBookId(1);
        requestMock.setNoOfCopies(-1);

        try{
            storeService.buyBook(requestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Number of books to buy is invalid",ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = ValidationException.class)
    public void testBuyBookValidationBookDoesntExist() throws ValidationException, InternalServerException, InterruptedException {

        when(bookService.getBookById(anyInt())).thenReturn(null);

        BuyBookRequest request = new BuyBookRequest();
        request.setBookId(1);
        request.setNoOfCopies(1);

        try{
            storeService.buyBook(request);
        }
        catch(ValidationException vEx){
            Assert.assertEquals("Given bookId does not exist in our system",vEx.getMessage());
            throw vEx;
        }

    }

    @Test
    public void testBuyBookValidationNoOfCopies() throws ValidationException, InternalServerException, InterruptedException {

        BuyBookRequest request = new BuyBookRequest();
        Integer bookId = 1;

        request.setNoOfCopies(10);
        request.setBookId(bookId);

        Book bookMock = new Book();
        bookMock.setId(bookId);

        Inventory mockInventory = new Inventory();
        mockInventory.setBookId(bookId);
        mockInventory.setNoOfCopies(1);

        when(helper.validateRequestAndGetBook(request)).thenReturn(bookMock);

        when(bookService.getBookById(bookId)).thenReturn(bookMock);
        when(inventoryService.getInventoryByBookId(bookId)).thenReturn(mockInventory);

        BuyBookResponse buyBookResponse = storeService.buyBook(request);

        Assert.assertEquals("Not enough books available at the moment",buyBookResponse.getMsg());
        Assert.assertEquals(false,buyBookResponse.isSuccess());
    }

}
