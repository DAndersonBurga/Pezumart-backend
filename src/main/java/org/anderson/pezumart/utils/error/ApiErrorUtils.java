package org.anderson.pezumart.utils.error;

import jakarta.servlet.http.HttpServletRequest;
import org.anderson.pezumart.error.ApiError;

import java.time.LocalDateTime;

public class ApiErrorUtils {
    public static ApiError generateApiError(Exception exception, HttpServletRequest request, String message) {
        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setTimestamp(LocalDateTime.now());
        apiError.setUrl(request.getRequestURL().toString());
        apiError.setMethod(request.getMethod());
        apiError.setMessage(message);
        return apiError;
    }
}
