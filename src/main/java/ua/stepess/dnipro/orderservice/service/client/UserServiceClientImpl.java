package ua.stepess.dnipro.orderservice.service.client;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import ua.stepess.dnipro.orderservice.config.properties.UserServiceClientProperties;
import ua.stepess.dnipro.orderservice.dto.user.User;

@Service
public class UserServiceClientImpl extends AbstractBaseRestClient implements UserServiceClient {

    private static final String USER_API_PATH = "/api/v3/users/{id}";

    public UserServiceClientImpl(RestTemplateBuilder restTemplateBuilder, UserServiceClientProperties properties) {
        super(restTemplateBuilder, properties);
    }

    @Override
    public User getUserById(String id) {
        return restTemplate.getForObject(USER_API_PATH, User.class, id);
    }
}
