package com.bookstore.responses;

import com.bookstore.entity.Book;

import java.util.List;

public class ListBookResponse {

    List<BookResponse> books;

    public ListBookResponse(List<BookResponse> books) {
        this.books = books;
    }

    public List<BookResponse> getBooks() {
        return books;
    }

    public void setBooks(List<BookResponse> books) {
        this.books = books;
    }
}
