package com.example.demo.product.modelMapper;
import com.example.demo.product.productConfiguration.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOConvertor {

    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO convertProductToProductDTO(Product product) {

        ProductDTO productDTO = modelMapper.map(product,ProductDTO.class);
        return productDTO;
    }

    public Product convertProductDTOToProduct(ProductDTO productDTO) {

        Product product= modelMapper.map(productDTO ,Product.class);
        return product;
    }

}
