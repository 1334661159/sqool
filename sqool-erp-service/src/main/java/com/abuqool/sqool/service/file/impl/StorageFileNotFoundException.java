package com.abuqool.sqool.service.file.impl;

public class StorageFileNotFoundException extends StorageException {

    /**
     * 
     */
    private static final long serialVersionUID = 3665306903492464296L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}