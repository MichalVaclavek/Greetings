package cz.trask.vaclavek.greetings.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception to be thrown, when a REST api call with unsufficient parameters is received.
 * 
 * @author Michal Vaclavek
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends RuntimeException
{
    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = -5767574973768520183L;
    
    // name of REST parameter, which is missing or invalid
    private String parameterName;
    // value of REST parameter, which is invalid
    private Object parameterValue;
    

    public InvalidParameterException(String fieldName, Object fieldValue) {
        super(String.format("Invalid value '%s' or missing parameter '%s'.", fieldValue, fieldName));
        this.parameterName = fieldName;
        this.parameterValue = fieldValue;
    }

    public String getFieldName() {
        return parameterName;
    }

    public Object getFieldValue() {
        return parameterValue;
    }
    
}
