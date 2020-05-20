package ua.stepess.dnipro.orderservice.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "client.user")
public class UserServiceClientProperties extends AbstractClientProperties {
}
