package com.webshop.config;

import com.webshop.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * Configuration class for Internationalization
 */
@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    /**
     * * @return default Locale set by the user
     */
    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        Locale locale = LocaleContextHolder.getLocale();
        slr.setDefaultLocale(Locale.ENGLISH);
        for (String lang : Constants.LANGUAGES) {
            if (locale.getLanguage().equalsIgnoreCase(lang))
                slr.setDefaultLocale(locale);
        }
//        if (locale.getLanguage().equalsIgnoreCase("nl"))
//            slr.setDefaultLocale(locale);
//        else
//            slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    /**
     * an interceptor bean that will switch to a new locale based on the value of the language parameter appended to a request:
     *
     * @param registry
     * @language should be the name of the request param i.e  localhost:8010/api/get-greeting?language=fr
     * <p>
     * Note: All requests to the backend needing Internationalization should have the "language" request param
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        registry.addInterceptor(localeChangeInterceptor);
    }
}
