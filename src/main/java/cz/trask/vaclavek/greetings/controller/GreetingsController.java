package cz.trask.vaclavek.greetings.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cz.trask.vaclavek.greetings.errors.InvalidParameterException;
import cz.trask.vaclavek.greetings.service.GreetingsService;
import cz.trask.vaclavek.greetings.service.TimePeriodService;

/**
 * 
 * Main REST controller to serve REST API calls.
 * <p>
 * It introduces two main API call paths for different modes of operation:
 * <ul>
 *   <li>1) For Time sensitive operation: /api/greeting/timesensitive</li>
 *  and
 *   <li>2) for Time insensitive operation: /api/greeting/timeinsensitive</li>
 * </ul>
 * 
 * Examples of correct REST API calls served by this controller:
 * <p>
 * <ul>
 *   <li>1) http://localhost:8080/api/greeting/timesensitive?usersTime=17:10&lang=en-US</li>
 *   <li>2) http://localhost:8080/api/greeting/timeinsensitive?lang=cs_CS</li>
 * </ul>
 * 
 * Requires two main services {@link GreetingsService} and {@link TimePeriodService}<br>
 * instancies to work properly.
 * <p>
 * @author Michal Vaclavek
 *
 */
@RestController 
@RequestMapping("/api/greeting") 
public class GreetingsController
{
    
    private static final Logger Log = LoggerFactory.getLogger(GreetingsController.class);
    
    /**
     * Instance of {@link GreetingsService} implementation
     */
    private GreetingsService greetingsService;
    
    /**
     * Instance of {@link TimePeriodService} implementation
     */
    private TimePeriodService timePeriodService;
    
    /**
     * Standard constructor injecting two main service's intancies
     * 
     * @param greetingsService - instance of {@link GreetingsService} implementation
     * @param timePeriodService - instance of {@link TimePeriodService} implementation
     */
    public GreetingsController(GreetingsService greetingsService, TimePeriodService timePeriodService) {
        super();
        this.greetingsService = greetingsService;
        this.timePeriodService = timePeriodService;
    }
    
    /**
     * Returns greeting text as {@code ResponseEntity<String>} according input parameters<br>
     * {@code usersTime} and {@code lang} for 'time sensitive' operation mode.
     * <p>
     * Examples of valid http request:<br>
     *  http://localhost:8080/api/greeting/timesensitive?usersTime=17:10&lang=en-US<br>
     *  http://localhost:8080/api/greeting/timesensitive?usersTime=05:01&lang=es<br>
     * 
     * @param usersTime - time in "HH:mm" format. Time of user for which the greeting is requested.
     * @param lang - language-country or language only abbrevation for which the greeting is requested.
     * @param locale - locale instance created by Spring according {@code lang} parameter value
     * @return requested greeting text according input parameters 'usersTime' and 'lang' value
     */
    @GetMapping("/timesensitive") 
    public ResponseEntity<String> getGreetingTimeSensitive(@RequestParam(value="usersTime", defaultValue = "")
                                                           String usersTime,
                                                           @RequestParam(value="lang", defaultValue = "")
                                                           String lang,
                                                           Locale locale) {
        
        if (locale.getLanguage().contains("xx") || lang.isEmpty()) {
            Log.error("Missing 'lang' parameter in REST request.");
            throw new InvalidParameterException("lang", lang);
        }
        if (usersTime.isEmpty()) {
            Log.error("Missing 'usersTime' parameter in REST request.");
            throw new InvalidParameterException("usersTime", usersTime);
        }
        
        String greeting = null;
        if (locale != null) {
            greeting = greetingsService.getTimeSensitiveGreeting(timePeriodService.getTimePeriod(usersTime), locale);
            Log.info("Time sensitive greeting retrieved: {}", greeting);
        }
        
        return (greeting == null) ? new ResponseEntity<String>(HttpStatus.NOT_FOUND)
                                  : new ResponseEntity<String>(greeting, HttpStatus.OK);
    }
    
    /**
     * Returns greeting text as {@code ResponseEntity<String>} according input parameter<br>
     * {@code lang} for 'time insensitive' operation mode.
     * <p>
     * Example of valid http request: http://localhost:8080/api/greeting/timeinsensitive?lang=cs_CS
     * 
     * @param lang - language-country or language only abbrevation for which the greeting is requested.
     * @param locale - locale instance created by Spring according {@code lang} parameter value
     * @return
     */
    @GetMapping("/timeinsensitive") 
    public ResponseEntity<String> getGreetingTimeInsensitive(@RequestParam(value="lang", defaultValue = "")
                                                             String lang,
                                                             Locale locale) {

        if (locale.getLanguage().contains("xx") || lang.isEmpty()) {
            Log.error("Missing 'lang' parameter in REST request.");
            throw new InvalidParameterException("lang", lang);
        }
        
        String greeting = greetingsService.getTimeInsensitiveGreeting(locale);
        Log.info("Time insensitive greeting retrieved: {}", greeting);
        
        return (greeting == null) ? new ResponseEntity<String>(HttpStatus.NOT_FOUND)
                                  : new ResponseEntity<String>(greeting, HttpStatus.OK);
    }

}
