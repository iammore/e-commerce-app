package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ECommerceApplication {

    public static void main(String[] args) {


        ApplicationContext applicationContext=SpringApplication.run(ECommerceApplication.class, args);
    }

}
