package ua.stepess.dnipro.orderservice.config;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.stepess.dnipro.orderservice.config.rest.LoggingRestTemplateCustomizer;

// FIXME
@Configuration
public class RestTemplateConfiguration {

   // @Bean
    public RestTemplateCustomizer loggingRestTemplateBuilder() {
        return new LoggingRestTemplateCustomizer();
    }

}
