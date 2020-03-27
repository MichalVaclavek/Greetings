package cz.trask.vaclavek.greetings.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excpetion to be thrown, when a REST api call requests greeting for non-supported language_COUNTRY.
 *   
 * @author Michal Vaclavek
 *
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Language Not Supported Exception!")
public class LanguageNotSupportedException extends RuntimeException
{
    /**
     * Generated serial version ID
     */
    private static final long serialVersionUID = -2416334043811848871L;
    
    public LanguageNotSupportedException(String message) {
        super(message);
    }
}
