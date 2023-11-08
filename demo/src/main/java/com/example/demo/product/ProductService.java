package com.example.demo.product;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductDTOMapper productDTOMapper;


    @Autowired
    public ProductService(ProductRepository productRepository, ProductDTOMapper productDTOMapper) {
        this.productRepository = productRepository;
        this.productDTOMapper = productDTOMapper;
    }

    public List<ProductDTO> getProducts() {
        return productRepository.findAll()
                .stream()
                .map(productDTOMapper)
                .collect(Collectors.toList());
    }

    public void addNewProducts(Product product) {
        Optional<Product> productOptional = productRepository.findByProductByName(product.getName());
        if (productOptional.isPresent()) {
            throw new IllegalStateException("product exist!");
        }
        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
         boolean exists = productRepository.existsById(productId);
         if(!exists){
             throw new IllegalStateException("product with name " + productId + " not exists!");
         }
         productRepository.deleteById(productId);
    }

    @Transactional
    public void updateProduct(Long productId,String name, String description) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalStateException(
                "Product with that " + productId + " doesn't not exist"));

        if (name!=null && name.length()>0 && !Objects.equals(product.getName(), name)) {
            product.setName(name);
        }

       if (description!=null && description.length()>0 && !Objects.equals(product.getDescription(), description)) {
            product.setDescription(description);
        }
    }
}
