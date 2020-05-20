package ua.stepess.dnipro.orderservice.service.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import ua.stepess.dnipro.orderservice.config.properties.AbstractClientProperties;

public abstract class AbstractBaseRestClient {

    protected final RestTemplate restTemplate;

    public AbstractBaseRestClient(RestTemplateBuilder restTemplateBuilder, AbstractClientProperties properties) {
        this.restTemplate = restTemplateBuilder
                .rootUri(properties.getRootUrl())
                .setReadTimeout(properties.getSocketTimeout())
                .setConnectTimeout(properties.getConnectionTimeout())
                .build();
    }

}
