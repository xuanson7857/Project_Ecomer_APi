package com.ra.service.Impl;

import com.ra.model.OrderStatus;
import com.ra.model.dto.request.OrderRequest;
import com.ra.model.entity.*;
import com.ra.repository.AddressRepository;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.OrderRepository;
import com.ra.repository.ProductRepository;
import com.ra.security.principal.M5UserPrincipal;
import com.ra.service.CartService;
import com.ra.service.OrderService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import com.ra.util.exception.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CartService cartService;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderDetailRepository orderDetailRepository,
                            CartService cartService,
                            AddressRepository addressRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public M5Order saveOrder(OrderRequest orderRequest, M5UserPrincipal userPrincipal) throws BadRequestException {
        M5ShoppingCart cart = cartService.cart(userPrincipal);

        if (cart.getCartItems().isEmpty()) {
            throw new BadRequestException("cart is empty");
        }
        M5Address address = address(orderRequest, userPrincipal.getUser());
        if (address == null) {
            throw new BadRequestException("address is empty");
        }
        M5Order order = order(address, cart);
        orderDetail(cart, order);
        return orderRepository.save(order);
    }

    @Override
    public List<M5Order> orderHistory(M5UserPrincipal userPrincipal) {
        orderRepository.findAll(Sort.by("status").ascending());
        return orderRepository.findAllByUser(userPrincipal.getUser());
    }

    @Override
    public M5Order findOrder(M5User user, Long oid) throws NotFoundException {
        M5Order order = orderRepository.findByUserAndOrderId(user, oid).orElse(null);
        if (order == null) {
            throw new NotFoundException("Not found your order with ID " + oid);
        }
        return order;
    }

    @Override
    @Transactional
    public M5Order cancelOrder(M5Order order) throws UnAuthorizedException {
        if (!order.getStatus().equals(OrderStatus.PROCESSING)) {
            throw new UnAuthorizedException("Your order has been processed");
        }
        order.setStatus(OrderStatus.CANCELED);
        orderDetailRepository.deleteAll(order.getOrderDetails());
        return orderRepository.save(order);
    }

    private M5Address address(OrderRequest orderRequest, M5User user) {
        M5Address address;
        if (orderRequest.getAddressId() != null) {
            address = addressRepository.findByAddressIdAndUser(orderRequest.getAddressId(), user).orElse(null);
            if (address != null) {
                return address;
            }
        }
        if (orderRequest.getPhone().isEmpty() || orderRequest.getReceiver().isEmpty() || orderRequest.getFullAddress().isEmpty()) {
            return null;
        }
        address = M5Address.builder()
                .user(user)
                .fullAddress(orderRequest.getFullAddress())
                .phone(orderRequest.getPhone())
                .receiver(orderRequest.getReceiver())
                .build();
        if (!addressRepository.existsByFullAddressAndPhoneAndReceiverAndUser(address.getFullAddress(), address.getPhone(), address.getReceiver(), user)) {
            return addressRepository.save(address);
        }
        return address;
    }

    private M5Order order(M5Address address, M5ShoppingCart shoppingCart) {
        Date date = new Date(new java.util.Date().getTime());
        Date rDate = new Date(new java.util.Date().getTime() + 86400000 * 6);

        M5Order order = M5Order.builder()
                .receiver(address.getReceiver())
                .receiverPhone(address.getPhone())
                .receiverAddress(address.getFullAddress())
                .createAt(date)
                .receiveAt(rDate)
                .status(OrderStatus.PROCESSING)
                .user(shoppingCart.getUser())
                .build();
        return orderRepository.save(order);
    }

    private void orderDetail(M5ShoppingCart cart, M5Order order) {
        double t = 0;
        Set<M5OrderDetail> orderDetails = new HashSet<>();
        for (M5ShopCartItem cartItem : cart.getCartItems()) {
            double s = cartItem.getProduct().getUnitPrice() * cartItem.getQuantity();
            t = t + s;
            orderDetails.add(
                    M5OrderDetail.builder()
                            .order(order)
                            .orderQuantity(cartItem.getQuantity())
                            .product(cartItem.getProduct())
                            .unitPrice(s)
                            .build()
            );
        }
        orderDetailRepository.saveAll(orderDetails);
        cartService.deleteCart(cart);
        order.setTotalPrice(t);
        order.setOrderDetails(orderDetails);
    }

    // admin
    @Override
    public Page<M5Order> orderList(Pageable pageable, String sort, String col) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(col));
        if (sort.equalsIgnoreCase("des")) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(col).descending());
        }
        return orderRepository.findAll(pageable);
    }

    @Override
    public M5Order findById(Long oid) throws NotFoundException {
        M5Order order = orderRepository.findById(oid).orElse(null);
        if (order == null) {
            throw new NotFoundException("Not found order with ID " + oid);
        }
        return order;
    }

    @Override
    public M5Order confirmOrder(Long oid, String status) throws NotFoundException {
        M5Order order = orderRepository.findById(oid).orElse(null);
//
        if (order != null) {
            if (order.getStatus().equals(OrderStatus.PROCESSING)) {
                if (status.equals("CONFIRM")) {
                    Set<M5OrderDetail> orderDetails = order.getOrderDetails();
                    updateProduct(orderDetails);
                    order.setStatus(OrderStatus.CONFIRMED);
                }
                if (status.equals("DENIED")) {
                    order.setStatus(OrderStatus.DENIED);
                }
                return orderRepository.save(order);
            }
            if (order.getStatus().equals(OrderStatus.CONFIRMED)) {
                if (status.equals("DELIVERING")) {
                    order.setStatus(OrderStatus.DELIVERING);
                }
                if (status.equals("DELIVERED")) {
                    order.setStatus(OrderStatus.DELIVERED);
                }
            }
            return orderRepository.save(order);
        }
        throw new NotFoundException("Not found order with ID " + oid);
    }

    private void updateProduct(Set<M5OrderDetail> orderDetails) {
        for (M5OrderDetail orderDetail : orderDetails) {
            M5Product product = orderDetail.getProduct();
            product.setStockQuantity(product.getStockQuantity() - orderDetail.getOrderQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public List<M5Order> orderByStatus(String s) throws BadRequestException {
        switch (s.toUpperCase()) {
//            PROCESSING,CANCELED,DELIVERING,DELIVERED,DENIED,CONFIRMED
            case "PROCESSING":
                return orderRepository.findAllByStatus(OrderStatus.PROCESSING);
            case "CANCELED":
                return orderRepository.findAllByStatus(OrderStatus.CANCELED);

            case "DELIVERING":
                return orderRepository.findAllByStatus(OrderStatus.DELIVERING);

            case "DELIVERED":
                return orderRepository.findAllByStatus(OrderStatus.DELIVERED);

            case "DENIED":
                return orderRepository.findAllByStatus(OrderStatus.DENIED);

            case "CONFIRMED":
                return orderRepository.findAllByStatus(OrderStatus.CONFIRMED);

        }
        throw new BadRequestException("Order has no status " + s);
    }
}
