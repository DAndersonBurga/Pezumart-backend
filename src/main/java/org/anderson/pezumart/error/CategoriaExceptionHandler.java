package org.anderson.pezumart.error;

import jakarta.servlet.http.HttpServletRequest;
import org.anderson.pezumart.exceptions.CategoriaNotFoundException;
import org.anderson.pezumart.utils.error.ApiErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CategoriaExceptionHandler {


    @ExceptionHandler(CategoriaNotFoundException.class)
    public ResponseEntity<ApiError> handleCategoriaNotFoundException(HttpServletRequest httpServletRequest, CategoriaNotFoundException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest, "No se encontró la categoría");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
