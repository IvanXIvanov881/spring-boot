package com.example.demo.product.service;
import com.example.demo.product.convertor.ProductDTOConvertor;
import com.example.demo.product.dto.ProductDTO;
import com.example.demo.product.entity.Product;
import com.example.demo.product.entity.User;
import com.example.demo.product.enums.Role;
import com.example.demo.product.repository.ProductRepository;
import com.example.demo.product.service.impl.ProductServiceImpl;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductDTOConvertor productDTOConvertor;
    @InjectMocks
    private ProductServiceImpl productServiceImpl;
    @Mock
    private Authentication authentication;

    @Test
    void getProduct_getOneProductByID_expectedException() {

        //when
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> productServiceImpl.getProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
    }

    @Test
    void deleteProduct_tryToDeleteNotExistingProduct_expectedException() {

        //when
        assertThatThrownBy(() -> productServiceImpl.deleteProduct(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product with id: 1 not exists!");
        //then
        verify(productRepository, never()).delete(any());
    }

    @Test
    void deleteProduct_tryDeleteSomeProduct_expectedToBeDeleted() {

        //given
        when(productRepository.existsById(1L)).thenReturn(true);
        //when
        productServiceImpl.deleteProduct(1L);
        //then
        verify(productRepository).deleteById(1L);
    }

    @Test
    void updateProduct_expectedException() {

        //given
        Product product1 = new Product();
        //when
        product1.setId(1L);
        //then
        assertThatThrownBy(() -> productServiceImpl.updateProduct(product1)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Product with that 1 doesn't not exist");
    }


    @Test
    void getProduct_expectedToGetProduct() {

        //given
        Product product1 = new Product();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("ddd");
        product1.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productDTOConvertor.convertProductToProductDTO(product1)).thenReturn(productDTO);
        //when
        final ProductDTO product2 = productServiceImpl.getProduct(1L);
        //then
        assertNotNull(product2);
        assertEquals("ddd", product2.getName());
    }

    @Test
    void getProduct_searchForNotExistingProduct_expectedException() {

        //given
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Ivan");
        when(productRepository.findByName("Ivan")).thenReturn(Optional.of(product));
        //then
        assertThatThrownBy(() -> productServiceImpl.addNewProducts(productDTO)).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("product exist!");
    }

    @Test
    void updateProduct_shouldUpgradeProductInfo_expectedNewNameAndDescription() {

        //given
        Product product = new Product();
        Product product1 = new Product();
        product.setId(1L);
        product.setName("Ivan");
        product.setDescription("asd");
        product.setPrice(222);
        product.setUnit("kg");
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        //when
        productServiceImpl.updateProduct(product);
        product1.setName(product.getName());
        product1.setDescription(product.getDescription());
        //then
        assertEquals("Ivan", product1.getName());
        assertEquals("asd", product1.getDescription());
        assertEquals(222, product1.getPrice());
        assertEquals("kg", product1.getUnit());
    }

    @Test
    void addNewProducts_getUserAddProduct_expectedNotNull() {

        //given
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
        //when
        productServiceImpl.addNewProducts(productDTO);
        //then
        verify(productRepository).save(product);
    }

    @Test
    void getMyProducts_getMyUserProducts_expectedNotNull() {

        //given
        User user = new User();
        user.setId(1L);
        user.setFirstname("ivan");
        Product product = new Product();
        product.setUser(user);
        product.setName("apple");
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(productRepository.findAllByUserId(1L)).thenReturn(productList);
        //when
        final List<Product> allProducts = productServiceImpl.getMyProducts();
        //then
        assertEquals("apple", allProducts.get(0).getName());
        assertEquals("ivan", allProducts.get(0).getUser().getFirstname());
    }

    @Test
    void getAllProducts_shouldGetAllProducts_ExpectedToReturnAllProducts() {

        //when
        productServiceImpl.getAllProducts();
        //then
        verify(productRepository).findAll();
    }
}
