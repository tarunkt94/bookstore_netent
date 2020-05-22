package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.helpers.BookServiceHelper;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.requests.BookAddRequest;
import com.bookstore.requests.BookPartialSearchRequest;
import com.bookstore.requests.BookUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BooksService {

    @Autowired
    BookServiceHelper bookServiceHelper;

    @Autowired
    BookDAOIFace bookDAO;

    public Book addBook(BookAddRequest bookAddRequest) throws ValidationException, InternalServerException {

        try{
            bookServiceHelper.validateBookAddRequest(bookAddRequest);
            Book bookInDB = bookServiceHelper.getBookFromAddRequest(bookAddRequest);

            bookDAO.addBook(bookInDB);
            return bookInDB;
        }
        catch(Exception ex){
            //log exception
            throw new InternalServerException();
        }

    }


    public Book getBookById(Integer id) throws InternalServerException{

        try{
            return bookDAO.getBook(id);
        }
        catch(Exception ex){
            //log exception
            throw new InternalServerException();
        }
    }

    public List<Book> findBooks(BookPartialSearchRequest partialSearchRequest) throws InternalServerException {

        try{
            List<Book> books = bookDAO.findBooks(partialSearchRequest);
            return books;
        }
        catch(Exception ex){
            //log exception
            throw new InternalServerException();
        }


    }

    public void updateBook(Integer id, BookUpdateRequest bookUpdateRequest) throws ResourceNotFoundException,InternalServerException {

        try{
            bookServiceHelper.validateBookUpdateRequest(id);
            Book bookInDb = bookDAO.getBook(id);
            bookServiceHelper.updateBookDetails(bookInDb,bookUpdateRequest);
            bookDAO.saveBook(bookInDb);
        }
        catch(Exception ex){
            //log error
            throw new InternalServerException();
        }

    }

    public void deleteBook(Integer id) throws  ResourceNotFoundException,InternalServerException{
        try{
            bookServiceHelper.validateBookDeleteRequest(id);
            Book bookInDB = bookDAO.getBook(id);
            bookDAO.deleteBook(bookInDB);
        }catch(Exception ex){
            //log error
            throw new InternalServerException();
        }
    }
}
