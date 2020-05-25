package com.bookstore.interfaces;

import com.bookstore.entity.Book;
import com.bookstore.exceptions.DBException;
import com.bookstore.requests.BookPartialSearchRequest;

import java.util.List;

public interface BookDAOIFace {

    public Book addBook(Book book) throws DBException;

    public void updateBook(Book book) throws DBException;

    public void deleteBook(Integer id) throws DBException;

    public Book getBook(int id) throws DBException;

    public Book getBookByISBN(String isbn) throws DBException;

    public void saveBook(Book book) throws DBException;

    List<Book> findBooks(BookPartialSearchRequest partialSearchRequest) throws DBException;
}
