package com.ra.repository;

import com.ra.model.OrderStatus;
import com.ra.model.entity.M5Order;
import com.ra.model.entity.M5User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<M5Order,Long> {
    List<M5Order> findAllByUser(M5User user);
    Optional<M5Order> findByUserAndOrderId(M5User user, Long oid);
    List<M5Order> findAllByStatus(OrderStatus orderStatus);

}
