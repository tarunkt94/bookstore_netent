package com.bookstore.responses;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ACCEPTED)
public class GenericResponse {

    String msg;

    public GenericResponse() {
    }

    public GenericResponse(String msg) {
        this.msg = msg;
    }
}
