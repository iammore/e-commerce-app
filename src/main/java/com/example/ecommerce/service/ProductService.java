package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ProductService {

   List<Product> getAllProductsByCategory(String category);
   List<Product> getAllProductsByName(String productName);


    List<Product> getAllProducts();
}
