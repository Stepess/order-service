package ua.stepess.dnipro.orderservice.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;

@Data
@Validated
@ConfigurationProperties(prefix = "client.book")
public class BookServiceClientProperties {
    private String baseUrl;
    private Duration connectionTimeout;
    private Duration socketTimeout;
}
