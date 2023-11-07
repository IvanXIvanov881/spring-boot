package com.example.demo.product;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository repository){
        return args -> {
            Product testProduct = new Product(
                    "Green Apple",
                    "A sweet green apple from Bulgaria",
                    2.19,
                    "kg");

            Product testProduct2 = new Product(
                    "diesel",
                    "A fuel...",
                    2.88,
                    "lb.");

            repository.saveAll(
                    List.of(testProduct,testProduct2)
            );
        };
    }

}
