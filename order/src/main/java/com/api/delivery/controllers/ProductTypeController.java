package com.api.delivery.controllers;

import com.api.delivery.dtos.requests.CreateProductTypeRequest;
import com.api.delivery.dtos.requests.UpdateProductTypeRequest;
import com.api.delivery.dtos.responses.ProductTypeResponse;
import com.api.delivery.services.ProductTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-types")
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> findAll() {
        List<ProductTypeResponse> productTypes = productTypeService.findAll();
        return new ResponseEntity<>(productTypes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> findProductTypeById(@PathVariable(value = "id") Long id) {
        ProductTypeResponse productType = productTypeService.findProductTypeById(id);
        return new ResponseEntity<>(productType, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductTypeResponse> createProductType(@Valid @RequestBody CreateProductTypeRequest productType) {
        ProductTypeResponse createdProductType = productTypeService.createProductType(productType);
        return new ResponseEntity<>(createdProductType, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> updateProductType(@PathVariable(value = "id") Long id, @Valid @RequestBody UpdateProductTypeRequest productType) {
        ProductTypeResponse updatedProductType = productTypeService.updateProductType(id, productType);
        return new ResponseEntity<>(updatedProductType, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable(value = "id") Long id) {
        productTypeService.deleteProductType(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
