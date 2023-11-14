package com.example.demo.product.core;

import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.modelMapper.ProductDTOConvertor;
import com.example.demo.product.productConfiguration.Product;
import com.example.demo.product.repostory.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService{

    private final ProductRepository productRepository;

    private final ProductDTOConvertor productDTOConvertor;


    @Autowired
    public ProductServiceImp(ProductRepository productRepository, ProductDTOConvertor productDTOConvertor) {
        this.productRepository = productRepository;
        this.productDTOConvertor = productDTOConvertor;
    }


   // GET all products
    public List<ProductDTO> getAllProducts() {

        return productRepository.findAll()
                .stream()
                .map(e-> productDTOConvertor.convertProductToProductDTO(e))
                .collect(Collectors.toList());
    }


    //GET product by ID
    public ProductDTO getProduct(Long productId) {
        Product productToSend = productRepository.findAll()
                .stream()
                .filter(t -> productId.equals(t.getId()))
                .findFirst()
                .orElse(null);

        if (productToSend == null) {
            throw new IllegalStateException("product with id: " + productId + " not exists!");
        }

        return productDTOConvertor.convertProductToProductDTO(productToSend);
    }


    //POST new product
    public void addNewProducts(ProductDTO productDTO) {
        Optional<ProductDTO> productOptional = productRepository.findByName(productDTO.getName());

        if (productOptional.isPresent()) {
            throw new IllegalStateException("product exist!");
        }

        productRepository.save(productDTOConvertor.convertProductDTOToProduct(productDTO));
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
    public void updateProduct(Long productId, ProductDTO productDTO) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException(
                "Product with that " + productId + " doesn't not exist"));

        if (productDTO.getName()!=null && productDTO.getName().length()>0 && !Objects.equals(product.getName(), productDTO.getName())) {
            product.setName(productDTO.getName());
        }

        if (productDTO.getDescription()!=null && productDTO.getDescription().length()>0 && !Objects.equals(product.getDescription(), productDTO.getDescription())) {
            product.setDescription(productDTO.getDescription());
        }


    }
}
