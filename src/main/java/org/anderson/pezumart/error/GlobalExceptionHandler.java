package org.anderson.pezumart.error;

import jakarta.servlet.http.HttpServletRequest;
import org.anderson.pezumart.utils.error.ApiErrorUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(HttpServletRequest httpRequest, Exception e) {

        ApiError apiError = ApiErrorUtils.generateApiError(e, httpRequest, "Error interno en el servidor, vuelva a intentarlo");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(HttpServletRequest httpRequest, MethodArgumentNotValidException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpRequest, "Error en los datos enviados");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(HttpServletRequest httpRequest, IOException e) {
        ApiError apiError = ApiErrorUtils.generateApiError(e, httpRequest, "Error en la lectura de los archivos");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

}
