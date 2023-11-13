package com.example.demo.product.core;

import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.modelMapper.ProductDTOConvertor;
import com.example.demo.product.productConfiguration.Product;
import com.example.demo.product.repostory.ProductRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductDTOConvertor productDTOConvertor;
    @Mock
    private Product product;
    @InjectMocks
    private ProductService productService;

//TODO
//    @Test
//    void canGetAllProducts() {
//
//        //when
//        productService.getAllProducts();
//
//        //then
//        verify(productRepository).findAll();
//    }


    @Test
    void tryGetProductByName() {

        //given
        productService.addNewProducts(new Product("Ivan", "asd", 222, "kg"));
        //then
        verify(productRepository).findByProductByName("Ivan");

    }


    @Test
    void mustAddNewProductInRepository() {

        //given
        Product product = new Product(
                "Ivan",
                "asd",
                222,
                "kg");

        //when
        productService.addNewProducts(product);

        //then
        ArgumentCaptor<Product> productArgumentCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(productArgumentCaptor.capture());

        Product capturedProduct = productArgumentCaptor.getValue();

        assertThat(capturedProduct).isEqualTo(product);

    }


    @Test
    void areWillThrowExceptionForTakenName() {

        //given
        Product product = new Product(
                "Ivan",
                "asd",
                222,
                "kg");

        //when
        given(productRepository.findByProductByName("Ivan"))
                .willReturn(Optional.of(product));

        //then
        assertThatThrownBy(() -> productService.addNewProducts(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product exist!");

        verify(productRepository, never()).save(any());

    }


    @Test
    void getOneProductByIDException() {

        //when
        assertThatThrownBy(() -> productService.getProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
        //then
        verify(productRepository, never()).findById(any());

    }


    @Test
    void tryToDeleteNotExistingProduct() {

        //when
        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
        //then
        verify(productRepository, never()).delete(any());

    }


    @Test
    void tryGetById() {

        //given
        Product product = new Product(
                "Ivan",
                "asd",
                222,
                "kg");

        ProductDTOConvertor productDTOConvertor1 = mock(ProductDTOConvertor.class);
        ProductDTO productDTO = mock(ProductDTO.class);

        //when
        when(productDTOConvertor1.convertProductToProductDTO(product)).thenReturn(new ProductDTO());
        ProductDTO productDTO1 = productDTOConvertor1.convertProductToProductDTO(product);

        //then
        assertNotNull(productDTO1);

    }

    @Test
    void mustReturnSomeProductByID() {

        //given
        ProductDTO productDTO = new ProductDTO();

        //when
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(product.getId()).thenReturn(1L);
        productDTO.setName("GGG");
        when(productDTOConvertor.convertProductToProductDTO(product)).thenReturn(productDTO);

        //then
        final ProductDTO product2 = productService.getProduct(1L);
        assertNotNull(product2);
        assertEquals("GGG", product2.getName());
    }

    @Test
    void mustDeleteSomeProduct() {

        //when
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(1L);

        //then
        verify(productRepository).deleteById(1L);

    }

    @Test
    void updateProductException() {

        assertThatThrownBy(() -> productService.updateProduct(1L, "DDD", "SSS"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product with that 1 doesn't not exist");

    }

    @Test
    void updateProductNameAndDescription() {

        Product product1 = new Product("Ivan", "asd", 20, "kg");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        productService.updateProduct(1L, "DDD", "ddssdffff");

        assertEquals("DDD", product1.getName());
        assertEquals("ddssdffff", product1.getDescription());

    }
}