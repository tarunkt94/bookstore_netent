package com.bookstore.helpers;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.requests.BookAddRequest;
import com.bookstore.requests.BookUpdateRequest;
import com.bookstore.responses.BookResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceHelper {

    @Autowired
    BookDAOIFace bookDAO;

    public void validateBookAddRequest(BookAddRequest bookAddRequest) throws ValidationException {

        if(bookAddRequest.getAuthor()==null || bookAddRequest.getIsbn()==null
        || bookAddRequest.getTitle() ==null || bookAddRequest.getPrice()==null){

            String msg = "Author, ISBN number, Title, Price are mandatory for adding a book";
            throw new ValidationException(msg);
        }

        Book book = bookDAO.getBookByISBN(bookAddRequest.getIsbn());
        if(book!=null){
            throw new ValidationException("Book already exists in our system");
        }

        if(bookAddRequest.getPrice()<0) throw new ValidationException("Book Price cannot be negative");
    }

    public Book getBookFromAddRequest(BookAddRequest bookAddRequest) {

        Book book = new Book();
        book.setAuthor(bookAddRequest.getAuthor());
        book.setIsbn(bookAddRequest.getIsbn());
        book.setPrice(bookAddRequest.getPrice());
        book.setTitle(bookAddRequest.getTitle());

        return book;
    }

    public void validateBookUpdateRequest(Integer id) throws ResourceNotFoundException {
        validateBookExistsInDB(id);
    }

    public void validateBookExistsInDB(Integer id) throws  ResourceNotFoundException{
        Book bookInDb = bookDAO.getBook(id);
        if(bookInDb==null) throw new ResourceNotFoundException();
    }

    public void updateBookDetails(Book bookInDb, BookUpdateRequest bookUpdateRequest) {

        if(bookUpdateRequest.getAuthor()!=null) bookInDb.setAuthor(bookUpdateRequest.getAuthor());
        if(bookUpdateRequest.getIsbn()!=null) bookInDb.setIsbn(bookUpdateRequest.getIsbn());
        if(bookUpdateRequest.getPrice()!=null) bookInDb.setPrice(bookUpdateRequest.getPrice());
        if(bookUpdateRequest.getTitle()!=null) bookInDb.setTitle(bookUpdateRequest.getTitle());

    }

    public void validateBookDeleteRequest(Integer id) throws ResourceNotFoundException {
        validateBookExistsInDB(id);
    }

    public Inventory getInventoryFromRequest(BookAddRequest bookAddRequest, Book bookInDB) {

        Inventory inventory = new Inventory();
        inventory.setBookId(bookInDB.getId());
        inventory.setNoOfCopies(bookAddRequest.getInventory()!=null ? bookAddRequest.getInventory() : 0);

        return  inventory;
    }

    public BookResponse generateBookResponse(Book bookInDB, Inventory inventory) {

        BookResponse addBookRespone = new BookResponse();
        addBookRespone.setAuthor(bookInDB.getAuthor());
        addBookRespone.setId(bookInDB.getId());
        addBookRespone.setIsbn(bookInDB.getIsbn());
        addBookRespone.setTitle(bookInDB.getTitle());
        addBookRespone.setPrice(bookInDB.getPrice());
        addBookRespone.setNoOfCopies(inventory.getNoOfCopies());

        return addBookRespone;
    }
}
