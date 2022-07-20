package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;

import java.util.List;

public interface CartService {
    List<Cart> getCart();

    Cart saveToCart(Cart cart);


    List<Cart> removeFromCart(String name);
}
