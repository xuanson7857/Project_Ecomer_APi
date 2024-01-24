package com.ra.service;

import com.ra.model.dto.request.OrderRequest;
import com.ra.model.entity.M5Order;
import com.ra.model.entity.M5User;
import com.ra.security.principal.M5UserPrincipal;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import com.ra.util.exception.UnAuthorizedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;


public interface OrderService {
    M5Order saveOrder(OrderRequest orderRequest, M5UserPrincipal userPrincipal) throws BadRequestException;
    List<M5Order> orderHistory(M5UserPrincipal userPrincipal);
    M5Order findOrder(M5User user,Long oid) throws NotFoundException;
    M5Order cancelOrder(M5Order order) throws UnAuthorizedException;
// admin
    Page<M5Order> orderList(Pageable pageable, String sort, String col);
    M5Order findById(Long oid) throws NotFoundException;
    M5Order confirmOrder(Long oid, String status) throws NotFoundException;

    List<M5Order> orderByStatus(String s) throws BadRequestException;
}
