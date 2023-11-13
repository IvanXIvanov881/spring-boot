package com.example.demo.product.repostory;

import com.example.demo.product.productConfiguration.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository underTest;

    @Test
    void shouldCheckForExistingName() {
        //given
        Product product1 = new Product(
                "productTest1",
                "testdiscription",
                2.80,
                "kg"
        );
        underTest.save(product1);

        //when
        Optional<Product> expected = underTest.findByProductByName(product1.getName());

        //then
        assertThat(expected).isNotEmpty();
    }

    @Test
    void shouldCheckForNotExistingName() {
        //given
        Product product1 = new Product(
                "productTest1",
                "testdiscription",
                2.80,
                "kg"
        );
        underTest.save(product1);

        //when
        Optional<Product> expected = underTest.findByProductByName("notExistingName");

        //then
        assertThat(expected).isEmpty();
    }

}