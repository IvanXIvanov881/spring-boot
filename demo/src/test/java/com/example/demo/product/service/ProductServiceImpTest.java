package com.example.demo.product.service;

import com.example.demo.product.service.impl.ProductServiceImp;
import com.example.demo.product.dto.ProductDTO;
import com.example.demo.product.convertor.ProductDTOConvertor;
import com.example.demo.product.entity.Product;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.enums.Role;
import com.example.demo.product.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
    @Mock
    private Authentication authentication;


    @Test
    void getProduct_getOneProductByID_expectedException() {


        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        //when
        assertThatThrownBy(() -> productServiceImp.getProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
    }


    @Test
    void deleteProduct_tryToDeleteNotExistingProduct_expectedException() {

        //when
        assertThatThrownBy(() -> productServiceImp.deleteProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
        //then
        verify(productRepository, never()).delete(any());

    }


    @Test
    void deleteProduct_tryDeleteSomeProduct_expectedToBeDeleted() {

        //when
        when(productRepository.existsById(1L)).thenReturn(true);

        productServiceImp.deleteProduct(1L);

        //then
        verify(productRepository).deleteById(1L);

    }

    @Test
    void updateProduct_expectedException() {

        Product product1 = new Product();

        product1.setId(1L);

        assertThatThrownBy(() -> productServiceImp.updateProduct(product1)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product with that 1 doesn't not exist");

    }


    @Test
    void getProduct_expectedToGetProduct() {

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
    void getProduct_searchForNotExistingProduct_expectedException() {
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Ivan");


        when(productRepository.findByName("Ivan")).thenReturn(Optional.of(product));

        //when
        assertThatThrownBy(() -> productServiceImp.addNewProducts(productDTO)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product exist!");
    }

    @Test
    void updateProduct_shouldUpgradeProductInfo_expectedNewNameAndDescription() {

        Product product = new Product();
        Product product1 = new Product();
        product.setId(1L);
        product.setName("Ivan");
        product.setDescription("asd");
        product.setPrice(222);
        product.setUnit("kg");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));

        productServiceImp.updateProduct(product);

        product1.setName(product.getName());
        product1.setDescription(product.getDescription());

        assertEquals("Ivan", product1.getName());
        assertEquals("asd", product1.getDescription());
        assertEquals(222, product1.getPrice());
        assertEquals("kg", product1.getUnit());

    }


    @Test
    void addNewProducts_getUserAddProduct_expectedNotNull() {


        ProductDTO productDTO = new ProductDTO();
        Product product = new Product();
        User user = new User();
        user.setId(1L);
        user.setFirstname("ivan");
        user.setLastname("iii");
        user.setEmail("mail");
        user.setPassword("ddd");
        user.setRole(Role.USER);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getPrincipal()).thenReturn(user);

        when(productDTOConvertor.convertProductDTOToProduct(productDTO)).thenReturn(product);

        productServiceImp.addNewProducts(productDTO);

        verify(productRepository).save(product);

    }

    @Test
    void getAllProducts_getAllUserProducts_expectedNotNull() {

        User user = new User();
        Product product = new Product();
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        user.setId(1L);
        user.setProducts(productList);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(productRepository.findAllByUserId(1L)).thenReturn(productList);

        productServiceImp.getAllProducts();
        final List<Product> allByUserId = productRepository.findAllByUserId(1L);

        assertNotNull(allByUserId);
    }
}