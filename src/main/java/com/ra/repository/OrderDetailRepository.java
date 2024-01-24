package com.ra.repository;

import com.ra.model.entity.M5Order;
import com.ra.model.entity.M5OrderDetail;
import com.ra.model.entity.M5Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<M5OrderDetail, Long> {
    List<M5OrderDetail> findAllByProduct(M5Product product);

}
