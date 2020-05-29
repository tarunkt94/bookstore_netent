package com.bookstore.entity;

import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "inventory",schema = "public")
@ToString
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
