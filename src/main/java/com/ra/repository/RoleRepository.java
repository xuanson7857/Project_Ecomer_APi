package com.ra.repository;

import com.ra.model.entity.M5Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<M5Role,Long> {
    Optional<M5Role> getByRoleName(String name);
}
