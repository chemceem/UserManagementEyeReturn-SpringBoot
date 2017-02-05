package com.eyereturn.exceptions;

/**
 * Created by Chemcee. M. C on 03-02-2017.
 */
public class DuplicateEmailException extends Exception {

    public DuplicateEmailException() {
        super();
    }

    public DuplicateEmailException(String exception) {
        super(exception);
    }
}
