package com.example.demo.product.repostory;

import com.example.demo.product.productConfiguration.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
    List<Product> findAllByUserId(Long id);


}
