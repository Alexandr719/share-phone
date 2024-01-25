package com.task.mobileshare.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;

@Configuration
public class ConvertersConfig {

    @Bean
    public ConversionService conversionService(@Autowired List<Converter> converterList) {
        DefaultConversionService defaultConversionService = new DefaultConversionService();
        converterList.forEach(defaultConversionService::addConverter);
        return defaultConversionService;
    }
}