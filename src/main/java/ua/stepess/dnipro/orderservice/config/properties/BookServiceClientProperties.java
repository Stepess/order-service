package ua.stepess.dnipro.orderservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client.book")
public class BookServiceClientProperties extends AbstractClientProperties {
}
