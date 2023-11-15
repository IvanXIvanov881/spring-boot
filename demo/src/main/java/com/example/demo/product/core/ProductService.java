package com.example.demo.product.core;

import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.productConfiguration.Product;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProduct(Long productId);

    void addNewProducts(ProductDTO productDTO);

    void deleteProduct(Long productId);

    void updateProduct(Product product);
}
