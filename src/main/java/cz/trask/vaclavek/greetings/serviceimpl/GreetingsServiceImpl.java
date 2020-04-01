package cz.trask.vaclavek.greetings.serviceimpl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import cz.trask.vaclavek.greetings.errors.LanguageNotSupportedException;
import cz.trask.vaclavek.greetings.service.GreetingsService;

import cz.trask.vaclavek.greetings.service.TimePeriodService.TimePeriod;

/**
 * Implementation of the {@link GreetingsService}
 * 
 * @see GreetingsConfiguration
 * 
 * @author Michal Vaclavek
 *
 */
@Service
public class GreetingsServiceImpl implements GreetingsService
{

    // ****** CLASS CONSTANTS ******
    
    private static final Logger Log = LoggerFactory.getLogger(GreetingsServiceImpl.class);
    
    /*
     * Keys of the 'messages_language_COUNTRY.properties' file items holding corresponding greeting texts<br>
     * Every language/coutry combination has its own properties file with the greetings texts in that specific language/coutry.
     * The keys are used by {@code MessageSource} instance of this {@code GreetingsServiceImpl} class
     */
    public static final String GREETING_MORNING_KEY = "greeting.timesensitive.morning";
    public static final String GREETING_AFTERNOON_KEY = "greeting.timesensitive.afternoon";
    public static final String GREETING_EVENING_KEY = "greeting.timesensitive.evening";
    public static final String GREETING_GENERAL_TIMESENSITIVE_KEY = "greeting.timesensitive.general";
    
    public static final String GREETING_GENERAL_TIMEINSENSITIVE_KEY = "greeting.timesinensitive.general";
    
    /**
     * Key of the 'messages.properties' file item holding common error text in case that greeting for requested language
     * is not supported.
     */
    public static final String GREETING_ERROR_LANGUAGENOTSUPPORTED_KEY = "greeting.error.language.notsupported";
    
    /**
     * Key of the 'messages.properties' file item holding common error text in case that greeting text cannot be returned
    */
    private static final String GREETING_ERROR_KEY = "greeting.error";
    
    /**
     * Deafult greeting text to identify, if the correct greeting was (not) find in {@link #messages}
     */
    private static final String GREETING_NOT_FOUND = "Not_found";
    

    // ****** INSTANCE FIELDS ******
    
    /**
     * Source of the greeting texts
     */
    private MessageSource messages;
    
    /**
     * General error message text in case that any suitable greeting text cannot be returned
     */
    private static String errorMessageGeneral;
    
    
    // ****** INSTANCE CONSTRUCTORS ******
    
    /**
     * Standard Constructor with MessageSource instance injected.
     * 
     * @param messages source of the greeting texts
     */
    public GreetingsServiceImpl(MessageSource messages) {
       super();
       this.messages = messages;
       errorMessageGeneral = messages.getMessage(GREETING_ERROR_KEY, null, null);
    }

    // ****** INSTANCE PUBLIC METHODS ******

    /**
     * {@inheritDoc}
     * <p>
     * If the greeting text is same as the default {@code GREETING_NOT_FOUND}, then text is not found for<br>
     * the locale's language in the messages_xx_XX.properties file.<br>
     * Throws {@link LanguageNotSupportedException} if the requested greeting text cannot be found for requested locale.
     */
    @Override
    public String getTimeSensitiveGreeting(TimePeriod timePeriod, Locale locale) {
        
        String greeting = errorMessageGeneral;
        
        switch (timePeriod) {
            case MORNING:
                greeting = messages.getMessage(GREETING_MORNING_KEY, null, GREETING_NOT_FOUND, locale);
                break;
            case AFTERNOON:
                greeting = messages.getMessage(GREETING_AFTERNOON_KEY, null, GREETING_NOT_FOUND, locale);
                break;
            case EVENING:
                greeting = messages.getMessage(GREETING_EVENING_KEY, null, GREETING_NOT_FOUND, locale);
                break;
            case GENERAL_PURPOSE:
                greeting = messages.getMessage(GREETING_GENERAL_TIMESENSITIVE_KEY, null, GREETING_NOT_FOUND, locale);
                break;
    
            default:
                break;
        }
        
        if (GREETING_NOT_FOUND.equals(greeting)) {
            Log.warn("Specific greeting text not available (in time sensitive operation mode) for the time period: {}. Trying to find general greeting text.", timePeriod);
            greeting = messages.getMessage(GREETING_GENERAL_TIMESENSITIVE_KEY, null, GREETING_NOT_FOUND, locale);
            
            if (GREETING_NOT_FOUND.equals(greeting)) {
                // Even GREETING_GENERAL text for this locale is not found i.e. corresponding properties file for this locale is not available, throw exception
                String errorText = messages.getMessage(GREETING_ERROR_LANGUAGENOTSUPPORTED_KEY, new String[] {locale.getLanguage()}, null);
                Log.error(errorText); 
                throw new LanguageNotSupportedException(errorText);
            }
        }
        
        return greeting;
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * If the greeting text is same as the default {@code GREETING_NOT_FOUND}, then the text is not found for<br>
     * the locale's language in the messages_xx_XX.properties file.<br>
     * Throws {@link LanguageNotSupportedException} if the requested greeting text cannot be found for requested locale.
     */
    @Override
    public String getTimeInsensitiveGreeting(Locale locale) {
        
        String greeting = messages.getMessage(GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, GREETING_NOT_FOUND, locale);
        
        if (GREETING_NOT_FOUND.equals(greeting)) {
            String errorText = messages.getMessage(GREETING_ERROR_LANGUAGENOTSUPPORTED_KEY, new String[] { locale.getLanguage()}, null);
            Log.error(errorText);
            throw new LanguageNotSupportedException(errorText);
        }
        
        return greeting;
    }

}
