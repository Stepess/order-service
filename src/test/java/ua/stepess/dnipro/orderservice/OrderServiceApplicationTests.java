package ua.stepess.dnipro.orderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ua.stepess.dnipro.orderservice.persistence.repository.OrderRepository;

@SpringBootTest
class OrderServiceApplicationTests {

	@MockBean
	OrderRepository orderRepository;

	@Test
	void contextLoads() {
	}

}
