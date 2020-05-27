package com.bookstore.helper;


import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.StoreServiceHelper;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import com.bookstore.service.BooksService;
import com.bookstore.service.InventoryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class StoreServiceHelperTest {

    @InjectMocks
    StoreServiceHelper storeServiceHelper;

    @Mock
    BooksService booksService;

    @Mock
    InventoryService inventoryService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testValidateRequest() throws InternalServerException, ValidationException {

        BuyBookRequest request = new BuyBookRequest();

        try{
            storeServiceHelper.validateRequestAndGetBook(request);
        }
        catch(ValidationException vEx){
            Assert.assertEquals("bookId cannot be null in the request",vEx.getMessage());
            Assert.assertEquals(ValidationException.class,vEx.getClass());
        }

        request.setBookId(1);
        request.setNoOfCopies(-1);

        try{
            storeServiceHelper.validateRequestAndGetBook(request);
        }
        catch(ValidationException vEx){
            Assert.assertEquals("Number of books to buy is invalid",vEx.getMessage());
            Assert.assertEquals(ValidationException.class,vEx.getClass());
        }

        request.setNoOfCopies(1);
        when(booksService.getBookById(1)).thenReturn(null);

        try{
            storeServiceHelper.validateRequestAndGetBook(request);
        }
        catch(ValidationException vEx){
            Assert.assertEquals("Given bookId does not exist in our system",vEx.getMessage());
            Assert.assertEquals(ValidationException.class,vEx.getClass());
        }

    }

    @Test
    public void testCheckInventoryAndAct() throws InternalServerException {

        Integer bookId = 1;
        Integer noOfBooksToBuy  = 10;
        Integer noOfBookToAvailable = 1;

        BuyBookRequest request = new BuyBookRequest();
        request.setBookId(bookId);
        request.setNoOfCopies(noOfBooksToBuy);

        Book bookInDb = new Book();
        bookInDb.setId(bookId);

        Inventory mockInventory = new Inventory();
        mockInventory.setBookId(bookId);
        mockInventory.setNoOfCopies(noOfBookToAvailable);

        when(inventoryService.getBookInventoryForBuying(bookId)).thenReturn(mockInventory);


        BuyBookResponse response = storeServiceHelper.checkInventoryAndAct(bookInDb,request);
        Assert.assertEquals(false,response.isSuccess());
        Assert.assertEquals("Not enough books available at the moment",response.getMsg());

        noOfBookToAvailable=100;
        mockInventory.setNoOfCopies(noOfBookToAvailable);

        response = storeServiceHelper.checkInventoryAndAct(bookInDb,request);
        Assert.assertEquals(true,response.isSuccess());
        Assert.assertEquals("Books bought successfully",response.getMsg());



    }
}
