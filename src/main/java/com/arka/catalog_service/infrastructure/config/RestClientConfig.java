package com.arka.catalog_service.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Value("${exchangerate.api.base-url:https://v6.exchangerate-api.com/v6}")
    private String baseUrl;

    @Value("${exchangerate.api.timeout:60}")
    private int timeoutSeconds;

    @Bean
    public RestClient restClient(){
        JdkClientHttpRequestFactory requestFactory= new JdkClientHttpRequestFactory();
        requestFactory.setReadTimeout(Duration.ofSeconds(timeoutSeconds));

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultStatusHandler(
                        responseStatusCode -> responseStatusCode.is4xxClientError() ||
                                responseStatusCode.is5xxServerError(),
                        (request, response) -> {
                            throw new RuntimeException("Error calling currency API: " + response.getStatusCode());
                        }
                )

                .build();
    }

}
