package ua.stepess.dnipro.orderservice.config.rest;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.web.client.RestTemplate;

public class LoggingRestTemplateCustomizer implements RestTemplateCustomizer {

    @Override
    public void customize(RestTemplate restTemplate) {
        restTemplate.getInterceptors().add(new BufferingLoggingRestTemplateInterceptor());
    }

}
