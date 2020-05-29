package com.bookstore.helpers;

import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import com.bookstore.service.BooksService;
import com.bookstore.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StoreServiceHelper {

    @Autowired
    BookDAOIFace bookDAO;

    @Autowired
    BooksService booksService;

    @Autowired
    InventoryService inventoryService;

    public Book validateRequestAndGetBook(BuyBookRequest buyBookRequest) throws ValidationException, InternalServerException {

        Integer bookId = buyBookRequest.getBookId();
        if(bookId==null) throw new ValidationException("bookId cannot be null in the request");

        if(buyBookRequest.getNoOfCopies() <=0)
            throw new ValidationException("Number of books to buy is invalid");

        Book book = getBookFromBookService(bookId);
        if(book==null) throw new ValidationException("Given bookId does not exist in our system");

        return book;
    }

    private Book getBookFromBookService(Integer bookId) throws InternalServerException {
        return booksService.getBookById(bookId);
    }

    private BuyBookResponse generateUnavailableResponse() {

        BuyBookResponse buyBookResponse = new BuyBookResponse();
        buyBookResponse.setSuccess(false);
        buyBookResponse.setMsg("Not enough books available at the moment");

        return buyBookResponse;

    }

    private BuyBookResponse generateSuccessResponse() {

        BuyBookResponse buyBookResponse = new BuyBookResponse();
        buyBookResponse.setSuccess(true);
        buyBookResponse.setMsg("Books bought successfully");

        return buyBookResponse;
    }

    @Transactional
    public BuyBookResponse checkInventoryAndAct(Book bookInDB, BuyBookRequest buyBookRequest) throws InternalServerException {

        Inventory bookInventory = getBookInventoryForBuying(bookInDB.getId());

        if(bookInventory.getNoOfCopies() < buyBookRequest.getNoOfCopies()) return generateUnavailableResponse();

        decreaseInventory(bookInventory,buyBookRequest.getNoOfCopies());

        return generateSuccessResponse();
    }

    private Inventory getBookInventoryForBuying(Integer id) throws InternalServerException{
        return inventoryService.getBookInventoryForBuying(id);
    }

    private void decreaseInventory(Inventory bookInventory,Integer noOfCopiesToBuy) throws InternalServerException {

        int finalNumberOfCopies = getNoOfCopiesAfterBuying(bookInventory,noOfCopiesToBuy);
        bookInventory.setNoOfCopies(finalNumberOfCopies);
        inventoryService.saveInventory(bookInventory);

    }

    private int getNoOfCopiesAfterBuying(Inventory bookInventory, Integer noOfCopiesToBuy) {
        if(bookInventory.getNoOfCopies()==noOfCopiesToBuy) return 1;
        return bookInventory.getNoOfCopies()-noOfCopiesToBuy;
    }

}
