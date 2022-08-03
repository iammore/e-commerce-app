package com.example.ecommerce.controller;

import com.example.ecommerce.authentication.AuthenticationRequest;
import com.example.ecommerce.authentication.AuthenticationResponse;
import com.example.ecommerce.authentication.ErrorResponse;
import com.example.ecommerce.authentication.SucessResponse;
import com.example.ecommerce.filters.JwtRequestFilter;
import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.userDetails.MyUserDetailsService;
import com.example.ecommerce.util.JwtUtil;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
//@CrossOrigin(origins = "http://localhost:3000/" )
@Slf4j
public class EComController {
    @Autowired
    ProductService productService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartService cartService;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JwtUtil jwtUtilToken;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @GetMapping("/getAllProducts")
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }
    @GetMapping("all_products/{category}")
    public List<Product> getAllProducts(@PathVariable String category){
        log.info("************finding products for category "+category);
    return  productService.getAllProducts();
    }

    @GetMapping("/categories/{category}")
    public List<Product> getAllProductsBycategory(@PathVariable String category){
        log.info("************finding products for category "+category);
        return productService.getAllProductsByCategory(category);
    }

    @GetMapping("/product/{productName}")
    public List<Product> getAllProductsByName(@PathVariable String productName){

        return productService.getAllProductsByName(productName);
    }
    //@CrossOrigin(origins = "http://localhost:3000/getCart", allowedHeaders = {"authorization"})
  /*  @GetMapping("/getCart/{Bearer}")
    public ResponseEntity<?> getAllItemsFromCart(){
        if (jwtRequestFilter.isLoggedIn()){
            log.info("*-*-*-*-*-*- updating cart for user "+jwtRequestFilter.getUsername()+"*-*-*-*-*-*-");
            List<Cart> cart=cartService.getCart();
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        ErrorResponse errorResponse=new ErrorResponse("User Needs to Login First",new Date(),"you have been logged out");
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);

    }*/

    //get cart for respective user
  @GetMapping("/getCart/{Bearer}")
    public ResponseEntity<?> getAllItemsFromCart(){
        if (jwtRequestFilter.isLoggedIn()){
            String cartOfUser=jwtRequestFilter.getUsername();
            log.info("*-*-*-*-*-*- updating cart for user "+cartOfUser+"*-*-*-*-*-*-");
            List<Cart> cart=cartService.getCart(cartOfUser);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }
        ErrorResponse errorResponse=new ErrorResponse("User Needs to Login First",new Date(),"you have been logged out");
        return new ResponseEntity<>(errorResponse,HttpStatus.UNAUTHORIZED);

    }

    @PostMapping("/addToCart")
    public Cart addToCart(@RequestBody Cart cart){
        return cartService.saveToCart(cart);
    }
    @PostMapping("/addToCart/{productToAdd}")
    public Cart add2Cart(@PathVariable String productToAdd) throws UnsupportedEncodingException {
        String productName = URLDecoder.decode(productToAdd, "UTF-8");
        String UserToken="";
        String UserNameFromToken="";
        if (productName.contains("&&Bearer")){
            UserToken= productName.substring(productName.indexOf("&&Bearer")+9);
            UserNameFromToken=jwtUtilToken.extractUsernmae(UserToken);
            log.info("user from token is"+ UserNameFromToken);
            productName=productName.substring(0,productName.indexOf("&&Bearer"));
        }

        log.info("finding product for {}", productName);
        Product product=productService.getOneProductByName(productName);
        if (cartService.getOneFromCart(productName)==null){
            log.info("finding product in product table {}",productName);

            Cart cart=new Cart();
            cart.setName(product.getName());
            cart.setDescription(product.getDescription());
            cart.setCategory(product.getCategory());
            cart.setPrice(product.getPrice());
            cart.setRating(product.getRating());
            cart.setCount(cart.getCount()+1);
            cart.setUsername(UserNameFromToken);
            return cartService.saveToCart(cart);
        }else {
           Cart cart=cartService.getOneFromCart(productName);
           cart.setCount(cart.getCount()+1);
           cart.setPrice(cart.getPrice()+ product.getPrice());
            return cartService.saveToCart(cart);
        }
    }

    @DeleteMapping("/removeFromCart/{name}")
    public List<Cart> removeFromCart(@PathVariable String name) throws UnsupportedEncodingException {
        String productName = URLDecoder.decode(name, "UTF-8");
        if (productName.contains("&&Bearer")){
            log.info("Bearer token is"+ productName.substring(productName.indexOf("&&Bearer")+9));
            productName=productName.substring(0,productName.indexOf("&&Bearer"));
        }
        if (cartService.getOneFromCart(productName)!=null){
            Cart cart= cartService.getOneFromCart(productName);
            int totalCount= cart.getCount();
            if (totalCount>1){
                Product product=productService.getOneProductByName(productName);
                log.info("************* item is in cart with count as "+cart.getCount()+"*************");
                log.info("************* item is in cart with price  as "+cart.getPrice()+"*************");
                cart.setCount(cart.getCount()-1);
                log.info("************* item is in cart with price  as "+cart.getCount()+"*************");
                cart.setPrice(cart.getPrice()-product.getPrice());
                log.info("************* item is in cart with price  as "+cart.getPrice()+"*************");

                cartRepository.save(cart);
                List<Cart> remainingItems=new ArrayList<>();
                remainingItems.add(cart);
                return remainingItems;
            }
        }
        log.info("************* when no items in cart*************");

        return cartService.removeFromCart(productName);
    }

    @GetMapping("/logout_success")
    public ResponseEntity<?> logoutSucess(){
        SucessResponse sucessResponse=new SucessResponse("Logged out successfully",new Date());
        return new ResponseEntity<>(sucessResponse,HttpStatus.OK);
    }

    @GetMapping("/tp")
    public String tp(){
        return ("<h1>checking logout functionality</h1>");
    }

    @GetMapping("/home")
    public String getHome(){
        return ("<h1>Welcome Home</h1");
    }

    @PostMapping("/login")
    public ResponseEntity<?> doLogin(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }catch (BadCredentialsException e){
            throw new Exception("Incorrect Username or Password ", e);
        }
        final UserDetails userDetails=userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt= jwtUtilToken.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/logout")
    public String doLogout(){
        return "logged out";
    }
}
