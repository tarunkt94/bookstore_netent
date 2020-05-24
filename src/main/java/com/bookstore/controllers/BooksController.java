package com.bookstore.controllers;

import com.bookstore.entity.Book;
import com.bookstore.exceptions.InternalServerException;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.requests.BookAddRequest;
import com.bookstore.requests.BookPartialSearchRequest;
import com.bookstore.requests.BookUpdateRequest;
import com.bookstore.responses.BookResponse;
import com.bookstore.responses.ListBookResponse;
import com.bookstore.responses.SuccessResponse;
import com.bookstore.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ResponseBody
@RequestMapping(value = "/books")
public class BooksController {

    @Autowired
    BooksService booksService;

    @RequestMapping(method = RequestMethod.POST)
    public BookResponse addBook(@RequestBody  BookAddRequest bookAddRequest) throws ValidationException {
        return booksService.addBook(bookAddRequest);
    }

    @RequestMapping(method = RequestMethod.GET,value = "/{id}")
    public BookResponse getBook(@PathVariable Integer id) throws ResourceNotFoundException{
        return booksService.getBook(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ListBookResponse getBooks(@RequestParam String isbn, @RequestParam String author, @RequestParam String title) throws InternalServerException{
        BookPartialSearchRequest partialSearchRequest = new BookPartialSearchRequest(isbn,title,author);
        List<BookResponse> books = booksService.findBooks(partialSearchRequest);
        return new ListBookResponse(books);
    }

    @RequestMapping(method = RequestMethod.PATCH,value = "/{id}")
    public SuccessResponse updateBook(@PathVariable Integer id, @RequestBody BookUpdateRequest bookUpdateRequest) throws ResourceNotFoundException, InternalServerException {
        booksService.updateBook(id,bookUpdateRequest);
        return new SuccessResponse();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public SuccessResponse deleteBook(@PathVariable Integer id) throws ResourceNotFoundException,InternalServerException{
        booksService.deleteBook(id);
        return new SuccessResponse();
    }
}
