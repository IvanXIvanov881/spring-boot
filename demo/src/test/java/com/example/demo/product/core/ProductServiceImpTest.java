package com.example.demo.product.core;

import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.modelMapper.ProductDTOConvertor;
import com.example.demo.product.productConfiguration.Product;
import com.example.demo.product.repostory.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImpTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductDTOConvertor productDTOConvertor;
    @InjectMocks
    private ProductServiceImp productServiceImp;


    @Test
    void canGetAllProducts() {

        //when
        productServiceImp.getAllProducts();

        //then
        verify(productRepository).findAll();
    }


    @Test
    void getOneProductByIDException() {


        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> productServiceImp.getProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");


    }


    @Test
    void tryToDeleteNotExistingProduct() {

        //when
        assertThatThrownBy(() -> productServiceImp.deleteProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
        //then
        verify(productRepository, never()).delete(any());

    }


    @Test
    void mustDeleteSomeProduct() {

        //when
        when(productRepository.existsById(1L)).thenReturn(true);
        productServiceImp.deleteProduct(1L);

        //then
        verify(productRepository).deleteById(1L);

    }

    @Test
    void updateProductException() {

        Product product1 = new Product(    1L,"Ivan",
                "asd",
                222,
                "kg");


        assertThatThrownBy(() -> productServiceImp.updateProduct(product1)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product with that 1 doesn't not exist");

    }


    @Test
    void getProductTest() {

        Product product1 = new Product();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("ddd");

        product1.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productDTOConvertor.convertProductToProductDTO(product1)).thenReturn(productDTO);


        final ProductDTO product2 = productServiceImp.getProduct(1L);


        assertNotNull(product2);

        assertEquals("ddd", product2.getName());

    }

    @Test
    void productExistException() {
        Product product = new Product(
                "Ivan",
                "asd",
                222,
                "kg");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Ivan");


        when(productRepository.findByName("Ivan")).thenReturn(Optional.of(product));

        //when
        assertThatThrownBy(() -> productServiceImp.addNewProducts(productDTO)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product exist!");
    }

    @Test
    void tryAddNewProducts() {

        //given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Ivan");
        productDTO.setDescription("asd");

        Product product = new Product(
                "Ivan",
                "asd",
                222,
                "kg");

        when(productDTOConvertor.convertProductDTOToProduct(productDTO)).thenReturn(product);
        when(productRepository.findByName("Ivan")).thenReturn(Optional.empty());
        //when
        productServiceImp.addNewProducts(productDTO);

        //then
        verify(productRepository).save(product);
    }

    @Test
    void shouldUpgradeProductInfo() {

        Product product = new Product(  1L, "Ivan",
                "asd",
                222,
                "kg");


        Product product1 = new Product(
                1L,
                "DDD",
                "Test",
                100,
                "lb");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        productServiceImp.updateProduct(product);

        product1.setName(product.getName());
        product1.setDescription(product.getDescription());

        assertEquals("Ivan", product1.getName());
        assertEquals("asd", product1.getDescription());
        assertEquals(222, product1.getPrice());
        assertEquals("kg", product1.getUnit());

    }
}
