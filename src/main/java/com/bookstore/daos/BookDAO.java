package com.bookstore.daos;

import com.bookstore.entity.Book;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.repos.BooksRepo;
import com.bookstore.requests.BookPartialSearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDAO implements BookDAOIFace {

    @Autowired
    BooksRepo booksRepo;

    @Override
    public Book addBook(Book book) {
        return booksRepo.save(book);
    }

    @Override
    public void updateBook(Book book) {
        booksRepo.save(book);
    }

    @Override
    public void deleteBook(Book book) {
        booksRepo.delete(book);
    }

    @Override
    public Book getBook(int id) {
        return booksRepo.findById(id).orElse(null);
    }

    @Override
    public Book getBookByISBN(String isbn) {
        return booksRepo.getBookByIsbn(isbn);
    }

    @Override
    public void saveBook(Book book) {
        booksRepo.save(book);
    }

    @Override
    public List<Book> findBooks(BookPartialSearchRequest partialSearchRequest) {
        if(partialSearchRequest.getIsbn().isEmpty())
            return booksRepo.findBooks(partialSearchRequest.getTitle(),partialSearchRequest.getAuthor());
        else
            return booksRepo.findBooks(partialSearchRequest.getTitle(),partialSearchRequest.getAuthor(),partialSearchRequest.getIsbn());
    }
}
