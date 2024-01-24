package com.ra.service.Impl;

import com.ra.model.entity.M5Address;
import com.ra.model.entity.M5User;
import com.ra.repository.AddressRepository;
import com.ra.service.AddressService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<M5Address> userAddress(M5User user) {
        return addressRepository.findAllByUser(user);
    }

    @Override
    public M5Address save(M5Address address) throws NotFoundException, BadRequestException {
        if (address.getAddressId() != null) {
            M5Address found = findByUserAndID(address.getUser(), address.getAddressId());
            address.setAddressId(found.getAddressId());
        }
        if(addressRepository.existsByFullAddressAndPhoneAndReceiverAndUser(address.getFullAddress(), address.getPhone(), address.getReceiver(),address.getUser())){
            throw new BadRequestException("address already exist");
        }
        return addressRepository.save(address);
    }

    @Override
    public M5Address findByUserAndID(M5User user, Long id) throws NotFoundException {
        M5Address address = addressRepository.findByAddressIdAndUser(id, user).orElse(null);
        if (address == null) {
            throw new NotFoundException("Not found your address with ID" + id);
        }
        return address;
    }

    @Override
    @Transactional
    public void delete(M5User user, Long id) throws NotFoundException {
        M5Address address = findByUserAndID(user, id);
        addressRepository.deleteByAddressIdAndUser(id, user);
    }
}
