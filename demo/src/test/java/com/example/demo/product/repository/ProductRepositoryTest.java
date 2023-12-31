package com.example.demo.product.repository;

import com.example.demo.product.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldCheckForNotExistingName() {
        //given
        Product product1 = new Product();

        productRepository.save(product1);

        //when
        Optional<Product> expected = productRepository.findByName("notExistingName");

        //then
        assertThat(expected).isEmpty();
    }

}