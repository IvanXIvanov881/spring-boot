package com.example.demo.product.core;
import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.productConfiguration.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //GET all
    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.getAllProducts();
    }

    //GET One product by ID
    @GetMapping(path = "{productId}")
    public ProductDTO getProduct(@PathVariable("productId") Long productId) {
        return productService.getProduct(productId);
    }

    //POST some product
    @PostMapping
    public void registerNewProduct(@RequestBody Product product) {
        productService.addNewProducts(product);
    }

    //DELETE product by ID
    @DeleteMapping
    public void deleteProduct(@RequestBody Product product) {
        Long productId = product.getId();
        productService.deleteProduct(productId);
    }

    //PUT product by ID (name and description)
    @PutMapping
    public void updateProduct(@RequestBody Product product)
    {
        productService.updateProduct(product.getId(), product.getName(), product.getDescription());
    }
}