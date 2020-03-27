package cz.trask.vaclavek.greetings.service;

import java.util.Locale;

import cz.trask.vaclavek.greetings.errors.LanguageNotSupportedException;
import cz.trask.vaclavek.greetings.service.TimePeriodService.TimePeriod;

/**
 * Interface to declare methods needed to be implemented by any GreetingsService implemntation.<br>
 * There are two main methods, one for 'time sensitive'<br>
 * and second one for 'time insensitive' modes of operation.<br>
 * Both methods return greeting text according {@code locale} and/or {@code TimePeriod} given<br>
 * as iput parameters. 
 * 
 * @author Michal Vaclavek
 *
 */
public interface GreetingsService
{
    /**
     * Returns greeting text according locale parameter for time insensitive operation mode.<br>
     * Can throw {@link LanguageNotSupportedException} if the requested greeting text cannot<br>
     * be determined for given locale.
     * 
     * @param locale - parameter to determine returned greeting according locale's language and/or country
     * @return greeting text according input language and/or country
     */
    String getTimeInsensitiveGreeting(Locale locale);
    
    /**
     * Returns greeting text according locale and {@link TimePeriod} parameters for time sensitive operation mode.<br>
     * Can throw {@link LanguageNotSupportedException} if the requested greeting text cannot<br> 
     * be determined for given locale.
     * 
     * @param locale - parameter to determine returned greeting according locale's language and/or country
     * @param timePeriod - parameter to determine returned greeting according given time period of the day
     * @return greeting text according input language and/or country and time period of the day
     */
    String getTimeSensitiveGreeting(TimePeriod timePeriod, Locale locale);
}
