package com.example.challange6.controllers;

import com.example.challange6.models.OrderDetail;
import com.example.challange6.models.Products;
import com.example.challange6.services.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Test
    public void testGetAllProduct() {
        // Mock data for Products
        Products product1 = new Products();
        product1.setId(UUID.randomUUID());
        product1.setProductName("Product 1");
        product1.setPrice(10.0);

        Products product2 = new Products();
        product2.setId(UUID.randomUUID());
        product2.setProductName("Product 2");
        product2.setPrice(20.0);

        // Mock data for OrderDetail
        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setId(UUID.randomUUID());
        orderDetail1.setQuantity(5L);
        orderDetail1.setTotalPrice(50.0);
        orderDetail1.setProduct(product1);

        OrderDetail orderDetail2 = new OrderDetail();
        orderDetail2.setId(UUID.randomUUID());
        orderDetail2.setQuantity(3L);
        orderDetail2.setTotalPrice(60.0);
        orderDetail2.setProduct(product2);

        List<Products> mockProducts = Arrays.asList(product1, product2);

        Mockito.when(productService.getAllProducts(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(mockProducts);

        ResponseEntity<?> responseEntity = productController.getAllProduct(1, 10);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        List<Products> productsList = (List<Products>) responseEntity.getBody();
        for (Products product : productsList) {
            assertNotNull(product.getId());
            assertNotNull(product.getProductName());
            System.out.println("Product ID: " + product.getId() + ", Product Name: " + product.getProductName());
        }
    }
}
