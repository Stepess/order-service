package ua.stepess.dnipro.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.stepess.dnipro.orderservice.service.client.BookServiceClient;
import ua.stepess.dnipro.orderservice.service.client.UserServiceClient;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final BookServiceClient bookServiceClient;
    private final UserServiceClient userServiceClient;

}
