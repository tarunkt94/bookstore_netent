package com.bookstore.entity;

import javax.persistence.*;

@Entity
@Table(name = "inventory",schema = "bookstore")
public class Inventory {

    @Id
    @GeneratedValue
    int id;

    @Column(name = "book_id")
    private int bookId;

    @Column(name = "no_of_copies")
    private int noOfCopies;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getNoOfCopies() {
        return noOfCopies;
    }

    public void setNoOfCopies(int noOfCopies) {
        this.noOfCopies = noOfCopies;
    }
}
