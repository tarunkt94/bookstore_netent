package com.bookstore.daos;

import com.bookstore.entity.Book;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.requests.BookPartialSearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDAO implements BookDAOIFace {


    @Override
    public void addBook(Book book) {

    }

    @Override
    public void updateBook(Book book) {

    }

    @Override
    public void deleteBook(Book book) {

    }

    @Override
    public Book getBook(int id) {
        return null;
    }

    @Override
    public Book getBookByISBN(String isbn) {
        return null;
    }

    @Override
    public void saveBook(Book book) {

    }

    @Override
    public List<Book> findBooks(BookPartialSearchRequest partialSearchRequest) {
        return null;
    }
}
