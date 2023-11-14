package com.example.demo.product.core;

import com.example.demo.product.modelMapper.MapperConfig;
import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.modelMapper.ProductDTOConvertor;
import com.example.demo.product.productConfiguration.Product;
import com.example.demo.product.repostory.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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
class ProductServiceImpTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductDTOConvertor productDTOConvertor;
    @Mock
    private Product product;
    @Mock
    private ModelMapper modelMapper;
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
    void areWillThrowExceptionForTakenName() {

        //given
        ProductDTO product = new ProductDTO();
        product.setName("Ivan");


        //when
        given(productRepository.findByName("Ivan"))
                .willReturn(Optional.of(product));

        //then
        assertThatThrownBy(() -> productServiceImp.addNewProducts(product))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product exist!");

        verify(productRepository, never()).save(any());

    }


    @Test
    void getOneProductByIDException() {

        //when
        assertThatThrownBy(() -> productServiceImp.getProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
        //then
        verify(productRepository, never()).findById(any());

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

        ProductDTO product1 = new ProductDTO();
        product1.setName("Ivan");

        assertThatThrownBy(() -> productServiceImp.updateProduct(1L, product1)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product with that 1 doesn't not exist");

    }


    @Test
    void getProductTest() {

        Product product1 = new Product();

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("ddd");

        product1.setId(1L);

        when(productRepository.findAll()).thenReturn(List.of(product1));
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

        when(productRepository.findByName(productDTO.getName())).thenReturn(Optional.of(productDTO));
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

        //when
        productServiceImp.addNewProducts(productDTO);

        //then
        ArgumentCaptor<ProductDTO> productArgumentCaptor =
                ArgumentCaptor.forClass(ProductDTO.class);

        verify(productRepository).save(productDTOConvertor.convertProductDTOToProduct(productDTO));
    }

    @Test
    void shouldUpgradeProductInfo() {

        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("DDD");
        productDTO.setDescription("ddssdffff");

        Product product1 = new Product(
                1L,
                "Ivan",
                "asd",
                20,
                "kg");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        productServiceImp.updateProduct(1L, productDTO);

        product1.setName(productDTO.getName());
        product1.setDescription(productDTO.getDescription());

        assertEquals("DDD", product1.getName());
        assertEquals("ddssdffff", product1.getDescription());

    }
}
