package com.example.ecommerce.controller;

import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@CrossOrigin @Slf4j
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
    @PostMapping("/addToCart/{productToAdd}")
    public Cart add2Cart(@PathVariable String productToAdd) throws UnsupportedEncodingException {
        String productName = URLDecoder.decode(productToAdd, "UTF-8");
        log.info("finding product for {}", productName);
        if (cartService.getOneFromCart(productName)==null){
            log.info("finding product in product table {}",productName);
            Product product=productService.getOneProductByName(productName);
            Cart cart=new Cart();
            cart.setName(product.getName());
            cart.setDescription(product.getDescription());
            cart.setCategory(product.getCategory());
            cart.setPrice(product.getPrice());
            cart.setRating(product.getRating());
            return cartService.saveToCart(cart);
        }else throw(new DuplicateRequestException("product already exists in cart :"+productName));
    }

    @DeleteMapping("/removeFromCart/{name}")
    public List<Cart> removeFromCart(@PathVariable String name){
        return cartService.removeFromCart(name);
    }
}
