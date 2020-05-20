package ua.stepess.dnipro.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ua.stepess.dnipro.orderservice.config.properties.BookServiceClientProperties;
import ua.stepess.dnipro.orderservice.dto.book.BookItem;

@Slf4j
@Service
public class BookServiceClientImpl implements BookServiceClient {

    private static final String BOOK_API_PATH = "/api/v1/books/{id}";

    private final RestTemplate restTemplate;

    public BookServiceClientImpl(RestTemplateBuilder restTemplateBuilder, BookServiceClientProperties properties) {
        this.restTemplate = restTemplateBuilder
                .rootUri(properties.getBaseUrl())
                .setReadTimeout(properties.getSocketTimeout())
                .setConnectTimeout(properties.getConnectionTimeout())
                .build();
    }

    @Override
    public BookItem getItemById(String id) {
        log.debug("Fetching book item: id [{}]", id);
        return restTemplate.getForObject(BOOK_API_PATH, BookItem.class, id);
    }

}
