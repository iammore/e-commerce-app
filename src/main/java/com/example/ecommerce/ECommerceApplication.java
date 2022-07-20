package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class ECommerceApplication {

    public static void main(String[] args) {


        ApplicationContext applicationContext = SpringApplication.run(ECommerceApplication.class, args);
    }


}