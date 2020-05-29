package com.bookstore.daos;

import com.bookstore.entity.Book;
import com.bookstore.exceptions.DBException;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.repos.BooksRepo;
import com.bookstore.requests.BookPartialSearchRequest;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookDAO implements BookDAOIFace {

    @Autowired
    BooksRepo booksRepo;

    @Override
    public Book addBook(Book book) throws DBException {
        try{
            return booksRepo.save(book);
        }
        catch(Exception ex){
            log.error("Exception while saving book : " + book,ex.getStackTrace());
            throw new DBException();
        }
    }

    @Override
    public void updateBook(Book book)  throws DBException{
        try{
            booksRepo.save(book);
        }
        catch(Exception ex){
            log.error("Exception while trying to updateBook : " + book, ex);
            throw new DBException();
        }
    }

    @Override
    public void deleteBook(Integer id) throws DBException {
        try{
            booksRepo.deleteById(id);
        }
        catch(Exception ex){
            log.error("Exception while trying to delete book with id : " + id, ex);
            throw new DBException();
        }
    }

    @Override
    public Book getBook(int id) throws DBException {
        try{
            return booksRepo.findById(id).orElse(null);
        }
        catch(Exception ex){
            log.error("Exception while trying to get book with id : " + id, ex);
            throw new DBException();
        }
    }

    @Override
    public Book getBookByISBN(String isbn) throws DBException{
        try{
            return booksRepo.getBookByIsbn(isbn);
        }
        catch(Exception ex){
            log.error("Exception while trying to get book with ISBN : " + isbn, ex);
            throw new DBException();
        }
    }

    @Override
    public void saveBook(Book book) throws DBException {
        try{
            booksRepo.save(book);
        }
        catch(Exception ex){
            log.error("Exception while trying to save book : " + book,ex);
            throw new DBException();
        }
    }

    @Override
    public List<Book> findBooks(BookPartialSearchRequest partialSearchRequest) throws DBException {
        try{
            if(partialSearchRequest.getIsbn().isEmpty())
                return booksRepo.findBooks(partialSearchRequest.getTitle(),partialSearchRequest.getAuthor());
            else
                return booksRepo.findBooks(partialSearchRequest.getTitle(),partialSearchRequest.getAuthor(),partialSearchRequest.getIsbn());
        }
        catch(Exception ex){
            log.error("Exception while trying to get books with partial search request : " + partialSearchRequest,ex);
            throw new DBException();
        }
    }
}
