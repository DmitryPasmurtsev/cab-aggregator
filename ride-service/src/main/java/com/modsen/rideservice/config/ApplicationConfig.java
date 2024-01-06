package com.modsen.rideservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Locale locale() { return LocaleContextHolder.getLocale(); }

    @Bean
    public CustomMessageSource messageSource() {
        CustomMessageSource messageSource = new CustomMessageSource();
        messageSource.setBasename("classpath:LocalizedMessages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
