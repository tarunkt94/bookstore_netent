package com.bookstore.exceptions;

public class InternalServerException extends Exception {

    public InternalServerException(){

    }

    InternalServerException(String msg){
        super(msg);
    }
}
