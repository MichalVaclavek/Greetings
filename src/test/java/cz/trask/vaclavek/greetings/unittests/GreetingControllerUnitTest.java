package cz.trask.vaclavek.greetings.unittests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;

import cz.trask.vaclavek.greetings.controller.GreetingsController;
import cz.trask.vaclavek.greetings.service.GreetingsService;
import cz.trask.vaclavek.greetings.service.TimePeriodService.TimePeriod;
import cz.trask.vaclavek.greetings.service.TimePeriodService;
import cz.trask.vaclavek.greetings.serviceimpl.GreetingsServiceImpl;

/**
 * Unit tests of the {@link GreetingsController}
 * 
 * Not done properly as the SpringBoot test context needs to be loaded (using @SpringBootTest)
 * to get MessageSource injected.
 * 
 * @author Michal Vaclavek
 *
 */
@SpringBootTest
public class GreetingControllerUnitTest
{
    
    @Mock
    private GreetingsService greetingsService;
    
    @Mock
    private TimePeriodService timePeriodService;
    
    /**
     * Source of the greeting texts expected during testing.
     */
    @Autowired
    private MessageSource messages;
    
    @InjectMocks
    GreetingsController greetingsController;
    
    
    /**
     * Tests if the correct greeting text is returned according given user's time, language code and {@code locale} 
     * by {@link GreetingsController#getGreetingTimeSensitive(String, String, locale)} method
     */
    @Test
    public void givenLocale_and_usersTime_whenTimeSensitive_thenCorrectGreetingShouldReturn() throws Exception {
        
        // czech, CS, user's time 05:01, Morning greeting expected
        Locale locale = new Locale("cs", "CS");
        
        String greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_MORNING_KEY, null, locale);
        given(timePeriodService.getTimePeriod(Mockito.contains("05:01"))).willReturn(TimePeriod.MORNING);
        given(greetingsService.getTimeSensitiveGreeting(Mockito.eq(TimePeriod.MORNING), Mockito.eq(locale))).willReturn(greetingExpected);
        
        ResponseEntity<String> responseEntity = greetingsController.getGreetingTimeSensitive("05:01", "cs", locale);
        
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
        
        // english, GB, user's time 18:01, Evening greeting expected
        locale = new Locale("en", "GB");
        
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_EVENING_KEY, null, locale);
        given(timePeriodService.getTimePeriod(Mockito.contains("18:01"))).willReturn(TimePeriod.EVENING);
        given(greetingsService.getTimeSensitiveGreeting(Mockito.eq(TimePeriod.EVENING), Mockito.eq(locale))).willReturn(greetingExpected);
        
        responseEntity = greetingsController.getGreetingTimeSensitive("18:01", "en", locale);
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
        
        // spanish, ES, user's time 05:01, but the Morning greeting is not available, General greeting expected
        locale = new Locale("es", "ES");
        
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        given(timePeriodService.getTimePeriod(Mockito.contains("05:01"))).willReturn(TimePeriod.GENERAL_PURPOSE);
        given(greetingsService.getTimeSensitiveGreeting(Mockito.eq(TimePeriod.GENERAL_PURPOSE), Mockito.eq(locale))).willReturn(greetingExpected);
        
        responseEntity = greetingsController.getGreetingTimeSensitive("05:01", "es", locale);
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
    }
    
    /**
     * Tests if the correct greeting text is returned according given language code and {@code locale} 
     * by {@link GreetingsController#getGreetingTimeInsensitive(String, locale)} method
     */
    @Test
    public void givenLocale_and_usersTime_whenTimeInSensitive_thenCorrectGreetingShouldReturn() throws Exception {
        
        // spanish, ES, General time insensitive greeting expected
        Locale locale = new Locale("es", "ES");
        
        String greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        given(greetingsService.getTimeInsensitiveGreeting(Mockito.eq(locale))).willReturn(greetingExpected);
        
        ResponseEntity<String> responseEntity = greetingsController.getGreetingTimeInsensitive("es", locale);
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
        
        // english, US, General time insensitive greeting expected
        locale = new Locale("en", "US");
        
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        given(greetingsService.getTimeInsensitiveGreeting(Mockito.eq(locale))).willReturn(greetingExpected);
        
        responseEntity = greetingsController.getGreetingTimeInsensitive("en", locale);
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
    }
    
}
