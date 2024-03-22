package org.anderson.pezumart.error;

import jakarta.servlet.http.HttpServletRequest;
import org.anderson.pezumart.exceptions.EmptyImageException;
import org.anderson.pezumart.exceptions.ImageExtensionNotSupportedException;
import org.anderson.pezumart.exceptions.ImageLimitExceededException;
import org.anderson.pezumart.utils.error.ApiErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImageExceptionHandler {

    @ExceptionHandler(EmptyImageException.class)
    public ResponseEntity<ApiError> handleEmptyImageException(HttpServletRequest httpServletRequest,
                                                              EmptyImageException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "La imagen esta vacía. Por favor, seleccione una imagen válida.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ImageExtensionNotSupportedException.class)
    public ResponseEntity<ApiError> handleImageExtensionNotSupportedException(HttpServletRequest httpServletRequest,
                                                                              ImageExtensionNotSupportedException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "La extensión de la imagen no es soportada. Por favor, seleccione una imagen válida.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(ImageLimitExceededException.class)
    public ResponseEntity<ApiError> handleImageLimitExceededException(HttpServletRequest httpServletRequest,
                                                                      ImageLimitExceededException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpServletRequest,
                "La cantidad de imágenes por producto es 5, no te puedes exceder de este límite.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
