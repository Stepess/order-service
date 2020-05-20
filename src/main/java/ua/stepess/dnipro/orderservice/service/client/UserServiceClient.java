package ua.stepess.dnipro.orderservice.service.client;

import ua.stepess.dnipro.orderservice.dto.user.User;

public interface UserServiceClient {

    User getUserById(String id);

}
