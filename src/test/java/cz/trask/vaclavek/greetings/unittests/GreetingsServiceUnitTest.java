package cz.trask.vaclavek.greetings.unittests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cz.trask.vaclavek.greetings.errors.InvalidParameterException;
import cz.trask.vaclavek.greetings.errors.LanguageNotSupportedException;
import cz.trask.vaclavek.greetings.service.GreetingsService;
import cz.trask.vaclavek.greetings.service.TimePeriodService;
import cz.trask.vaclavek.greetings.service.TimePeriodService.TimePeriod;
import cz.trask.vaclavek.greetings.serviceimpl.GreetingsServiceImpl;

/**
 * Unit tests of the {@link GreetingsService}
 * 
 * @author Michal Vaclavek
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class GreetingsServiceUnitTest
{
    @Autowired
    private GreetingsService greetingsService;
    
    /**
     * Source of the greeting texts expected during testing.
     */
    @Autowired
    private MessageSource messages;
    
    /**
     * Tests if the correct greeting text is returned according given {@code locale} and {@link TimePeriod}
     * by {@link GreetingsService#getTimeSensitiveGreeting(TimePeriod, Locale)} method
     */
    @Test
    public void givenLocale_and_timePeriod_whenTimeSensitive_thenCorrectGreetingShouldReturn() {
        
        Locale locale = new Locale("cs", "CZ");
        String greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_MORNING_KEY, null, locale);
        assertThat(greetingsService.getTimeSensitiveGreeting(TimePeriod.MORNING, locale)).isEqualTo(greetingExpected);
        
        locale = new Locale("en", "GB");
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_AFTERNOON_KEY, null, locale);
        assertThat(greetingsService.getTimeSensitiveGreeting(TimePeriod.AFTERNOON, locale)).isEqualTo(greetingExpected);
        
        locale = new Locale("en", "US");
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_EVENING_KEY, null, locale);
        assertThat(greetingsService.getTimeSensitiveGreeting(TimePeriod.EVENING, locale)).isEqualTo(greetingExpected);
        
        locale = new Locale("es", "ES");
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMESENSITIVE_KEY, null, locale);
        assertThat(greetingsService.getTimeSensitiveGreeting(TimePeriod.GENERAL_PURPOSE, locale)).isEqualTo(greetingExpected);
     }
    
    /**
     * Tests if the correct greeting text is returned according given {@code locale}
     * by {@link GreetingsService#getTimeInsensitiveGreeting(Locale)} method
     */
    @Test
    public void givenLocale_whenTimeInSensitive_thenCorrectGreetingShouldReturn() {
        
        Locale locale = new Locale("cs", "CZ");
        String greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        assertThat(greetingsService.getTimeInsensitiveGreeting(locale)).isEqualTo(greetingExpected);
        
        locale = new Locale("en", "GB");
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        assertThat(greetingsService.getTimeInsensitiveGreeting(locale)).isEqualTo(greetingExpected);
        
        locale = new Locale("en", "US");
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        assertThat(greetingsService.getTimeInsensitiveGreeting(locale)).isEqualTo(greetingExpected);
        
        locale = new Locale("es", "ES");
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        assertThat(greetingsService.getTimeInsensitiveGreeting(locale)).isEqualTo(greetingExpected);
    }
    
    /**
     * Tests if the {@link LanguageNotSupportedException} is trown, when wrong input {@code locale} is inserted into
     * {@link GreetingsService#getTimeSensitiveGreeting(TimePeriod, Locale)} method
     */
    @Test
    public void whenInValidLocale_and_timeSensitiveRequest_thenLanguageNotSupportedExceptionShouldBeThrown() {
        
        Locale notSupportedLocale = new Locale("ch", "CH");
        
        assertThatThrownBy(() -> {greetingsService.getTimeSensitiveGreeting(TimePeriod.EVENING, notSupportedLocale);}).as("LanguageNotSupportedException not thrown.")
                                 .isInstanceOf(LanguageNotSupportedException.class);
    }
    
    /**
     * Tests if the {@link LanguageNotSupportedException} is trown, when wrong input {@code locale} is inserted into
     * {@link GreetingsService#getTimeInsensitiveGreeting(Locale)} method
     */
    @Test
    public void whenInValidLocale_and_timeInSensitiveRequest_thenLanguageNotSupportedExceptionShouldBeThrown() {
        
       Locale notSupportedLocale = new Locale("xx", "XX");
        
        assertThatThrownBy(() -> {greetingsService.getTimeInsensitiveGreeting(notSupportedLocale);}).as("LanguageNotSupportedException not thrown.")
                                 .isInstanceOf(LanguageNotSupportedException.class);
     }
    
}
