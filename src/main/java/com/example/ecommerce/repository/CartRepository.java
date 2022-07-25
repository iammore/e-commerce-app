package com.example.ecommerce.repository;

import com.example.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public interface CartRepository extends JpaRepository<Cart,Long> {


    @Transactional
    List<Cart> deleteByNameContaining(String name);

    Cart findOneByNameContaining(String name);
}
