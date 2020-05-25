package com.bookstore.helpers;

import com.bookstore.entity.Book;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.requests.BuyBookRequest;
import com.bookstore.responses.BuyBookResponse;
import com.bookstore.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceHelper {

    @Autowired
    BookDAOIFace bookDAO;

    @Autowired
    BooksService booksService;

    public Book validateRequestAndGetBook(BuyBookRequest buyBookRequest) throws ValidationException, InternalServerException {

        Integer bookId = buyBookRequest.getBookId();
        if(bookId==null) throw new ValidationException("bookId cannot be null in the request");

        if(buyBookRequest.getNoOfCopies() == null || buyBookRequest.getNoOfCopies() <=0)
            throw new ValidationException("Number of books to buy is invalid");

        Book book = booksService.getBookById(bookId);
        if(book==null) throw new ValidationException("Given bookId does not exist in our system");

        return book;
    }

    public BuyBookResponse generateUnavailableResponse() {

        BuyBookResponse buyBookResponse = new BuyBookResponse();
        buyBookResponse.setSuccess(false);
        buyBookResponse.setMsg("Not enough books available at the moment");

        return buyBookResponse;

    }

    public BuyBookResponse generateSuccessReponse() {

        BuyBookResponse buyBookResponse = new BuyBookResponse();
        buyBookResponse.setSuccess(true);
        buyBookResponse.setMsg("Books bought successfully");

        return buyBookResponse;
    }
}
