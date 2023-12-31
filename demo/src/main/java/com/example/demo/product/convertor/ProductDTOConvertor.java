package com.example.demo.product.convertor;
import com.example.demo.product.dto.ProductDTO;
import com.example.demo.product.entity.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductDTOConvertor {

    @Autowired
    private ModelMapper modelMapper;

    public ProductDTO convertProductToProductDTO(Product product) {

        ProductDTO productDTO = modelMapper.map(product,ProductDTO.class);
        return productDTO;
    }

    public List<ProductDTO> convertAllProductToProductDTO(List<Product> products) {

        return products.stream().map(this::convertProductToProductDTO).collect(Collectors.toList());
    }

    public Product convertProductDTOToProduct(ProductDTO productDTO) {

        Product product= modelMapper.map(productDTO ,Product.class);
        return product;
    }

}
