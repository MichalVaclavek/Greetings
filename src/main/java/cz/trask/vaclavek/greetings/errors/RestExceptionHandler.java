package cz.trask.vaclavek.greetings.errors;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Central handling of the Exceptions raised during REST api call processing.<br>
 * Converts the given Exception into {@link ApiError} class instance which is<br>
 * then sent as {@code ResponseEntity<Object>} of the REST api call response.<br> 
 * 
 * @author Michal Vaclavek
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String error = "Malformed JSON request";
        return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
    }

    /**
     * Converts {@link ApiError} instance into ResponseEntity<Object>
     * 
     * @param apiError to convert
     * @return {@code ResponseEntity<Object>} created from {@code apiError}
     */
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
    
    /**
     * Converts {@link LanguageNotSupportedException} instance into {@link ApiError}<br>
     * and corresponding {@code ResponseEntity<Object>}
     * 
     * @param ex {@link LanguageNotSupportedException} instance
     * @return {@code ResponseEntity<Object>} created from input param. ex
     */
    @ExceptionHandler(LanguageNotSupportedException.class)
    protected ResponseEntity<Object> handleLanguageNotSupported(LanguageNotSupportedException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    
    /**
     * Converts {@link InvalidParameterException} instance into {@link ApiError}<br>
     * and corresponding {@code ResponseEntity<Object>}
     * 
     * @param ex {@link InvalidParameterException} instance
     * @return {@code ResponseEntity<Object>} created from input param. ex
     */
    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<Object> handleInvalidParameter(InvalidParameterException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    
    /**
     * Converts all other Exceptions
     * 
     * @param ex instance of Exceptions not defined to be processed by other methods
     * @return ResponseEntity<Object> created from input param. ex
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
}
