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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServiceHelperTest {

    @InjectMocks
    BookServiceHelper bookServiceHelper;

    @Mock
    BookDAO bookDAOIFace;

    @Mock
    BooksService booksService;

    private final String MOCK_ISBN = "mock";
    private final String MOCK_TITLE = "mock";
    private final String MOCK_AUTHOR = "mock";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = ValidationException.class)
    public void testValidateBookAddRequestNulls() throws ValidationException{

        BookAddRequest addRequestMock = new BookAddRequest();

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

        BookAddRequest addRequestMock = new BookAddRequest();

        addRequestMock.setAuthor(MOCK_AUTHOR);
        addRequestMock.setTitle(MOCK_TITLE);
        addRequestMock.setIsbn(MOCK_ISBN);
        addRequestMock.setPrice(-1.0F);

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

        BookAddRequest addRequestMock = new BookAddRequest();

        addRequestMock.setPrice(1.0F);
        addRequestMock.setAuthor(MOCK_AUTHOR);
        addRequestMock.setTitle(MOCK_TITLE);
        addRequestMock.setIsbn(MOCK_ISBN);
        addRequestMock.setInventory(-1);

        try{
            bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Book inventory cannot be less than 0",ex.getMessage());
            throw ex;
        }
    }

    @Test(expected = ValidationException.class)
    public void testValidateBookAddRequestBookAlreadyExists() throws ValidationException, InternalServerException,DBException {

        BookAddRequest addRequestMock = new BookAddRequest();

        addRequestMock.setPrice(1.0F);
        addRequestMock.setAuthor(MOCK_AUTHOR);
        addRequestMock.setTitle(MOCK_TITLE);
        addRequestMock.setIsbn(MOCK_ISBN);

        Mockito.doReturn(new Book()).when(bookDAOIFace).getBookByISBN(MOCK_ISBN);

        try{
            bookServiceHelper.validateBookAddRequest(addRequestMock);
        }
        catch(ValidationException ex){
            Assert.assertEquals("Book already exists in our system",ex.getMessage());
            throw ex;
        }

    }

    @Test
    public void testValidateBookAddRequestHappyCase() throws DBException, ValidationException {

        BookAddRequest addRequestMock = new BookAddRequest();

        addRequestMock.setPrice(1.0F);
        addRequestMock.setAuthor(MOCK_AUTHOR);
        addRequestMock.setTitle(MOCK_TITLE);
        addRequestMock.setIsbn(MOCK_ISBN);

        when(bookDAOIFace.getBookByISBN(MOCK_ISBN)).thenReturn(new Book());
        bookServiceHelper.validateAttributesOfBookAddRequest(addRequestMock);

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testValidateBookExistsInDb() throws InternalServerException,ResourceNotFoundException {

        when(booksService.getBookById(1)).thenReturn(null);

        bookServiceHelper.validateBookExistsInDB(1);

    }

    @Test
    public void testModifyRequest(){

        BookPartialSearchRequest mockRequest = new BookPartialSearchRequest(null,null,null);

        BookPartialSearchRequest response  = bookServiceHelper.modifyRequest(mockRequest);

        Assert.assertEquals(response.getAuthor(), Constants.EMPTY_STRING);
        Assert.assertEquals(response.getTitle(), Constants.EMPTY_STRING);
        Assert.assertEquals(response.getIsbn(), Constants.EMPTY_STRING);

    }

}
