package com.example.demo.product.core;

import com.example.demo.product.auth.AuthenticationService;
import com.example.demo.product.config.JwtService;
import com.example.demo.product.modelMapper.ProductDTO;
import com.example.demo.product.modelMapper.ProductDTOConvertor;
import com.example.demo.product.productConfiguration.Product;
import com.example.demo.product.repostory.ProductRepository;
import com.example.demo.product.user.User;
import com.example.demo.product.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;

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
    private AuthenticationService authenticationService;


    @Test
    void canGetAllProducts() {
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        ProductDTO productDTO2 = new ProductDTO();

        when(productRepository.findAll()).thenReturn(List.of(product));
        productRepository.findAll();
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

        Product product1 = new Product();
        product1.setId(1L);


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
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Ivan");


        when(productRepository.findByName("Ivan")).thenReturn(Optional.of(product));

        //when
        assertThatThrownBy(() -> productServiceImp.addNewProducts(productDTO)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product exist!");
    }

    @Test
    void shouldUpgradeProductInfo() {

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
    void shouldSaveUser() {
        UserRepository userRepository = mock(UserRepository.class);

        User user1 = new User();
        user1.setEmail("mail");

        userRepository.save(user1);
        userRepository.findByEmail("mail");

        verify(userRepository, times(1)).findByEmail("mail");
    }
}
