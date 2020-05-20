package ua.stepess.dnipro.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.stepess.dnipro.orderservice.service.client.BookServiceClient;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final BookServiceClient bookServiceClient;

}
