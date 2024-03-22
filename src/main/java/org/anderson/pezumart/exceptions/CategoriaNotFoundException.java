package org.anderson.pezumart.exceptions;

public class CategoriaNotFoundException extends RuntimeException{
    public CategoriaNotFoundException(String message) {
        super(message);
    }
}
