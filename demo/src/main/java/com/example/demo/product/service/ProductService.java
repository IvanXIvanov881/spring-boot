package com.example.demo.product.service;

import com.example.demo.product.dto.ProductDTO;
import com.example.demo.product.entity.Product;

import java.util.List;

public interface ProductService {

    List<Product> getMyProducts();

    List<Product> getAllProducts();
    ProductDTO getProduct(Long productId);

    void addNewProducts(ProductDTO productDTO);

    void deleteProduct(Long productId);

    void updateProduct(Product product);
}
