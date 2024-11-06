package com.mozaiko.readfile.exception;

public class OutOfBoundsException extends Exception {

    private static final String ERROR_MESSAGE = "O valor de qty não pode ser maior do que a quantidade de linhas de epcs.";

    public OutOfBoundsException(){
        super(ERROR_MESSAGE);
    }

}
