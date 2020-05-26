package com.bookstore.helpers;

import com.bookstore.config.Constants;
import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.DBException;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.pojos.MediaCoverage;
import com.bookstore.requests.BookAddRequest;
import com.bookstore.requests.BookPartialSearchRequest;
import com.bookstore.requests.BookUpdateRequest;
import com.bookstore.responses.BookResponse;
import com.bookstore.responses.MediaCoverageResponse;
import com.bookstore.service.BooksService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BookServiceHelper {

    @Autowired
    BookDAOIFace bookDAO;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    BooksService booksService;

    @Value("${media.coverage.url}")
    String mediaCoverageURL;

    public void validateBookAddRequest(BookAddRequest bookAddRequest) throws ValidationException, InternalServerException {

        validateAttributesOfBookAddRequest(bookAddRequest);

        Book book = getBookByISBN(bookAddRequest.getIsbn());

        if(book!=null){
            throw new ValidationException("Book already exists in our system");
        }

    }

    public void validateAttributesOfBookAddRequest(BookAddRequest bookAddRequest) throws ValidationException {

        if(bookAddRequest.getAuthor()==null || bookAddRequest.getIsbn()==null
                || bookAddRequest.getTitle() ==null || bookAddRequest.getPrice()==null){

            String msg = "Author, ISBN number, Title, Price are mandatory for adding a book";
            throw new ValidationException(msg);
        }

        if(bookAddRequest.getPrice()<0) throw new ValidationException("Book Price cannot be negative");

        if(bookAddRequest.getInventory()<0) throw new ValidationException("Book inventory cannot be less than 0");
    }

    private Book getBookByISBN(String isbn) throws InternalServerException {
        try{
            return bookDAO.getBookByISBN(isbn);
        }
        catch(DBException dbEx){
            log.error("Exception while trying to get book with ISBN : " + isbn,dbEx);
            throw new InternalServerException();
        }
    }

    public Book getBookFromAddRequest(BookAddRequest bookAddRequest) {

        Book book = new Book();
        book.setAuthor(bookAddRequest.getAuthor());
        book.setIsbn(bookAddRequest.getIsbn());
        book.setPrice(bookAddRequest.getPrice());
        book.setTitle(bookAddRequest.getTitle());

        return book;
    }

    public void validateBookUpdateRequest(Integer id) throws ResourceNotFoundException, InternalServerException {
        validateBookExistsInDB(id);
    }

    public void validateBookExistsInDB(Integer id) throws ResourceNotFoundException, InternalServerException {
        Book bookInDb = booksService.getBookById(id);
        if(bookInDb==null) throw new ResourceNotFoundException();
    }

    public void updateBookDetails(Book bookInDb, BookUpdateRequest bookUpdateRequest) {

        if(bookUpdateRequest.getAuthor()!=null) bookInDb.setAuthor(bookUpdateRequest.getAuthor());
        if(bookUpdateRequest.getIsbn()!=null) bookInDb.setIsbn(bookUpdateRequest.getIsbn());
        if(bookUpdateRequest.getPrice()!=null) bookInDb.setPrice(bookUpdateRequest.getPrice());
        if(bookUpdateRequest.getTitle()!=null) bookInDb.setTitle(bookUpdateRequest.getTitle());

    }

    public void validateBookDeleteRequest(Integer id) throws ResourceNotFoundException, InternalServerException {
        validateBookExistsInDB(id);
    }

    public Inventory getInventoryFromRequest(BookAddRequest bookAddRequest, Book bookInDB) {

        Inventory inventory = new Inventory();
        inventory.setBookId(bookInDB.getId());
        inventory.setNoOfCopies(bookAddRequest.getInventory()!=null ? bookAddRequest.getInventory() : 0);

        return  inventory;
    }

    public BookResponse generateBookResponse(Book bookInDB, Inventory inventory) {

        BookResponse addBookResponse = new BookResponse();
        addBookResponse.setAuthor(bookInDB.getAuthor());
        addBookResponse.setId(bookInDB.getId());
        addBookResponse.setIsbn(bookInDB.getIsbn());
        addBookResponse.setTitle(bookInDB.getTitle());
        addBookResponse.setPrice(bookInDB.getPrice());
        addBookResponse.setNoOfCopies(inventory.getNoOfCopies());

        return addBookResponse;
    }

    public List<MediaCoverage> getAllMediaCoverage() {

        ResponseEntity<List<MediaCoverage>> responseEntity ;
        List<MediaCoverage> response = new ArrayList<>();

        try{
            responseEntity = restTemplate.exchange(mediaCoverageURL, HttpMethod.GET,
                    new HttpEntity<>(null),new ParameterizedTypeReference<List<MediaCoverage>>(){});
            return responseEntity.getBody();
        }
        catch(Exception ex){
            //log error message
            return response;
        }
    }

    public MediaCoverageResponse generateUnavailableBookMediaCoverageResponse() {
        MediaCoverageResponse mediaCoverageResponse = new MediaCoverageResponse();
        mediaCoverageResponse.setSuccess(false);
        mediaCoverageResponse.setMsg("No book exists in the system with given ISBN");

        return mediaCoverageResponse;
    }

    public MediaCoverageResponse generateMediaCoverageResponse(List<MediaCoverage> mediaCoverageList) {

        MediaCoverageResponse response = new MediaCoverageResponse();
        response.setSuccess(true);
        response.setTitleList(getTitleList(mediaCoverageList));
        return response;
    }

    private List<String> getTitleList(List<MediaCoverage> mediaCoverageList) {
        List<String> response = new ArrayList<>();
        for(MediaCoverage mediaCoverage: mediaCoverageList) response.add(mediaCoverage.getTitle());
        return response;
    }

    public BookPartialSearchRequest modifyRequest(BookPartialSearchRequest partialSearchRequest) {
        if(partialSearchRequest.getAuthor()==null) partialSearchRequest.setAuthor(Constants.EMPTY_STRING);
        if(partialSearchRequest.getIsbn()==null) partialSearchRequest.setIsbn(Constants.EMPTY_STRING);
        if(partialSearchRequest.getTitle()==null) partialSearchRequest.setTitle(Constants.EMPTY_STRING);
        return partialSearchRequest;
    }
}
