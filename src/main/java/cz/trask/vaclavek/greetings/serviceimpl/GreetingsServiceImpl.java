package cz.trask.vaclavek.greetings.serviceimpl;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;

import cz.trask.vaclavek.greetings.errors.LanguageNotSupportedException;
import cz.trask.vaclavek.greetings.service.GreetingsService;

import cz.trask.vaclavek.greetings.service.TimePeriodService.TimePeriod;

/**
 * Implementation of the {@link GreetinsService}
 * 
 * @see GreetingsConfiguration
 * 
 * @author Michal Vaclavek
 *
 */
@Service
public class GreetingsServiceImpl implements GreetingsService
{

    private static final Logger log = LoggerFactory.getLogger(GreetingsServiceImpl.class);
    
    /**
     * Keys of the 'messages_language_COUNTRY.properties' file items holding corresponding greeting texts<br>
     * Every language/coutry combination has its own properties file with the greetings texts in that specific language/coutry.
     * The keys are used by {@code MessageSource} instance of this {@code GreetingsServiceImpl} class
     */
    public static final String GREETING_MORNING_KEY = "greeting.timesensitive.morning";
    public static final String GREETING_AFTERNOON_KEY = "greeting.timesensitive.afternoon";
    public static final String GREETING_EVENING_KEY = "greeting.timesensitive.evening";
    public static final String GREETING_GENERAL_KEY = "greeting.timesensitive.general";
    
    public static final String GREETING_GENERAL_TIMEINSENSITIVE_KEY = "greeting.timesinensitive.general";
    
    /**
     * Key of the 'messages.properties' file item holding common error text in case that greeting for requested language
     * is not supported.
     */
    public static final String GREETING_ERROR_LANGUAGENOTSUPPORTED_KEY = "greeting.error.language.notsupported";
    
    /**
     * Key of the 'messages.properties' file item holding common error text in case that greeting text cannot be returned
    */
    public static final String GREETING_ERROR_KEY = "greeting.error";
    
    /**
     * Source of the greeting texts
     */
    private MessageSource messages;
    
    /**
     * General error message text in case that greeting text cannot be returned
     */
    private static String errorMessageGeneral;
    
    
    /**
     * Standard constructor with MessageSource instance injected.
     * 
     * @param messages source of the greeting texts
     */
    public GreetingsServiceImpl(MessageSource messages) {
       super();
       this.messages = messages;
       errorMessageGeneral = messages.getMessage(GREETING_ERROR_KEY, null, null);
    }


    /**
     * {@inheritDoc}
     * <p>
     * If the greeting text is same as the message key, then text is not found for<br>
     * the locale's language in the messages_xx_XX.properties file.<br>
     * Throws {@link LanguageNotSupportedException} if the requested greeting text cannot be found for requested locale.
     */
    @Override
    public String getTimeSensitiveGreeting(TimePeriod timePeriod, Locale locale) {
        
        String greeting = errorMessageGeneral;
        
        boolean languageAndTimeSupported = false;
        
        try {
            switch (timePeriod) {
                case MORNING:
                    greeting = messages.getMessage(GREETING_MORNING_KEY, null, locale);
                    languageAndTimeSupported = !GREETING_MORNING_KEY.equals(greeting);
                    break;
                case AFTERNOON:
                    greeting = messages.getMessage(GREETING_AFTERNOON_KEY, null, locale);
                    languageAndTimeSupported = !GREETING_AFTERNOON_KEY.equals(greeting);
                    break;
                case EVENING:
                    greeting = messages.getMessage(GREETING_EVENING_KEY, null, locale);
                    languageAndTimeSupported = !GREETING_EVENING_KEY.equals(greeting);
                    break;
                case GENERAL_PURPOSE:
                    greeting = messages.getMessage(GREETING_GENERAL_KEY, null, locale);
                    languageAndTimeSupported = !GREETING_GENERAL_KEY.equals(greeting);
                    break;
        
                default:
                    break;
        }
        } catch (NoSuchMessageException ex) {
            log.error("Greeting text not found in the messages.properties file.", ex);
        }
        
        if (!languageAndTimeSupported) {
            log.warn("Specific greeting text not available (in time sensitive operation mode) for the time period: {}. Trying to find general greeting text.", timePeriod);
            greeting = messages.getMessage(GREETING_GENERAL_KEY, null, locale);
            
            if (GREETING_GENERAL_KEY.equals(greeting)) {
                // Even GREETING_GENERAL text for this locale is not found i.e. corresponding properties file for this locale is not available, throw exception
                String errorText = messages.getMessage(GREETING_ERROR_LANGUAGENOTSUPPORTED_KEY, new String[] {locale.getLanguage()}, null);
                log.error(errorText); 
                throw new LanguageNotSupportedException(errorText);
            }
        }
        
        return greeting;
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * If the greeting text is same as the message key, then the text is not found for<br>
     * the locale's language in the messages_xx_XX.properties file.<br>
     * Throws {@link LanguageNotSupportedException} if the requested greeting text cannot be found for requested locale.
     */
    @Override
    public String getTimeInsensitiveGreeting(Locale locale) {
        String greeting = errorMessageGeneral;
        boolean languageSupported = false;
        
        try {
            greeting = messages.getMessage(GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
            // if the greeting text is same as the message key, then text is not found for the locale's language
            languageSupported = !GREETING_GENERAL_TIMEINSENSITIVE_KEY.equals(greeting);
        } catch  (NoSuchMessageException ex) {
            log.error("Greeting text not found in the messages.properties file.", ex);
        }
        
        if (!languageSupported) {
            
            log.warn("Greeting text not available for time insensitive operation mode.");
            greeting = messages.getMessage(GREETING_GENERAL_KEY, null, locale);
            
            if (GREETING_GENERAL_KEY.equals(greeting)) {
                // Even GREETING_GENERAL text for this locale is not found i.e. corresponding properties file for this locale is not available, throw exception
                String errorText = messages.getMessage(GREETING_ERROR_LANGUAGENOTSUPPORTED_KEY, new String[] { locale.getLanguage()}, null);
                log.error(errorText);
                throw new LanguageNotSupportedException(errorText);
            }
        }
        
        return greeting;
    }

}
