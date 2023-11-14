package com.example.demo.product.repostory;

import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.productConfiguration.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    //    @Query("SELECT name FROM Product products WHERE products.name = ?1")
    //    Optional<ProductDTO> findByProductByName(String name);
    Optional<ProductDTO> findByName(String name);

}
