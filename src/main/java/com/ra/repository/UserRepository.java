package com.ra.repository;

import com.ra.model.entity.M5User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<M5User,Long> {
    M5User findByEmailIgnoreCase(String emailId);

    Optional<M5User> findByUsername(String username);
    Page<M5User> findAllByUsernameContaining(String keyword, Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
}
