package com.bookstore.exceptions;

public class ValidationException extends Exception {
    
    String msg;

    public  ValidationException(){

    }

    public ValidationException(String msg){
        super(msg);
    }
}
