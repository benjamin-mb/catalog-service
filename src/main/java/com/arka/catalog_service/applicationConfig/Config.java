package com.arka.catalog_service.applicationConfig;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = {
        "com.arka.catalog_service.domain.usecase",
        "com.arka.catalog_service.infrastructure.adapters.repository",
        "com.arka.catalog_service.infrastructure.adapters.mapper",
        "com.arka.catalog_service.infrastructure.controllers"
},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class Config {
}


