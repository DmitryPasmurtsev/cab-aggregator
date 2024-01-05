package com.modsen.rideservice.config;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.lang.Nullable;

import java.util.Locale;

public class CustomMessageSource extends ReloadableResourceBundleMessageSource {

    public String customGetMessage(String code, @Nullable Object[] args, Locale locale) {
        try {
            return super.getMessage(code, args, locale);
        } catch (NoSuchMessageException ex) {
            return code;
        }
    }
}
