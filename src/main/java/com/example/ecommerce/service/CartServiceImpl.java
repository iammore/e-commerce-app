package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService{
    @Autowired
    CartRepository cartRepository;

    @Override

    public List<Cart> removeFromCart(String name) {
        List<Cart> list=new ArrayList<>();
       try{
           return cartRepository.deleteByNameContaining(name);
       }catch (Exception e){
           System.out.println(e.getCause());
       }
return null;
    }

    @Override
    public Cart saveToCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getCart() {
        return cartRepository.findAll();
    }
}
