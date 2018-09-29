package com.geek.logger.extract.exceptions;

/**
 * @Description: geek-logger-extract   Config not found.
 * @Author: Captain.Ma
 * @Date: 2018-09-29 14:45
 */
public class MappedFileConfigNotFoundException extends RuntimeException {
    public MappedFileConfigNotFoundException() {
    }

    public MappedFileConfigNotFoundException(String message) {
        super(message);
    }

    public MappedFileConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappedFileConfigNotFoundException(Throwable cause) {
        super(cause);
    }

    public MappedFileConfigNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
