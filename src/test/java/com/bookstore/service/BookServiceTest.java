package com.bookstore.service;

import com.bookstore.config.Constants;
import com.bookstore.daos.BookDAO;
import com.bookstore.exceptions.DBException;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.BookServiceHelper;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.responses.MediaCoverageResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    @InjectMocks
    BooksService booksService;

    @Mock
    BookDAO bookDAO;

    @Spy
    BookServiceHelper helper;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InternalServerException.class)
    public  void testGetBookById() throws InternalServerException,DBException{

        Assert.assertNull(booksService.getBookById(null));

        when(bookDAO.getBook(1)).thenThrow(new DBException());

        booksService.getBookById(1);

    }

    @Test(expected = ValidationException.class)
    public void testMediaCoverageResponseEmptyOrNullISBN() throws ValidationException,InternalServerException{

        try{
            booksService.getMediaCoverage(null);
        }
        catch(ValidationException vEx){
            assertEquals("Request parameter 'isbn' cant be empty",vEx.getMessage());
            throw vEx;
        }

        try{
            booksService.getMediaCoverage(Constants.EMPTY_STRING);
        }
        catch(ValidationException vEx){
            assertEquals("Request parameter 'isbn' cant be empty",vEx.getMessage());
            throw vEx;
        }

    }

    @Test
    public void testMediaCoverageNonExistentBook() throws ValidationException,InternalServerException,DBException{

        String mockISBN = "1";

        when(bookDAO.getBookByISBN(mockISBN)).thenReturn(null);

        MediaCoverageResponse response = booksService.getMediaCoverage(mockISBN);

        Assert.assertEquals(false,response.isSuccess());
        Assert.assertEquals("No book exists in the system with given ISBN",response.getMsg());

    }

}
