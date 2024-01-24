package com.ra.service.Impl;

import com.ra.model.dto.request.CartItemRequest;
import com.ra.model.dto.response.CartItemResponse;
import com.ra.model.entity.M5Product;
import com.ra.model.entity.M5ShopCartItem;
import com.ra.model.entity.M5ShoppingCart;
import com.ra.repository.CartItemRepository;
import com.ra.repository.CartRepository;
import com.ra.repository.ProductRepository;
import com.ra.security.principal.M5UserPrincipal;
import com.ra.service.CartService;
import com.ra.util.exception.BadRequestException;
import com.ra.util.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public M5ShoppingCart save(M5ShoppingCart shoppingCart) {
        return cartRepository.save(shoppingCart);
    }

    @Override
    public CartItemResponse saveToCart(M5ShoppingCart shoppingCart, CartItemRequest cartItemRequest) throws NotFoundException, BadRequestException {
        M5Product product = productRepository.findById(cartItemRequest.getProductId()).orElse(null);
        if (product == null) {
            throw new NotFoundException("Not found product with id " + cartItemRequest.getProductId());
        }
        M5ShopCartItem found = cartItemRepository.findByProductAndCart(product, shoppingCart);
        if (product.getStockQuantity() < cartItemRequest.getQuantity()) {
            throw new BadRequestException("item quantity lager than product stock");
        }
        if (found != null) {
            cartItemRequest.setId(found.getId());
            if (product.getStockQuantity() < cartItemRequest.getQuantity() + found.getQuantity()) {
                cartItemRequest.setQuantity(product.getStockQuantity());
            } else {
                cartItemRequest.setQuantity(cartItemRequest.getQuantity() + found.getQuantity());
            }
        }
        M5ShopCartItem saveCartItem = cartItem(cartItemRequest, product, shoppingCart);
        if (shoppingCart.getOrder_quantity() == null) {
            shoppingCart.setOrder_quantity(1);
        } else {
            shoppingCart.setOrder_quantity(shoppingCart.getOrder_quantity() + 1);
        }
//        shoppingCart.setOrder_quantity(shoppingCart.getOrder_quantity() + 1);
        save(shoppingCart);
        return cartItemResponse(cartItemRepository.save(saveCartItem));
    }

    @Override
    public CartItemResponse updateCart(CartItemRequest cartItemRequest, M5ShoppingCart cart) throws NotFoundException, BadRequestException {

        M5ShopCartItem cartItem = cartItemRepository.findByIdAndCart(cartItemRequest.getId(), cart).orElse(null);
        if (cartItem == null) {
            throw new NotFoundException("Not found cartItem with id " + cartItemRequest.getId());
        }

        cartItem.setQuantity(cartItemRequest.getQuantity());
        if (cartItemRequest.getQuantity() > cartItem.getProduct().getStockQuantity()) {
            throw new BadRequestException("cart item quantity can't lager than product quantity");
        }
        return cartItemResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public M5ShoppingCart cart(M5UserPrincipal userPrincipal) {
        M5ShoppingCart shoppingCart = cartRepository.getByUserUserId(userPrincipal.getUser().getUserId()).orElse(null);
        if (shoppingCart == null) {
            M5ShoppingCart newCart = new M5ShoppingCart();
            newCart.setUser(userPrincipal.getUser());
            return save(newCart);
        }
        return shoppingCart;
    }

    @Override
    public void deleteCartItem(Long itemId, M5ShoppingCart cart) throws NotFoundException {
        M5ShopCartItem cartItem = cartItemRepository.findByIdAndCart(itemId, cart).orElse(null);
        if (cartItem == null) {
            throw new NotFoundException("cart item not found");
        }
        cart.setOrder_quantity(cart.getOrder_quantity() - 1);
        save(cart);
        cartItemRepository.deleteById(itemId);
    }

    @Override
    @Transactional
    public void deleteCart(M5ShoppingCart cart) {
        cartItemRepository.deleteAllByCart(cart);
        cart.setOrder_quantity(0);
    }

    private CartItemResponse cartItemResponse(M5ShopCartItem cartItem) {
        return CartItemResponse.builder()
                .productName(cartItem.getProduct().getProductName())
                .unitPrice(cartItem.getProduct().getUnitPrice() * cartItem.getQuantity())
                .quantity(cartItem.getQuantity())
                .id(cartItem.getId())
                .build();
    }

    private M5ShopCartItem cartItem(CartItemRequest cartItemRequest, M5Product product, M5ShoppingCart shoppingCart) {
        M5ShopCartItem cartItem = M5ShopCartItem.builder()
                .quantity(cartItemRequest.getQuantity())
                .cart(shoppingCart)
                .product(product)
                .build();
        if (cartItemRequest.getId() != null) {
            cartItem.setId(cartItemRequest.getId());
        }
        return cartItem;
    }


}
