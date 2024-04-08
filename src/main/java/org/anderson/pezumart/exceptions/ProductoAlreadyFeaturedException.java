package org.anderson.pezumart.exceptions;

public class ProductoAlreadyFeaturedException extends RuntimeException {
    public ProductoAlreadyFeaturedException(String message) {
        super(message);
    }
}
