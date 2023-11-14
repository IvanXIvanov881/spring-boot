package com.example.demo.product.core;
import com.example.demo.product.modelMapper.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    public ProductController(ProductServiceImp productServiceImp) {
        this.productService = productServiceImp;
    }

//    //GET all
    @GetMapping("/all")
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
    public void registerNewProduct(@RequestBody ProductDTO productDTO) {
        productService.addNewProducts(productDTO);
    }

    //DELETE product by ID
    @DeleteMapping(path = "{productId}")
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }

    //PUT product by ID (name and description)
    @PutMapping(path = "{productId}")
    public void updateProduct(@PathVariable("productId") Long productId
            ,@RequestBody(required = false) ProductDTO productDTO)
    {
        productService.updateProduct(productId, productDTO);
    }
}