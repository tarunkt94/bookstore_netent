package com.bookstore.helper;

import com.bookstore.config.Constants;
import com.bookstore.daos.BookDAO;
import com.bookstore.entity.Book;
import com.bookstore.exceptions.DBException;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.BookServiceHelper;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.requests.BookAddRequest;
import com.bookstore.requests.BookPartialSearchRequest;
import com.bookstore.service.BooksService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServiceHelperTest {

    @Mock
    BookServiceHelper bookServiceHelper;

    @InjectMocks
    BookDAOIFace bookDAOIFace;

    @InjectMocks
    BooksService booksService;

    @Test(expected = ValidationException.class)
    public void testValidateBookAddRequestNulls() throws ValidationException{

        BookAddRequest addRequestMock = mock(BookAddRequest.class);

        when(addRequestMock.getIsbn()).thenReturn(null);
        when(addRequestMock.getAuthor()).thenReturn(null);
        when(addRequestMock.getTitle()).thenReturn(null);
        when(addRequestMock.getPrice()).thenReturn(null);
        try{
            bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Author, ISBN number, Title, Price are mandatory for adding a book",ex.getMessage());
            throw ex;
        }

        when(addRequestMock.getPrice()).thenReturn(-1.0F);
        try{
            bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Author, ISBN number, Title, Price are mandatory for adding a book",ex.getMessage());
            throw ex;
        }

    }

    @Test(expected = ValidationException.class)
    public void testValidateBookAddRequestNegativePrice() throws ValidationException{

        BookAddRequest addRequestMock = mock(BookAddRequest.class);

        when(addRequestMock.getIsbn()).thenReturn("mock");
        when(addRequestMock.getAuthor()).thenReturn("mock");
        when(addRequestMock.getTitle()).thenReturn("mock");
        when(addRequestMock.getPrice()).thenReturn(-1.0F);

        try{
            bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Book Price cannot be negative",ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = ValidationException.class)
    public void testValidateBookAddRequestNegativeInventory() throws ValidationException{

        BookAddRequest addRequestMock = mock(BookAddRequest.class);

        when(addRequestMock.getIsbn()).thenReturn("mock");
        when(addRequestMock.getAuthor()).thenReturn("mock");
        when(addRequestMock.getTitle()).thenReturn("mock");
        when(addRequestMock.getPrice()).thenReturn(1.0F);
        when(addRequestMock.getInventory()).thenReturn(-1);

        try{
            bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Book inventory cannot be less than 0",ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = ValidationException.class)
    public void testValidateBookAddRequestBookAlreadyExists() throws ValidationException, DBException {

        BookAddRequest addRequestMock = mock(BookAddRequest.class);

        Book bookMock = mock(Book.class);

        when(addRequestMock.getIsbn()).thenReturn("mock");
        when(addRequestMock.getAuthor()).thenReturn("mock");
        when(addRequestMock.getTitle()).thenReturn("mock");
        when(addRequestMock.getPrice()).thenReturn(1.0F);

        when(bookDAOIFace.getBookByISBN("mock")).thenReturn(bookMock);

        try{
            bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Book already exists in our system",ex.getMessage());
            throw ex;
        }

    }

    @Test
    public void testValidateBookAddRequestHappyCase(){

        BookAddRequest addRequestMock = mock(BookAddRequest.class);

        Book bookMock = mock(Book.class);

        when(addRequestMock.getIsbn()).thenReturn("mock");
        when(addRequestMock.getAuthor()).thenReturn("mock");
        when(addRequestMock.getTitle()).thenReturn("mock");
        when(addRequestMock.getPrice()).thenReturn(1.0F);

        try{
            when(bookDAOIFace.getBookByISBN("mock")).thenReturn(bookMock);
            bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);
        }
        catch(Exception ex){
            Assert.fail("Exception should not have been thrown");
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testValidateBookExistsInDb() throws InternalServerException,ResourceNotFoundException {

        when(booksService.getBookById(1)).thenReturn(null);

        bookServiceHelper.validateBookExistsInDB(1);

    }

    @Test
    public void testModifyRequest(){

        BookPartialSearchRequest mockRequest = mock(BookPartialSearchRequest.class);

        when(mockRequest.getAuthor()).thenReturn(null);
        when(mockRequest.getIsbn()).thenReturn(null);
        when(mockRequest.getTitle()).thenReturn(null);

        BookPartialSearchRequest response  = bookServiceHelper.modifyRequest(mockRequest);

        Assert.assertEquals(response.getAuthor(), Constants.EMPTY_STRING);
        Assert.assertEquals(response.getTitle(), Constants.EMPTY_STRING);
        Assert.assertEquals(response.getIsbn(), Constants.EMPTY_STRING);

    }

}
