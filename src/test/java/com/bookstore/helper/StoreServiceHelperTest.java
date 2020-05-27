package com.bookstore.helper;


import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.StoreServiceHelper;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.service.BooksService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class StoreServiceHelperTest {

    @InjectMocks
    StoreServiceHelper storeServiceHelper;

    @Mock
    BooksService booksService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }


    @Test(expected = ValidationException.class)
    public void testValidateRequest() throws InternalServerException, ValidationException {

        BuyBookRequest request = new BuyBookRequest();

        try{
            storeServiceHelper.validateRequestAndGetBook(request);
        }
        catch(ValidationException vEx){
            Assert.assertEquals("bookId cannot be null in the request",vEx.getMessage());
            throw vEx;
        }

        request.setBookId(1);
        request.setNoOfCopies(-1);

        try{
            storeServiceHelper.validateRequestAndGetBook(request);
        }
        catch(ValidationException vEx){
            Assert.assertEquals("bookId cannot be null in the request",vEx.getMessage());
            throw vEx;
        }

    }
}
