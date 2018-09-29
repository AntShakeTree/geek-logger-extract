package com.geek.logger.extract.exceptions;

/**
 * @Description: geek-logger-extract   Mapper file not found
 * @Author: Captain.Ma
 * @Date: 2018-09-29 14:47
 */
public class MappedFileNotFoundException extends RuntimeException {
    public MappedFileNotFoundException() {
    }

    public MappedFileNotFoundException(String message) {
        super(message);
    }

    public MappedFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappedFileNotFoundException(Throwable cause) {
        super(cause);
    }

    public MappedFileNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
