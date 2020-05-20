package ua.stepess.dnipro.orderservice.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.stepess.dnipro.orderservice.persistence.entity.Order;

import java.util.UUID;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, UUID> {

    Page<Order> findByUserId(String userId, Pageable pageable);

}
