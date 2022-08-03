package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getCart();

    Cart saveToCart(Cart cart);

Cart getOneFromCart(String name);
    List<Cart> removeFromCart(String name);

    List<Cart> getCart(String cartOfUser);
}
