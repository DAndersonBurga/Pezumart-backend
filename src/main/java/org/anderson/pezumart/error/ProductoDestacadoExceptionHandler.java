package org.anderson.pezumart.error;

import jakarta.servlet.http.HttpServletRequest;
import org.anderson.pezumart.exceptions.MaxFeaturedProductsReachedException;
import org.anderson.pezumart.exceptions.ProductoAlreadyFeaturedException;
import org.anderson.pezumart.exceptions.ProductoDestacadoNotFoundException;
import org.anderson.pezumart.utils.error.ApiErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductoDestacadoExceptionHandler {

    @ExceptionHandler(ProductoDestacadoNotFoundException.class)
    public ResponseEntity<ApiError> handleProductoDestacadoNotFoundException(HttpServletRequest httpServletRequest,
                                                                             ProductoDestacadoNotFoundException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "El producto destacado no fue encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(MaxFeaturedProductsReachedException.class)
    public ResponseEntity<ApiError> handleMaxFeaturedProductsReachedException(HttpServletRequest httpServletRequest,
                                                                              MaxFeaturedProductsReachedException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "Se ha alcanzado el límite de productos destacados");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ProductoAlreadyFeaturedException.class)
    public ResponseEntity<ApiError> handleProductoAlreadyFeaturedException(HttpServletRequest httpServletRequest,
                                                                              ProductoAlreadyFeaturedException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "El producto ya ha sido destacado, prueba con otro!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
