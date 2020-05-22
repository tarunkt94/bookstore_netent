package com.bookstore.responses;

import com.bookstore.entity.Book;

import java.util.List;

public class ListBookResponse {

    public ListBookResponse(List<Book> books) {
        this.books = books;
    }

    List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
