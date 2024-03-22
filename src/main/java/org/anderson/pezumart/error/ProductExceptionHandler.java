package org.anderson.pezumart.error;

import jakarta.servlet.http.HttpServletRequest;
import org.anderson.pezumart.exceptions.ProductoModifiedByAnotherUserException;
import org.anderson.pezumart.exceptions.ProductoNotFoundException;
import org.anderson.pezumart.exceptions.UnauthorizedProductoDeletionException;
import org.anderson.pezumart.utils.error.ApiErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductExceptionHandler {

    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ApiError> handleProductoNotFoundException(HttpServletRequest httpServletRequest,
                                                                      ProductoNotFoundException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "El producto no fue encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(ProductoModifiedByAnotherUserException.class)
    public ResponseEntity<ApiError> handleProductoModifiedByAnotherUserException(HttpServletRequest httpServletRequest,
                                                                                 ProductoModifiedByAnotherUserException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "No tienes permisos para modificar este producto");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(UnauthorizedProductoDeletionException.class)
    public ResponseEntity<ApiError> handleUnauthorizedProductoDeletionException(HttpServletRequest httpServletRequest,
                                                                                UnauthorizedProductoDeletionException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "No tienes permisos para eliminar este producto");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
