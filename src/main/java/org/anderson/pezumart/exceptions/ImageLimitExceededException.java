package org.anderson.pezumart.exceptions;

public class ImageLimitExceededException extends RuntimeException{
    public ImageLimitExceededException(String message) {
        super(message);
    }
}
