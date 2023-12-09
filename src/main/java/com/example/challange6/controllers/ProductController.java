package com.example.challange6.controllers;


import com.example.challange6.dto.product.request.AddProductRequestDTO;
import com.example.challange6.dto.product.request.UpdateProductRequestDTO;
import com.example.challange6.dto.product.response.UpdateProductResponseDTO;
import com.example.challange6.models.Products;
import com.example.challange6.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addProductById(@PathVariable UUID id, @RequestBody AddProductRequestDTO addProductRequestDTO) {
        log.info("Received a request to add a product for merchant with ID: {}", id);
        Products newProduct = productService.addProductByMerchantId(id, addProductRequestDTO);
        if (newProduct != null) {
            log.info("Response from addProductById: {}", newProduct);
            return new ResponseEntity<>(newProduct, HttpStatus.OK); // Dont Return Order Details
        } else {
            log.error("Unable to add product for merchant with ID: {}", id);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("uid", id);
            errorResponse.put("message", "UID tidak valid");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable UUID id) {
        log.info("Received a request to delete a product with ID: {}", id);
        Boolean deleteProduct = productService.deleteProductById(id);
        if (deleteProduct) {
            log.info("Product deleted successfully with ID: {}", id);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("uid", id);
            errorResponse.put("message", "Product deleted successfully");
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        } else {
            log.error("Product not found or unable to delete with ID: {}", id);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("uid", id);
            errorResponse.put("message", "Product not found or unable to delete");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllProduct(
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Received a request to get all products.");
        Integer offset = (page - 1) * size;
        List<Products> listProducts = productService.getAllProducts(offset, size);

        if (listProducts.isEmpty()) {
            log.error("Products not found.");
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Product Empty");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } else {
            log.info("Response from getAllProduct: {}", listProducts);
            return new ResponseEntity<>(listProducts, HttpStatus.OK);
        }
    }

    @PutMapping("/{idMerchant}/{idProduct}")
    public ResponseEntity<?> updateProduct(
            @PathVariable UUID idMerchant,
            @PathVariable UUID idProduct,
            @RequestBody UpdateProductRequestDTO updateProductRequestDTO
    ) {
        UpdateProductResponseDTO updateProductResponseDTO = productService.updateProduct(idMerchant, idProduct, updateProductRequestDTO);

        if (updateProductResponseDTO != null) {
            return ResponseEntity.ok(updateProductResponseDTO);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Invalid Product ID");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


}