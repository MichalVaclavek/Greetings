package cz.trask.vaclavek.greetings.unittests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import cz.trask.vaclavek.greetings.controller.GreetingsController;
import cz.trask.vaclavek.greetings.service.GreetingsService;
import cz.trask.vaclavek.greetings.service.TimePeriodService.TimePeriod;
import cz.trask.vaclavek.greetings.service.TimePeriodService;
import cz.trask.vaclavek.greetings.serviceimpl.GreetingsServiceImpl;

/**
 * Unit tests of the {@link GreetingsController}
 * 
 * @author Michal Vaclavek
 *
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
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
    
    
    
    @Test
    public void givenLocale_and_usersTime_whenTimeSensitive_thenCorrectGreetingShouldReturn() throws Exception {
        
        // czech, CS, user's time 05:01, evening greeting expected
        Locale locale = new Locale("cs", "CS");
        
        String greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_MORNING_KEY, null, locale);
        when(timePeriodService.getTimePeriod(Mockito.contains("05:01"))).thenReturn(TimePeriod.MORNING);
        when(greetingsService.getTimeSensitiveGreeting(Mockito.eq(TimePeriod.MORNING), Mockito.eq(locale))).thenReturn(greetingExpected);
        
        ResponseEntity<String> responseEntity = greetingsController.getGreetingTimeSensitive("05:01", "cs", locale);
        
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
        
        // english, GB, user's time 18:01, evening greeting expected
        locale = new Locale("en", "GB");
        
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_EVENING_KEY, null, locale);
        when(timePeriodService.getTimePeriod(Mockito.contains("18:01"))).thenReturn(TimePeriod.EVENING);
        when(greetingsService.getTimeSensitiveGreeting(Mockito.eq(TimePeriod.EVENING), Mockito.eq(locale))).thenReturn(greetingExpected);
        
        responseEntity = greetingsController.getGreetingTimeSensitive("18:01", "en", locale);
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
    }
    
    @Test
    public void givenLocale_and_usersTime_whenTimeInSensitive_thenCorrectGreetingShouldReturn() throws Exception {
        
        // spanish, ES, general time insensitive greeting expected
        Locale locale = new Locale("es", "ES");
        
        String greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        when(greetingsService.getTimeInsensitiveGreeting(Mockito.eq(locale))).thenReturn(greetingExpected);
        
        ResponseEntity<String> responseEntity = greetingsController.getGreetingTimeInsensitive("es", locale);
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
        
        // english, US, general time insensitive greeting expected
        locale = new Locale("en", "US");
        
        greetingExpected = messages.getMessage(GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY, null, locale);
        when(greetingsService.getTimeInsensitiveGreeting(Mockito.eq(locale))).thenReturn(greetingExpected);
        
        responseEntity = greetingsController.getGreetingTimeInsensitive("en", locale);
        
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().toString()).isEqualTo(greetingExpected);
    }
    
}
