package com.bookstore.interfaces;

import com.bookstore.entity.Book;
import com.bookstore.requests.BookPartialSearchRequest;

import java.util.List;

public interface BookDAOIFace {

    public void addBook(Book book);

    public void updateBook(Book book);

    public void deleteBook(Book book);

    public Book getBook(int id);

    public Book getBookByISBN(String isbn);

    public void saveBook(Book book);

    List<Book> findBooks(BookPartialSearchRequest partialSearchRequest);
}
