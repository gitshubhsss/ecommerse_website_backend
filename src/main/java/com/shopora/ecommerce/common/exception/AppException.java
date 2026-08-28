package com.shopora.ecommerce.common.exception;

import org.springframework.http.HttpStatus;

public class AppException extends  RuntimeException{
    private final HttpStatus status;

    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return this.status.value();
    }


}
