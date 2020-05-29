package com.bookstore.requests;

public class InventoryAddRequest {

    private Integer bookId;
    private Integer noOfBooks;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getNoOfBooks() {
        return noOfBooks;
    }

    public void setNoOfBooks(Integer noOfBooks) {
        this.noOfBooks = noOfBooks;
    }
}
