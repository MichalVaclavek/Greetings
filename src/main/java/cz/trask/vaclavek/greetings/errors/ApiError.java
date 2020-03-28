package cz.trask.vaclavek.greetings.errors;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Class to hold information about REST api call errors. This class instance is returned<br>
 * as JSON response in case error arrise during REST api call processing.<br>
 * <p>
 * Can contain following info about error:
 * <ul>
 *   <li>HttpStatus error status</li>
 *   <li>date/time when the error occured</li>
 *   <li>message - short description of the error</li>
 *   <li>debugMessage - detailed, debug related description of the error</li>
 * </ul>
 * 
 * @author Michal Vaclavek
 *
 */
public class ApiError
{
    private HttpStatus status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone="Europe/Prague")
    private LocalDateTime timestamp;
    
    private String message;
    private String debugMessage="";

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public String getDebugMessage() {
        return this.debugMessage;
    }
    
}
