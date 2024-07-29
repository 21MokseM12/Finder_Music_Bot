package com.layers.exception;

public class DaoException extends Throwable {

    public DaoException(Throwable cause) {super(cause);}

    public DaoException(String message) {super(message);}

    public DaoException() {}
}
