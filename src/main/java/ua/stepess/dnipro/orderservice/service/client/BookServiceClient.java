package ua.stepess.dnipro.orderservice.service.client;

import ua.stepess.dnipro.orderservice.dto.book.BookItem;

public interface BookServiceClient {

    BookItem getItemById(String id);

}
