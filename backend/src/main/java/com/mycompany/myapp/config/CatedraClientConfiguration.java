package com.mycompany.myapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CatedraClientConfiguration {

    private final ApplicationProperties applicationProperties;

    public CatedraClientConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean("catedraRestTemplate")
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        String catedraJwtToken = applicationProperties.getCatedra().getJwtToken();
        String catedraBaseUrl = applicationProperties.getCatedra().getBaseUrl();

        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + catedraJwtToken);
            return execution.execute(request, body);
        };

        return builder.rootUri(catedraBaseUrl).additionalInterceptors(interceptor).build();
    }
}
