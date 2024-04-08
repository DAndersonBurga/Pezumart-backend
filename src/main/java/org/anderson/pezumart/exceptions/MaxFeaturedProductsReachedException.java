package org.anderson.pezumart.exceptions;

public class MaxFeaturedProductsReachedException extends RuntimeException {
    public MaxFeaturedProductsReachedException(String message) {
        super(message);
    }
}
