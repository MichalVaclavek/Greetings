package cz.trask.vaclavek.greetings.integrattests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import cz.trask.vaclavek.greetings.errors.ApiError;
import cz.trask.vaclavek.greetings.serviceimpl.GreetingsServiceImpl;

/**
 * Integration test class for Greetings REST service.
 * <p>
 * Requires to customize RestTemplate class instance {@code testRestTemplate}<br>
 * to allow correct parsing of datetime field of the {@link ApiError} class<br>
 * when testing error situation.
 * 
 * @author Michal Vaclavek
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GreetingsControllerIntegrationTest
{
    @LocalServerPort
    private int port;
    
    /**
     * Source of the greeting texts expected during testing.
     */
    @Autowired
    private MessageSource messages;
    
    @Autowired
    private RestTemplate testRestTemplate;
    
    /**
     * Needed to insert RestTemplateCustomizer into testing {@code testRestTemplate} instance
     */
    @TestConfiguration
    static class GreetingsControllerIntegrationTestConfiguration {
        
        @Bean
        public RestTemplate getRestTemplate(RestTemplateBuilder restTemplateBuilder) {
            
            return restTemplateBuilder.build();
        }  
        
        @Bean
        public CustomRestTemplateCustomizer customRestTemplateCustomizer() 
        {
            return new CustomRestTemplateCustomizer();
        }
        
        @Bean
        @DependsOn(value = {"customRestTemplateCustomizer"})
        public RestTemplateBuilder restTemplateBuilder() 
        {
            return new RestTemplateBuilder(customRestTemplateCustomizer());
        }
    }
    
    /**
     * Customizer of the RestTempate.<br>
     * Needed to correct parsing of the JSON datetime field of the {@link ApiError} class,<br>
     * when testing error situation.
     */
    @TestConfiguration
    static class CustomRestTemplateCustomizer implements RestTemplateCustomizer {
        
        @Override
        public void customize(RestTemplate restTemplate) 
        {
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
            jsonMessageConverter.setObjectMapper(objectMapper);
            messageConverters.add(jsonMessageConverter);
        }
    }
    
    
    /**
     * Tests of the correct situations in time sensitive operation mode - i.e. correct lang and user's time parameters are sent to REST api
     * and corresponding greeting is checked.
     * 
     * @throws Exception
     */
    @Test
    public void givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned() throws Exception {
        
        // Test config. n. 1
        // en-US and 18:36 user's time - expects correct Evening greeting in US english
        String lang = "en";
        String country = "US";
        String usersTime = "18:36"; 
       
        givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned(lang, country, usersTime, GreetingsServiceImpl.GREETING_EVENING_KEY);
           
       
        // Test conffig. n. 2
        // cs-CS and 05:01 user's time - expects correct Morning greeting in czech
        lang = "cs";
        country = "CS";
        usersTime = "05:01"; 
      
        givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned(lang, country, usersTime, GreetingsServiceImpl.GREETING_MORNING_KEY);
      
        // Test config. n. 3
        // cs-CS and 23:23 user's time - expects correct general greeting in czech
        lang = "cs";
        country = "CS";
        usersTime = "23:23"; 
         
        givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned(lang, country, usersTime, GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY);
         
        // Test config. n. 4
        // 'es' language and 12:01 user's time - expects correct Afternoon greeting in spanish
        lang = "es";
        country = "";
        usersTime = "12:01"; 
         
        givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned(lang, country, usersTime, GreetingsServiceImpl.GREETING_AFTERNOON_KEY);
        
        // Test config. n. 5
        // 'es-ES' language-COUNTRY and 18:00 user's time - expects correct Evenning greeting in spanish
        lang = "es";
        country = "ES";
        usersTime = "18:00"; 
        
        givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned(lang, country, usersTime, GreetingsServiceImpl.GREETING_EVENING_KEY);
     }
    
    /**
     * Helper method to gather repeated code of different test configurations, when time sensitive operation is requested.<br>
     * expectedGreetingKey is the key within messages_lang_COUNTRY.properties file where the corresponding greeting is stored.<br>
     * Used only by {@link #givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned()} method. 
     * <p>
     * Test passes, if the string returned by REST api call is the same as expected text obtained from 'messages_lang_COUNTRY.properties' file<br>
     * The respective 'messages_lang_COUNTRY.properties' file is determined according locale (created from lang and country parameters) by Spring framework. 
     * 
     * @param lang - language abbreavation i.e. 'cs' , 'en' .. etc
     * @param country - country abbreavation i.e. 'CS', 'US' etc
     * @param usersTime - time of user requesting greeting
     * @param expectedGreetingKey - messages.properties file key of the expected greeting text
     */
    private void givenLang_and_usersTime_whenTimeSensitive_thenCorrectGreetingIsReturned(String lang, String country, String usersTime, final String expectedGreetingKey) {
        
        String baseURL = "http://localhost:" + port + "/api/greeting/timesensitive";
        URI targetUrl= UriComponentsBuilder.fromUriString(baseURL)
                                           .queryParam("usersTime", usersTime) 
                                           .queryParam("lang", lang + "-" + country)                               
                                           .build()                                                
                                           .encode()                                               
                                           .toUri();
        
        
        Locale locale = new Locale(lang, country);
        String greetingExpected = messages.getMessage(expectedGreetingKey, null, locale);
       
        ResponseEntity<String> response = testRestTemplate.getForEntity(targetUrl, String.class);
       
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(greetingExpected);
    }
    
    /**
     * Tests of the correct situations in time insensitive operation - i.e. correct lang is sent to REST api
     * and corresponding greeting is checked.
     * 
     * @throws Exception
     */
    @Test
    public void givenLang_whenTimeInSensitive_thenCorrectGreetingIsReturned() throws Exception {
        
        // Test config. n. 1
        // en-GB - expects correct greeting in GB english
        String lang = "en";
        String country = "GB";
        
        givenLang_and_usersTime_whenTimeInSensitive_thenCorrectGreetingIsReturned(lang, country, GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY);
       
        // Test config. n. 2
        // cs-CS - expects correct greeting in spanish
        lang = "es";
        country = "ES";
      
        givenLang_and_usersTime_whenTimeInSensitive_thenCorrectGreetingIsReturned(lang, country, GreetingsServiceImpl.GREETING_GENERAL_TIMEINSENSITIVE_KEY);
    } 
    
    /**
     * Helper method to gather repeated code of different test configurations, when time insensitive operation is requested.<br>
     * {@code expectedGreetingKey} is the key within messages_lang_COUNTRY.properties file where the corresponding greeting is stored<br>
     * Used only by {@link #givenLang_whenTimeInSensitive_thenCorrectGreetingIsReturned()} method. 
     * <p>
     * Test passes, if the string returned by REST api call is the same as expected text obtained from 'messages_lang_COUNTRY.properties' file
     * The respective 'messages_lang_COUNTRY.properties' file is determined according locale (created from lang and country parameters) by Spring framework. 
     * 
     * @param lang - language abbreavation i.e. 'cs' , 'en' .. etc
     * @param country - country abbreavation i.e. 'CS', 'US' etc
     * @param expectedGreetingKey - messages_lang_COUNTRY.properties file key of the expected greeting text
     */
    private void givenLang_and_usersTime_whenTimeInSensitive_thenCorrectGreetingIsReturned(String lang, String country, final String expectedGreetingKey) {
        
        String baseURL = "http://localhost:" + port + "/api/greeting/timeinsensitive";
        URI targetUrl= UriComponentsBuilder.fromUriString(baseURL)
                                           .queryParam("lang", lang + "-" + country)                               
                                           .build()                                                
                                           .encode()                                               
                                           .toUri();
        
        
        Locale locale = new Locale(lang, country);
        String greetingExpected = messages.getMessage(expectedGreetingKey, null, locale);
       
        ResponseEntity<String> response = testRestTemplate.getForEntity(targetUrl, String.class);
       
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(greetingExpected);
    }
    
    
    /**
     * Testing of the error situation, missing lang parameter of the time sensitive operation
     */
    @Test
    public void missingLang_whenTimeSensitive_thenExpectedErrorIsReturned() {
        
        // Test n. 1 - time sensitive 
        String usersTime = "18:36"; 
        String baseURL = "http://localhost:" + port + "/api/greeting/timesensitive";
        URI targetUrl= UriComponentsBuilder.fromUriString(baseURL)
                                           .queryParam("usersTime", usersTime) 
                                           .build()                                                
                                           .encode()                                               
                                           .toUri();
        
        String errorExpected = "Invalid value '' or missing parameter 'lang'." ;
       
        ResponseEntity<ApiError> errorResponse = null;
        try
        {
           errorResponse = testRestTemplate.getForEntity(targetUrl, ApiError.class);
           fail("HttpClientErrorException because of LanguageNotSupportedException"); 
        }
        catch(HttpClientErrorException ex) 
        {
           //Verify bad request 
           assertThat(ex.getRawStatusCode()).isEqualTo(400);
           assertThat(ex.getResponseBodyAsString().contains(errorExpected)).isEqualTo(true);
        }
    }
    
    /**
     * Testing of the error situation, missing lang parameter of the time insensitive operation
     */
    @Test
    public void missingLang_whenTimeInSensitive_thenExpectedErrorIsReturned() {
        
       // Test n. 1 - time insensitive 
       String baseURL = "http://localhost:" + port + "/api/greeting/timeinsensitive";
       URI targetUrl= UriComponentsBuilder.fromUriString(baseURL)
                                          .build()                                                
                                          .encode()                                               
                                          .toUri();
       
      String errorExpected = "Invalid value '' or missing parameter 'lang'." ;
      
      ResponseEntity<ApiError> errorResponse = null;
      try
      {
          errorResponse = testRestTemplate.getForEntity(targetUrl, ApiError.class);
          fail("HttpClientErrorException because of LanguageNotSupportedException"); 
      }
      catch(HttpClientErrorException ex) 
      {
          //Verify bad request 
          assertThat(ex.getRawStatusCode()).isEqualTo(400);
          assertThat(ex.getResponseBodyAsString().contains(errorExpected)).isEqualTo(true);
      }
    }
    
}
