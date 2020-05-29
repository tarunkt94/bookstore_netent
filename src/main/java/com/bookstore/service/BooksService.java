package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.DBException;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.BookServiceHelper;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.pojos.MediaCoverage;
import com.bookstore.requests.BookAddRequest;
import com.bookstore.requests.BookPartialSearchRequest;
import com.bookstore.requests.BookUpdateRequest;
import com.bookstore.responses.BookResponse;
import com.bookstore.responses.MediaCoverageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BooksService {

    @Autowired
    BookServiceHelper bookServiceHelper;

    @Autowired
    BookDAOIFace bookDAO;

    @Autowired
    InventoryService inventoryService;

    public BookResponse addBook(BookAddRequest bookAddRequest) throws ValidationException, InternalServerException {

        bookServiceHelper.validateBookAddRequest(bookAddRequest);
        Book bookInDB = bookServiceHelper.getBookFromAddRequest(bookAddRequest);

        try{
            bookDAO.addBook(bookInDB);
        }
        catch(DBException dbEx){
            log.error("Exception while trying to save book in DB : " + bookInDB, dbEx );
            throw new InternalServerException();
        }

        Inventory inventory =  addInventoryInDB(bookAddRequest,bookInDB);

        return bookServiceHelper.generateBookResponse(bookInDB,inventory);
    }

    private Inventory addInventoryInDB(BookAddRequest bookAddRequest, Book bookInDB) throws InternalServerException {

        Inventory inventory = bookServiceHelper.getInventoryFromRequest(bookAddRequest,bookInDB);
        inventoryService.saveInventory(inventory);
        return inventory;
    }


    public Book getBookById(Integer id) throws InternalServerException {
        if(id==null) return null;
        try{
            return bookDAO.getBook(id);
        }
        catch(DBException ex){
            log.error("Exception while trying to get book by id : " + id);
            throw new InternalServerException();
        }
    }


    public List<BookResponse> findBooks(BookPartialSearchRequest partialSearchRequest) throws InternalServerException {

        bookServiceHelper.modifyRequest(partialSearchRequest);

        List<Book> books;

        try{
            books = bookDAO.findBooks(partialSearchRequest);
        }
        catch(DBException dBEx){
            log.error("Exception while trying to findBooks with partial request : " + partialSearchRequest,dBEx);
            throw new InternalServerException();
        }

        return generateBookResponse(books);

    }

    private List<BookResponse> generateBookResponse(List<Book> books) {

        List<BookResponse> response =  new ArrayList<>();

        for(Book book : books){
            try{
                Inventory inventoryInDB = inventoryService.getInventoryByBookId(book.getId());
                response.add(bookServiceHelper.generateBookResponse(book,inventoryInDB));
            }
            catch(InternalServerException iSEx){
                log.error("Exception while trying to get inventory for bookId : " + book.getId(),iSEx);
            }
        }

        return response;
    }

    public void updateBook(Integer bookId, BookUpdateRequest bookUpdateRequest) throws ResourceNotFoundException, InternalServerException, ValidationException {

        bookServiceHelper.validateBookUpdateRequest(bookId,bookUpdateRequest);

        updateBookDetails(bookId,bookUpdateRequest);
        if(bookUpdateRequest.getNoOfCopies()!=null)updateInventoryDetails(bookId,bookUpdateRequest);

    }

    private void updateInventoryDetails(Integer bookId, BookUpdateRequest bookUpdateRequest) throws InternalServerException {

        Inventory inventory = inventoryService.getInventoryByBookId(bookId);
        inventory.setNoOfCopies(bookUpdateRequest.getNoOfCopies());
        inventoryService.saveInventory(inventory);

    }

    private void updateBookDetails(Integer bookId, BookUpdateRequest bookUpdateRequest) throws InternalServerException {
        try{
            Book bookInDb = bookDAO.getBook(bookId);
            bookServiceHelper.updateBookDetails(bookInDb,bookUpdateRequest);
            bookDAO.saveBook(bookInDb);
        }
        catch(DBException dbEx){
            log.error("Exception while trying to update book : " , dbEx);
            throw new InternalServerException();
        }
    }

    public void deleteBook(Integer id) throws ResourceNotFoundException, InternalServerException {

        bookServiceHelper.validateBookDeleteRequest(id);

        inventoryService.deleteInventoryOfBook(id);

        try{
            bookDAO.deleteBook(id);
        }
        catch(DBException dBEx){
            log.error("Exception while trying to delete book with id : " + id,dBEx);
            throw new InternalServerException();
        }

    }

    public BookResponse getBook(Integer id) throws ResourceNotFoundException, InternalServerException {

        Book book = getBookById(id);
        if(book == null) throw new ResourceNotFoundException();

        Inventory inventory = inventoryService.getInventoryByBookId(book.getId());

        return bookServiceHelper.generateBookResponse(book,inventory);

    }

    public MediaCoverageResponse getMediaCoverage(String isbn) throws ValidationException, InternalServerException {

        if(isbn==null || isbn.isEmpty()) throw new ValidationException("Request parameter 'isbn' cant be empty");

        Book bookInDB = getBookByISBN(isbn);

        if(bookInDB==null) return bookServiceHelper.generateUnavailableBookMediaCoverageResponse();

        List<MediaCoverage> mediaCoverageList = getMediaCoverage(bookInDB);

        return bookServiceHelper.generateMediaCoverageResponse(mediaCoverageList);
    }

    private List<MediaCoverage> getMediaCoverage(Book bookInDB) {

        List<MediaCoverage> allMediaCoverage = bookServiceHelper.getAllMediaCoverage();

        List<MediaCoverage> response =  new ArrayList<>();

        for(MediaCoverage mediaCoverage : allMediaCoverage){
            if(mediaCoverage.getTitle().contains(bookInDB.getTitle())
                || mediaCoverage.getBody().contains(bookInDB.getTitle())){
                response.add(mediaCoverage);
            }
        }
        return response;
    }

    private Book getBookByISBN(String isbn) throws InternalServerException {
        try{
            return bookDAO.getBookByISBN(isbn);
        }
        catch(DBException dbEx){
            log.error("Exception while trying to get book by ISBN " + isbn,dbEx);
            throw new InternalServerException();
        }
    }
}
