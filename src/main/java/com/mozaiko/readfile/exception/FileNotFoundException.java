package com.mozaiko.readfile.exception;

public class FileNotFoundException extends Exception {

    private static final String ERROR_MESSAGE = "O arquivo procurado não foi encontrado.";

    public FileNotFoundException(String msg){
        super(msg);
    }

    public FileNotFoundException(){
        super(ERROR_MESSAGE);
    }
}
