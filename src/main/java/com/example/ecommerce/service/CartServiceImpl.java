package com.example.ecommerce.service;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.repository.CartRepository;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
public class CartServiceImpl implements CartService{
    @Autowired
    CartRepository cartRepository;

    @Override
    public Cart getOneFromCart(String name) {
        log.info("finding one product from cart with the name {}", name);
        try{
            return cartRepository.findOneByNameContaining(name);
        }catch (Exception e){
            log.error(String.valueOf(e.getStackTrace()));
        }
     return null;
    }

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
