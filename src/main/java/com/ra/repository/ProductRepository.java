package com.ra.repository;

import com.ra.model.entity.M5Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<M5Product,Long> {
 Page<M5Product> findAllByProductNameContainingOrDescriptionContaining(String name, String des,Pageable pageable);
 Page<M5Product> findAllByCategoryStatus(Boolean b, Pageable pageable);
 Page<M5Product> findAllByCategoryCategoryId(Long cateId,Pageable pageable);



}
