package com.example.demo.product.core;

import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.modelMapper.ProductDTOConvertor;
import com.example.demo.product.productConfiguration.Product;
import com.example.demo.product.repostory.ProductRepository;
import com.example.demo.product.user.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service
public class ProductServiceImp implements ProductService {

    private final ProductRepository productRepository;

    private final ProductDTOConvertor productDTOConvertor;


    @Autowired
    public ProductServiceImp(ProductRepository productRepository, ProductDTOConvertor productDTOConvertor) {
        this.productRepository = productRepository;
        this.productDTOConvertor = productDTOConvertor;
    }


    // GET all products
    public List<ProductDTO> getAllProducts() {

        User currentUser = null;
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (nonNull(principal) && principal instanceof User) {
            currentUser = (User) principal;
        }

        return  productRepository.findAllByUserId(currentUser.getId()).stream().map(productDTOConvertor::convertProductToProductDTO)
                .collect(Collectors.toList());
    }


    //GET product by ID
    public ProductDTO getProduct(Long productId) {
        Product productToSend = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalStateException("product with id: " + productId + " not exists!"));

        return productDTOConvertor.convertProductToProductDTO(productToSend);
    }


    //POST new product
    public void addNewProducts(ProductDTO productDTO) {
        Optional<Product> productOptional = productRepository.findByName(productDTO.getName());
        if (productOptional.isPresent()) {
            throw new IllegalStateException("product exist!");
        }

        User currentUser = null;
        final Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (nonNull(principal) && principal instanceof User) {
            currentUser = (User) principal;
        }

        final Product product1 = productDTOConvertor.convertProductDTOToProduct(productDTO);
        product1.setUser(currentUser);
        productRepository.save(product1);
    }

    //DELETE product
    public void deleteProduct(Long productId) {
        boolean exists = productRepository.existsById(productId);
        if (!exists) {
            throw new IllegalStateException("product with id: " + productId + " not exists!");
        }

        productRepository.deleteById(productId);
    }


    //PUT product (name, description)
    @Transactional
    public void updateProduct(Product productUpdate) {
        Product product = productRepository.findById(productUpdate.getId()).orElseThrow(() -> new IllegalStateException(
                "Product with that " + productUpdate.getId() + " doesn't not exist"));

        if (productUpdate.getName() != null && productUpdate.getName().length() > 0 && !Objects.equals(product.getName(), productUpdate.getName())) {
            product.setName(productUpdate.getName());
        }

        if (productUpdate.getDescription() != null && productUpdate.getDescription().length() > 0 && !Objects.equals(product.getDescription(), productUpdate.getDescription())) {
            product.setDescription(productUpdate.getDescription());
        }

        if (productUpdate.getPrice() >= 0 && !Objects.equals(product.getPrice(), productUpdate.getPrice())) {
            product.setPrice(productUpdate.getPrice());
        }

        if (productUpdate.getUnit() != null && productUpdate.getUnit().length() > 0 && !Objects.equals(product.getUnit(), productUpdate.getUnit())) {
            product.setUnit(productUpdate.getUnit());
        }
    }
}
