package com.bookstore.repos;

import com.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepo extends JpaRepository<Book, Integer> {

    Book getBookByIsbn(String isbn);

    @Query(value = "select * from bookstore.books where title like %?1% and author like %?2% and isbn like ?3 ", nativeQuery = true)
    List<Book> findBooks(String title, String author, String isbn);

    @Query(value = "select * from bookstore.books where title like %?1% and author like %?2% ", nativeQuery = true)
    List<Book> findBooks(String title, String author);

}
