package com.ra.repository;

import com.ra.model.entity.M5Address;
import com.ra.model.entity.M5User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<M5Address,Long> {
    Optional<M5Address> findByAddressIdAndUser(Long id, M5User user);
    List<M5Address> findAllByUser(M5User user);
    Boolean existsByFullAddressAndPhoneAndReceiverAndUser(String address, String phone , String name,M5User user);

    void deleteByAddressIdAndUser(Long id,M5User user);

}
