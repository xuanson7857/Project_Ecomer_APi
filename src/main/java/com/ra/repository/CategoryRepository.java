package com.ra.repository;

import com.ra.model.entity.M5Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<M5Category,Long> {
List<M5Category> findAllByStatus(boolean status);
Optional<M5Category> findByCategoryName(String name);
}
