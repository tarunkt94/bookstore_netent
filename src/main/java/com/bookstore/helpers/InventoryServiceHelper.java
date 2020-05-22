package com.bookstore.helpers;

import com.bookstore.daos.BookDAO;
import com.bookstore.entity.Book;
import com.bookstore.interfaces.BookDAOIFace;
import com.bookstore.requests.InventoryAddRequest;
import org.springframework.stereotype.Service;

@Service
public class InventoryServiceHelper {

    BookDAOIFace bookDAO;

    public void validateRequest(InventoryAddRequest inventoryAddRequest) {

        Book bookInDB = bookDAO.getBook(inventoryAddRequest.getBookId());
        if(bookInDB==null) throw new Re
    }
}
