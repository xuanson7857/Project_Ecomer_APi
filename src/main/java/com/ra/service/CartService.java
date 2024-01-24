package com.ra.service;

import com.ra.model.dto.request.CartItemRequest;
import com.ra.model.dto.response.CartItemResponse;
import com.ra.model.entity.M5ShopCartItem;
import com.ra.model.entity.M5ShoppingCart;
import com.ra.security.principal.M5UserPrincipal;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;

public interface CartService {
    M5ShoppingCart cart(M5UserPrincipal userPrincipal);
    M5ShoppingCart save(M5ShoppingCart shoppingCart);
    CartItemResponse saveToCart(M5ShoppingCart shoppingCart, CartItemRequest cartItemRequest) throws NotFoundException, BadRequestException;
    CartItemResponse updateCart(CartItemRequest cartItemRequest,M5ShoppingCart cart) throws NotFoundException, BadRequestException;
    void deleteCartItem(Long itemId, M5ShoppingCart cart) throws NotFoundException;
    void deleteCart(M5ShoppingCart cart);
}
