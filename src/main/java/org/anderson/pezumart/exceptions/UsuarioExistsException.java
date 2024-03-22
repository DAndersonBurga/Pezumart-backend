package org.anderson.pezumart.exceptions;

public class UsuarioExistsException extends RuntimeException{

    public UsuarioExistsException(String message) {
        super(message);
    }
}
