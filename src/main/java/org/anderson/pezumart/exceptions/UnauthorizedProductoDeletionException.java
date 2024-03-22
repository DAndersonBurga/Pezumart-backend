package org.anderson.pezumart.exceptions;

public class UnauthorizedProductoDeletionException extends RuntimeException {
    public UnauthorizedProductoDeletionException(String message) {
        super(message);
    }
}
