package cz.trask.vaclavek.greetings.configuration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * Basic Spring {@code WebMvcConfigurer} configuration implementation of the application.
 * <p>
 * It configures especialy default locale {@code new Locale("xx", "XX")} to detect,
 * if the {@code 'lang'} parameter from REST request is included. If it is not, then the default
 * locale is created by Spring.
 * <p>
 * It also defines "{@code lang}" string as the parameter name to resolve locale, when serving REST requests.
 * <p>
 * Also defines {@code MessageSource} i.e. properties files for every allowed locale (i.e. language_country combination),
 * which stores the greetings for the respective locale.<br>
 * The properties Files are stored in the {@code /src/main/resources} directory of the application and their name compose
 * like: {@code 'messages_lang_COUNTRY.properties'}, where {@code lang} is the language abbrevation and {@code COUNTRY} is
 * the country abbrevation, for example {@code 'messages_en_GB.properties'}.
 * <p>
 * The Service expects the api REST calls including lang parameter, which can consists of both language and COUNTRY abbrevations,
 * connected by '-' or '_' characters (for example 'en-US' or 'cs_CS'), but it can contain only language abbrevation, too.
 * So, if the greetings for specific new language and COUNTRY should be introduced, then corresponding properties file
 * has to be created and filled by desired greetings.<br>
 * If it is expected, that the lang parameter of the API request can contain only language abbrevation (for example lang=es),
 * then corresponding properties file has to be created in form {@code 'messages_language.properties'}, for example
 * {@code 'messages_es.properties'}.
 * 
 * @author Michal Vaclavek
 *
 */
@Configuration
public class GreetingsConfiguration implements WebMvcConfigurer
{
    /**
     * Implementation of LocaleResolver interface to define default locale.
     * 
     * @return LocaleResolver interface implementation with default locale set to {@code Locale("xx", "XX")}
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        
        // Set default locale to any unsopported language
        // Used to detect, if the REST request contains correct lang parameter
        // If the locale of the Controller's methods contain this default language,
        // then it indictes that the REST request did not contain valid lang parameter
        sessionLocaleResolver.setDefaultLocale(new Locale("xx", "XX"));
        return sessionLocaleResolver;
    }

    /**
     * Returns LocaleChangeInterceptor 
     * 
     * @return LocaleChangeInterceptor with 'lang' parameter name to detect locale settings in api REST request
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry ir) {
        ir.addInterceptor(localeChangeInterceptor());
    } 
    
    /**
     * Defines {@code MessageSource} i.e. properties files for every allowed locale (i.e. language_country combination),
     * which stores the greetings for the respective locale.
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
    
}
