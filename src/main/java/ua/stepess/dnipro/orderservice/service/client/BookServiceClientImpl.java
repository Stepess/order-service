package ua.stepess.dnipro.orderservice.service.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ua.stepess.dnipro.orderservice.config.properties.BookServiceClientProperties;
import ua.stepess.dnipro.orderservice.dto.book.BookItem;

@Service
public class BookServiceClientImpl extends AbstractBaseRestClient implements BookServiceClient {

    private static final String BOOK_API_PATH = "/api/v1/books/{id}";

    public BookServiceClientImpl(RestTemplateBuilder restTemplateBuilder, BookServiceClientProperties properties) {
        super(restTemplateBuilder, properties);
    }

    @Override
    public BookItem getItemById(String id) {
        return restTemplate.getForObject(BOOK_API_PATH, BookItem.class, id);
    }

}
