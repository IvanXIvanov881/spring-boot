package com.example.demo.product.controller;
import com.example.demo.product.convertor.ProductDTOConvertor;
import com.example.demo.product.dto.ProductDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    private ProductService productService;
    private ProductDTOConvertor productDTOConvertor;

    @Autowired
    public ProductController(ProductService productService, ProductDTOConvertor productDTOConvertor) {

        this.productService = productService;
        this.productDTOConvertor = productDTOConvertor;
    }

    //GET One product by ID
    @GetMapping(path = "{productId}")
    public ProductDTO getProduct(@PathVariable("productId") Long productId) {
        return productService.getProduct(productId);
    }

    //GET All my products
    @GetMapping("/my")
    public List<ProductDTO> getMyProducts() {
        return productDTOConvertor.convertAllProductToProductDTO(productService.getMyProducts());
    }

    //GET All products
    @GetMapping("/all")
    public List<ProductDTO> getAllProducts() {
        return productDTOConvertor.convertAllProductToProductDTO(productService.getAllProducts());
    }

    //POST product
    @PostMapping
    public void registerNewProduct(@RequestBody ProductDTO productDTO) {
        productService.addNewProducts(productDTO);
    }

    //DELETE product by ID
    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }

    //UPDATE product by ID (name, description, price and unit)
    @PutMapping
    public void updateProduct(@RequestBody Product product) {
        productService.updateProduct(product);
    }

}