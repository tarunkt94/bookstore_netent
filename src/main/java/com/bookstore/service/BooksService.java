package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
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

    public BookResponse addBook(BookAddRequest bookAddRequest) throws ValidationException {

        bookServiceHelper.validateBookAddRequest(bookAddRequest);
        Book bookInDB = bookServiceHelper.getBookFromAddRequest(bookAddRequest);

        bookDAO.addBook(bookInDB);

        Inventory inventory =  addInventoryInDB(bookAddRequest,bookInDB);

        return bookServiceHelper.generateBookResponse(bookInDB,inventory);
    }

    private Inventory addInventoryInDB(BookAddRequest bookAddRequest, Book bookInDB) {

        Inventory inventory = bookServiceHelper.getInventoryFromRequest(bookAddRequest,bookInDB);
        inventoryService.saveInventory(inventory);
        return inventory;

    }


    public Book getBookById(Integer id){
        if(id==null) return null;
        return bookDAO.getBook(id);
    }


    public List<BookResponse> findBooks(BookPartialSearchRequest partialSearchRequest) {

        bookServiceHelper.modifyRequest(partialSearchRequest);
        List<Book> books = bookDAO.findBooks(partialSearchRequest);

        return generateBookResponse(books);

    }

    private List<BookResponse> generateBookResponse(List<Book> books) {

        List<BookResponse> response =  new ArrayList<>();

        for(Book book : books){
            Inventory inventoryInDB = inventoryService.getInventoryByBookId(book.getId());
            response.add(bookServiceHelper.generateBookResponse(book,inventoryInDB));
        }

        return response;
    }

    public void updateBook(Integer id, BookUpdateRequest bookUpdateRequest) throws ResourceNotFoundException {

        bookServiceHelper.validateBookUpdateRequest(id);
        Book bookInDb = bookDAO.getBook(id);
        bookServiceHelper.updateBookDetails(bookInDb,bookUpdateRequest);
        bookDAO.saveBook(bookInDb);
    }

    public void deleteBook(Integer id) throws  ResourceNotFoundException{

        bookServiceHelper.validateBookDeleteRequest(id);

        Book bookInDB = bookDAO.getBook(id);
        bookDAO.deleteBook(bookInDB);

    }

    public BookResponse getBook(Integer id) throws ResourceNotFoundException {

        Book book = getBookById(id);
        if(book == null) throw new ResourceNotFoundException();

        Inventory inventory = inventoryService.getInventoryByBookId(book.getId());

        return bookServiceHelper.generateBookResponse(book,inventory);

    }

    public MediaCoverageResponse getMediaCoverage(String isbn) throws ValidationException {

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

    private Book getBookByISBN(String isbn) {
        return bookDAO.getBookByISBN(isbn);
    }
}
