package com.ra.controller;

import com.ra.model.dto.request.AccountRequest;
import com.ra.model.dto.request.CartItemRequest;
import com.ra.model.dto.request.OrderRequest;
import com.ra.model.dto.response.CartItemResponse;
import com.ra.model.entity.M5Address;
import com.ra.model.entity.M5Order;
import com.ra.model.entity.M5ShoppingCart;
import com.ra.security.principal.M5UserPrincipal;
import com.ra.service.AddressService;
import com.ra.service.CartService;
import com.ra.service.OrderService;
import com.ra.service.UserService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import com.ra.util.exception.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/user")
public class UserController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;
    private final AddressService addressService;

    @Autowired
    public UserController(CartService cartService,
                          OrderService orderService,
                          UserService userService,
                          AddressService addressService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
        this.addressService = addressService;
    }

    //    cart
    @GetMapping("/shopping-cart")
    public ResponseEntity<?> userCart() throws UnAuthorizedException {
        return new ResponseEntity<>(cartService.cart(userPrincipal()), HttpStatus.OK);

    }

    @PostMapping("/shopping-cart")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest cartItemRequest) throws Exception {

        M5ShoppingCart shoppingCart = cartService.cart(userPrincipal());

        CartItemResponse cartItemResponse = cartService.saveToCart(shoppingCart, cartItemRequest);
        return new ResponseEntity<>(cartItemResponse, HttpStatus.OK);
    }


    @PutMapping("/shopping-cart/{itemId}")
    public ResponseEntity<?> updateCart(@PathVariable("itemId") Long itemId,
                                        @RequestBody CartItemRequest cartItemRequest) throws NotFoundException, BadRequestException, UnAuthorizedException {
        M5ShoppingCart cart = cartService.cart(userPrincipal());
        cartItemRequest.setId(itemId);
        return new ResponseEntity<>(cartService.updateCart(cartItemRequest, cart), HttpStatus.OK);
    }

    @DeleteMapping("/shopping-cart/{itemId}")
    public ResponseEntity<?> removeItemCart(@PathVariable("itemId") Long itemId) throws NotFoundException, UnAuthorizedException {
        M5ShoppingCart cart = cartService.cart(userPrincipal());
        cartService.deleteCartItem(itemId, cart);
        return new ResponseEntity<>("delete success", HttpStatus.OK);

    }

    @DeleteMapping("/shopping-cart")
    public ResponseEntity<?> removeAllCartItem() throws UnAuthorizedException {
        M5ShoppingCart shoppingCart = cartService.cart(userPrincipal());
        cartService.deleteCart(shoppingCart);
        return new ResponseEntity<>("Delete successfully !", HttpStatus.OK);
    }

    @PostMapping("/shopping-cart/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderRequest orderRequest) throws UnAuthorizedException, BadRequestException {
        return new ResponseEntity<>(orderService.saveOrder(orderRequest, userPrincipal()), HttpStatus.OK);
    }

    // order
    @GetMapping("/history")
    private ResponseEntity<?> orderHistory() throws UnAuthorizedException {
        return new ResponseEntity<>(orderService.orderHistory(userPrincipal()), HttpStatus.OK);
    }

    @GetMapping("/history/{orderId}")
    public ResponseEntity<?> orderDetails(@PathVariable("orderId") Long orderId) throws UnAuthorizedException, NotFoundException {
        M5UserPrincipal userPrincipal = userPrincipal();
        return new ResponseEntity<>(orderService.findOrder(userPrincipal.getUser(), orderId), HttpStatus.OK);
    }

    @PutMapping("/history/{orderId}/cancel")
    private ResponseEntity<?> cancelOrder(@PathVariable("orderId") Long orderId) throws UnAuthorizedException, NotFoundException {
        M5UserPrincipal userPrincipal = userPrincipal();
        M5Order order = orderService.findOrder(userPrincipal.getUser(), orderId);
        orderService.cancelOrder(order);
        return new ResponseEntity<>("Cancel success", HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<?> accountDetail() throws UnAuthorizedException {
        M5UserPrincipal userPrincipal = userPrincipal();
        return new ResponseEntity<>(userService.account(userPrincipal.getUser()), HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<?> updateAccount(@RequestBody AccountRequest account) throws UnAuthorizedException {
        M5UserPrincipal userPrincipal = userPrincipal();
        return new ResponseEntity<>(userService.updateAccount(userPrincipal.getUser(), account), HttpStatus.OK);
    }

    @PutMapping("/account/change-password")
    public ResponseEntity<?> changePassword(@RequestBody AccountRequest account) throws UnAuthorizedException {
        M5UserPrincipal userPrincipal = userPrincipal();
        return new ResponseEntity<>(userService.changePassword(userPrincipal.getUser(), account), HttpStatus.OK);
    }

    @GetMapping("/account/address")
    public ResponseEntity<?> addressList() throws UnAuthorizedException {
        M5UserPrincipal userPrincipal = userPrincipal();
        return new ResponseEntity<>(addressService.userAddress(userPrincipal.getUser()), HttpStatus.OK);

    }

    @PostMapping("/account/address")
    public ResponseEntity<?> addAddress(@RequestBody M5Address address) throws UnAuthorizedException, NotFoundException, BadRequestException {
        M5UserPrincipal userPrincipal = userPrincipal();
        address.setUser(userPrincipal.getUser());
        return new ResponseEntity<>(addressService.save(address), HttpStatus.OK);
    }

    @DeleteMapping("/account/address/{id}")
    private ResponseEntity<?> deleteAddress(@PathVariable("id") Long id) throws UnAuthorizedException, NotFoundException {
        M5UserPrincipal userPrincipal = userPrincipal();
        addressService.delete(userPrincipal.getUser(), id);
        return new ResponseEntity<>("delete successful", HttpStatus.OK);
    }

    private M5UserPrincipal userPrincipal() throws UnAuthorizedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        M5UserPrincipal userPrincipal = (M5UserPrincipal) authentication.getPrincipal();
        if (userPrincipal == null) {
            throw new UnAuthorizedException("Un Authorized");
        }
        return userPrincipal;
    }
}
