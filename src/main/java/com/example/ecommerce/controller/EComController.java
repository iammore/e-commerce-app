package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@CrossOrigin
public class EComController {
    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }
    @GetMapping("/{category}")
    public List<Product> getAllProducts(@PathVariable String category){
    return productService.getAllProductsByCategory(category.toUpperCase());
    }

    @GetMapping("/product/{productName}")
    public List<Product> getAllProductsByName(@PathVariable String productName){
        return productService.getAllProductsByName(productName);
    }

    @GetMapping("/getCart")
    public List<Cart> getAllItemsFromCart(){
        return cartService.getCart();
    }

    @PostMapping("/addToCart")
    public Cart addToCart(@RequestBody Cart cart){
        return cartService.saveToCart(cart);
    }

    @DeleteMapping("/removeFromCart/{name}")

    public List<Cart> removeFromCart(@PathVariable String name){
        return cartService.removeFromCart(name);
    }
}
