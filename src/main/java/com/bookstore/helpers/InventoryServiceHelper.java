package com.bookstore.helpers;

import com.bookstore.daos.BookDAO;
import com.bookstore.entity.Book;
import com.bookstore.entity.Inventory;
import com.bookstore.exceptions.ResourceNotFoundException;
import com.bookstore.exceptions.ValidationException;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.interfaces.InventoryDAOIFace;
import com.bookstore.requests.InventoryAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceHelper {

    @Autowired
    BookDAOIFace bookDAO;

    @Autowired
    InventoryDAOIFace inventoryDAO;

    public void validateRequest(InventoryAddRequest inventoryAddRequest) throws ValidationException{

        if(inventoryAddRequest.getBookId()==null) throw new ValidationException("bookId is empty in the request");
        Book bookInDB = bookDAO.getBook(inventoryAddRequest.getBookId());
        if(bookInDB==null) throw new ValidationException("Book doesnt exist in the system");

    }

    public Inventory convertRequest(InventoryAddRequest inventoryAddRequest) {

        Inventory inventory;

        inventory = inventoryDAO.getInventoryByBookId(inventoryAddRequest.getBookId());
        if(inventory==null) inventory = new Inventory();

        inventory.setBookId(inventoryAddRequest.getBookId());
        inventory.setNoOfCopies(inventory.getNoOfCopies() + inventoryAddRequest.getNoOfBooks());

        return inventory;
    }
}
