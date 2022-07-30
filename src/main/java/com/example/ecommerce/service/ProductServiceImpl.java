package com.example.ecommerce.service;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;
    @Override
    public List<Product> getAllProductsByCategory(String category) {
        log.info("************finding products for category in repository"+category);
        return productRepository.findAllByCategory(category);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getAllProductsByName(String productName) {
        return productRepository.findAllByNameContaining(productName);
    }

    @Override
    public Product getOneProductByName(String productName) {
        try {
            return productRepository.findByNameContaining(productName);
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }
}
