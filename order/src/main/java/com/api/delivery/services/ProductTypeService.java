package com.api.delivery.services;

import com.api.delivery.domain.ProductType;
import com.api.delivery.dtos.requests.CreateProductTypeRequest;
import com.api.delivery.dtos.requests.UpdateProductTypeRequest;
import com.api.delivery.dtos.responses.ProductTypeResponse;
import com.api.delivery.mappers.ProductTypeMapper;
import com.api.delivery.repositories.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;
    private final ProductTypeMapper productMapper;

    public ProductTypeService(ProductTypeRepository productTypeRepository, ProductTypeMapper productMapper) {
        this.productTypeRepository = productTypeRepository;
        this.productMapper = productMapper;
    }

    public List<ProductTypeResponse> findAll() {
        List<ProductType> productTypes = productTypeRepository.findAll();
        return productMapper.toProductTypeResponseList(productTypes);
    }

    public ProductType findProductTypeEntityById(Long id) {
        return productTypeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product Type not found with ID: " + id));
    }

    public boolean existsByName(String name) {
        return productTypeRepository.existsByName(name);
    }

    public ProductTypeResponse findProductTypeById(Long id) {
        ProductType productType = findProductTypeEntityById(id);
        return productMapper.toProductTypeResponse(productType);
    }

    public ProductTypeResponse createProductType(CreateProductTypeRequest request) {
        ProductType productType = productMapper.toProductTypeEntity(request);
        ProductType createdProductType = productTypeRepository.save(productType);
        return productMapper.toProductTypeResponse(createdProductType);
    }

    public ProductTypeResponse updateProductType(Long id, UpdateProductTypeRequest request) {
        ProductType productType = findProductTypeEntityById(id);
        productMapper.updateProductTypeFromRequest(request, productType);
        ProductType createdProductType = productTypeRepository.save(productType);
        return productMapper.toProductTypeResponse(createdProductType);
    }

    public void deleteProductType(Long id) {
        productTypeRepository.deleteById(id);
    }
}
