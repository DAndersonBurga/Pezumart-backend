package org.anderson.pezumart.error;

import jakarta.servlet.http.HttpServletRequest;
import org.anderson.pezumart.exceptions.UsuarioExistsException;
import org.anderson.pezumart.exceptions.UsuarioNotFountException;
import org.anderson.pezumart.utils.error.ApiErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UsuarioEceptionHandler {


    @ExceptionHandler(UsuarioExistsException.class)
    public ResponseEntity<ApiError> handleUsuarioExistsException(HttpServletRequest httpRequest, UsuarioExistsException e) {

        ApiError apiError = ApiErrorUtils.generateApiError(e, httpRequest, "El usuario ya existe");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(UsuarioNotFountException.class)
    public ResponseEntity<ApiError> handleGenericException(HttpServletRequest httpRequest, UsuarioNotFountException e) {

        ApiError apiError = ApiErrorUtils.generateApiError(e, httpRequest, "El usuario no fue encontrado");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

}
