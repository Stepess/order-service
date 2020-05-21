package ua.stepess.dnipro.orderservice.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ua.stepess.dnipro.orderservice.persistence.entity.OrderEntity;

import java.util.UUID;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, UUID> {

    Page<OrderEntity> findByUserId(String userId, Pageable pageable);

}
