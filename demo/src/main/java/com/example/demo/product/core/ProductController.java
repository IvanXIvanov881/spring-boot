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
    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }

    //PUT product
    @PutMapping(path = "{productId}")
    public void updateProduct(@PathVariable("productId") Long productId
            ,@RequestParam(required = false) String name
            ,@RequestParam(required = false) String description)
    {
        productService.updateProduct(productId, name, description);
    }
}