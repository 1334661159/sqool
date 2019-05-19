package com.abuqool.sqool.service.file.impl;

public class StorageException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -872839572006498746L;

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
