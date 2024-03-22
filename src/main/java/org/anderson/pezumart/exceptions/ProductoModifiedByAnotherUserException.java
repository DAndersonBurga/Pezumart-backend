package org.anderson.pezumart.exceptions;

public class ProductoModifiedByAnotherUserException extends RuntimeException {
    public ProductoModifiedByAnotherUserException(String message) {
        super(message);
    }
}
