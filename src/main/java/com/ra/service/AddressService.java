package com.ra.service;

import com.ra.model.entity.M5Address;
import com.ra.model.entity.M5User;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;

import java.util.List;

public interface AddressService {
    List<M5Address> userAddress(M5User user);
    M5Address save(M5Address address) throws NotFoundException, BadRequestException;

    M5Address findByUserAndID(M5User user , Long id) throws NotFoundException;

    void delete(M5User user,Long id) throws NotFoundException;
}
